/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortAware;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronPort;

/**
 * Neutron Northbound REST APIs.<br>
 * This class provides REST APIs for managing neutron port objects
 *
 * <br>
 * <br>
 * Authentication scheme : <b>HTTP Basic</b><br>
 * Authentication realm : <b>opendaylight</b><br>
 * Transport : <b>HTTP and HTTPS</b><br>
 * <br>
 * HTTPS Authentication is disabled by default. Administrator can enable it in
 * tomcat-server.xml after adding a proper keystore / SSL certificate from a
 * trusted authority.<br>
 * More info :
 * http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html#Configuration
 *
 */

@Path("/ports")
public class NeutronPortsNorthbound extends AbstractNeutronNorthbound {

    private static final String RESOURCE_NAME = "Port";

    private NeutronPort extractFields(NeutronPort o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces(boolean needNetworks, boolean needSubnets) {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronPortCRUD(this);
        if (answer.getPortInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable(RESOURCE_NAME));
        }
        if (needNetworks) {
            answer = answer.fetchINeutronNetworkCRUD( this);
            if (answer.getNetworkInterface() == null) {
                throw new ServiceUnavailableException("Network CRUD Interface "
                        + RestMessages.SERVICEUNAVAILABLE.toString());
            }
        }
        if (needSubnets) {
            answer = answer.fetchINeutronSubnetCRUD( this);
            if (answer.getSubnetInterface() == null) {
                throw new ServiceUnavailableException("Subnet CRUD Interface "
                        + RestMessages.SERVICEUNAVAILABLE.toString());
            }
        }
        return answer;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all Ports */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listPorts(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("network_id") String queryNetworkID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("mac_address") String queryMACAddress,
            @QueryParam("device_id") String queryDeviceID,
            @QueryParam("device_owner") String queryDeviceOwner,
            @QueryParam("tenant_id") String queryTenantID,
            // linkTitle
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
            // sorting not supported
            ) {
        INeutronPortCRUD portInterface = getNeutronInterfaces(false, false).getPortInterface();
        List<NeutronPort> allPorts = portInterface.getAllPorts();
        List<NeutronPort> ans = new ArrayList<NeutronPort>();
        Iterator<NeutronPort> i = allPorts.iterator();
        while (i.hasNext()) {
            NeutronPort oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryNetworkID == null || queryNetworkID.equals(oSS.getNetworkUUID())) &&
                    (queryName == null || queryName.equals(oSS.getName())) &&
                    (queryAdminStateUp == null || queryAdminStateUp.equals(oSS.getAdminStateUp())) &&
                    (queryStatus == null || queryStatus.equals(oSS.getStatus())) &&
                    (queryMACAddress == null || queryMACAddress.equals(oSS.getMacAddress())) &&
                    (queryDeviceID == null || queryDeviceID.equals(oSS.getDeviceID())) &&
                    (queryDeviceOwner == null || queryDeviceOwner.equals(oSS.getDeviceOwner())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS,fields));
                } else {
                    ans.add(oSS);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronPortRequest request = (NeutronPortRequest) PaginatedRequestFactory.createRequest(limit,
                    marker, pageReverse, uriInfo, ans, NeutronPort.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronPortRequest(ans)).build();
    }

    /**
     * Returns a specific Port */

    @Path("{portUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showPort(
            @PathParam("portUUID") String portUUID,
            // return fields
            @QueryParam("fields") List<String> fields ) {
        INeutronPortCRUD portInterface = getNeutronInterfaces(false, false).getPortInterface();
        if (!portInterface.portExists(portUUID)) {
            throw new ResourceNotFoundException(uuidNoExist(RESOURCE_NAME));
        }
        if (fields.size() > 0) {
            NeutronPort ans = portInterface.getPort(portUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronPortRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronPortRequest(portInterface.getPort(portUUID))).build();
        }
    }

    /**
     * Creates new Ports */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createPorts(final NeutronPortRequest input) {
        NeutronCRUDInterfaces interfaces = getNeutronInterfaces(true, true);
        INeutronPortCRUD portInterface = interfaces.getPortInterface();
        if (input.isSingleton()) {
            NeutronPort singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronPortAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronPortAware service = (INeutronPortAware) instance;
                    int status = service.canCreatePort(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            }

            // add the port to the cache
            portInterface.addPort(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronPortAware service = (INeutronPortAware) instance;
                    service.neutronPortCreated(singleton);
                }
            }
        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronPortAware.class, this);
            if (instances != null) {
                for (NeutronPort test : input.getBulk()) {
                    for (Object instance : instances) {
                        INeutronPortAware service = (INeutronPortAware) instance;
                        int status = service.canCreatePort(test);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                }
            }

            //once everything has passed, then we can add to the cache
            for (NeutronPort test : input.getBulk()) {
                portInterface.addPort(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronPortAware service = (INeutronPortAware) instance;
                        service.neutronPortCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Port */

    @Path("{portUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updatePort(
            @PathParam("portUUID") String portUUID,
            NeutronPortRequest input
            ) {
        NeutronCRUDInterfaces interfaces = getNeutronInterfaces(false, true);
        INeutronPortCRUD portInterface = interfaces.getPortInterface();
        NeutronPort original = portInterface.getPort(portUUID);

        /*
         * note: what we would like to get is the complete object as it
         * is known by neutron.  Until then, patch what we *do* get
         * so that we don't lose already known information
         */

        NeutronPort updatedObject = input.getSingleton();
        if (updatedObject.getID() == null) {
            updatedObject.setID(portUUID);
        }
        if (updatedObject.getTenantID() == null) {
            updatedObject.setTenantID(original.getTenantID());
        }
        if (updatedObject.getNetworkUUID() == null) {
            updatedObject.setNetworkUUID(original.getNetworkUUID());
        }
        if (updatedObject.getMacAddress() == null) {
            updatedObject.setMacAddress(original.getMacAddress());
        }
        if (updatedObject.getFixedIPs() == null) {
            updatedObject.setFixedIPs(original.getFixedIPs());
        }

        Object[] instances = NeutronUtil.getInstances(INeutronPortAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronPortAware service = (INeutronPortAware) instance;
                int status = service.canUpdatePort(updatedObject, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        //        TODO: Support change of security groups
        // update the port and return the modified object
        portInterface.updatePort(portUUID, updatedObject);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronPortAware service = (INeutronPortAware) instance;
                service.neutronPortUpdated(updatedObject);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronPortRequest(updatedObject)).build();

    }

    /**
     * Deletes a Port */

    @Path("{portUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deletePort(
            @PathParam("portUUID") String portUUID) {
        final INeutronPortCRUD portInterface = getNeutronInterfaces(false, false).getPortInterface();

        NeutronPort singleton = portInterface.getPort(portUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronPortAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronPortAware service = (INeutronPortAware) instance;
                int status = service.canDeletePort(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }
        deleteUuid(RESOURCE_NAME, portUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return portInterface.removePort(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronPortAware service = (INeutronPortAware) instance;
                service.neutronPortDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
