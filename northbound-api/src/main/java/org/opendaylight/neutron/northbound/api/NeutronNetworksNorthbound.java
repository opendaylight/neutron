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
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronNetworkAware;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronNetwork;

/**
 * Neutron Northbound REST APIs for Network.<br>
 * This class provides REST APIs for managing neutron Networks
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

@Path("/networks")
public class NeutronNetworksNorthbound extends AbstractNeutronNorthbound {

    @Context
    UriInfo uriInfo;

    private static final String RESOURCE_NAME = "Network";

    private NeutronNetwork extractFields(NeutronNetwork o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronNetworkCRUD(this);
        if (answer.getNetworkInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable(RESOURCE_NAME));
        }
        return answer;
    }

    /**
     * Returns a list of all Networks */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listNetworks(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("shared") String queryShared,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("router_external") String queryRouterExternal,
            @QueryParam("provider_network_type") String queryProviderNetworkType,
            @QueryParam("provider_physical_network") String queryProviderPhysicalNetwork,
            @QueryParam("provider_segmentation_id") String queryProviderSegmentationID,
            // linkTitle
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
            // sorting not supported
            ) {
        INeutronNetworkCRUD networkInterface = getNeutronInterfaces().getNetworkInterface();
        List<NeutronNetwork> allNetworks = networkInterface.getAllNetworks();
        List<NeutronNetwork> ans = new ArrayList<NeutronNetwork>();
        Iterator<NeutronNetwork> i = allNetworks.iterator();
        while (i.hasNext()) {
            NeutronNetwork oSN = i.next();
            //match filters: TODO provider extension
            Boolean bAdminStateUp = null;
            Boolean bShared = null;
            Boolean bRouterExternal = null;
            if (queryAdminStateUp != null) {
                bAdminStateUp = Boolean.valueOf(queryAdminStateUp);
            }
            if (queryShared != null) {
                bShared = Boolean.valueOf(queryShared);
            }
            if (queryRouterExternal != null) {
                bRouterExternal = Boolean.valueOf(queryRouterExternal);
            }
            if ((queryID == null || queryID.equals(oSN.getID())) &&
                    (queryName == null || queryName.equals(oSN.getNetworkName())) &&
                    (bAdminStateUp == null || bAdminStateUp.booleanValue() == oSN.isAdminStateUp()) &&
                    (queryStatus == null || queryStatus.equals(oSN.getStatus())) &&
                    (bShared == null || bShared.booleanValue() == oSN.isShared()) &&
                    (bRouterExternal == null || bRouterExternal.booleanValue() == oSN.isRouterExternal()) &&
                    (queryTenantID == null || queryTenantID.equals(oSN.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSN,fields));
                } else {
                    ans.add(oSN);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronNetworkRequest request = (NeutronNetworkRequest) PaginatedRequestFactory.createRequest(limit,
                    marker, pageReverse, uriInfo, ans, NeutronNetwork.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

    return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronNetworkRequest(ans)).build();

    }

    /**
     * Returns a specific Network */

    @Path("{netUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showNetwork(
            @PathParam("netUUID") String netUUID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        INeutronNetworkCRUD networkInterface = getNeutronInterfaces().getNetworkInterface();
        if (!networkInterface.networkExists(netUUID)) {
            throw new ResourceNotFoundException(uuidNoExist(RESOURCE_NAME));
        }
        if (fields.size() > 0) {
            NeutronNetwork ans = networkInterface.getNetwork(netUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronNetworkRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronNetworkRequest(networkInterface.getNetwork(netUUID))).build();
        }
    }

    /**
     * Creates new Networks */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronNetwork.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createNetworks(final NeutronNetworkRequest input) {
        INeutronNetworkCRUD networkInterface = getNeutronInterfaces().getNetworkInterface();
        if (input.isSingleton()) {
            NeutronNetwork singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronNetworkAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronNetworkAware service = (INeutronNetworkAware) instance;
                    int status = service.canCreateNetwork(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            }

            // add network to cache
            singleton.initDefaults();
            networkInterface.addNetwork(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronNetworkAware service = (INeutronNetworkAware) instance;
                    service.neutronNetworkCreated(singleton);
                }
            }

        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronNetworkAware.class, this);
            if (instances != null) {
                for (NeutronNetwork test : input.getBulk()) {
                    for (Object instance: instances) {
                        INeutronNetworkAware service = (INeutronNetworkAware) instance;
                        int status = service.canCreateNetwork(test);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                }
            }

            // now that everything passed, add items to the cache
            for (NeutronNetwork test : input.getBulk()) {
                test.initDefaults();
                networkInterface.addNetwork(test);
                if (instances != null) {
                    for (Object instance: instances) {
                        INeutronNetworkAware service = (INeutronNetworkAware) instance;
                        service.neutronNetworkCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Network */
    @Path("{netUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateNetwork(
            @PathParam("netUUID") String netUUID, final NeutronNetworkRequest input
            ) {
        INeutronNetworkCRUD networkInterface = getNeutronInterfaces().getNetworkInterface();

        NeutronNetwork updatedObject = input.getSingleton();
        NeutronNetwork original = networkInterface.getNetwork(netUUID);

        /*
         *  note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */

        updatedObject.setID(netUUID);
        updatedObject.setTenantID(original.getTenantID());
        Object[] instances = NeutronUtil.getInstances(INeutronNetworkAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronNetworkAware service = (INeutronNetworkAware) instance;
                int status = service.canUpdateNetwork(updatedObject, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        // update network object
        networkInterface.updateNetwork(netUUID, updatedObject);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronNetworkAware service = (INeutronNetworkAware) instance;
                service.neutronNetworkUpdated(updatedObject);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronNetworkRequest(networkInterface.getNetwork(netUUID))).build();
    }

    /**
     * Deletes a Network */

    @Path("{netUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteNetwork(
            @PathParam("netUUID") String netUUID) {
        final INeutronNetworkCRUD networkInterface = getNeutronInterfaces().getNetworkInterface();

        NeutronNetwork singleton = networkInterface.getNetwork(netUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronNetworkAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronNetworkAware service = (INeutronNetworkAware) instance;
                int status = service.canDeleteNetwork(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        deleteUuid(RESOURCE_NAME, netUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return networkInterface.removeNetwork(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronNetworkAware service = (INeutronNetworkAware) instance;
                service.neutronNetworkDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
