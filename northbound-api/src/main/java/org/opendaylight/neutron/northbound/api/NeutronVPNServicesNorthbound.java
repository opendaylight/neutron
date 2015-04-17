/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.opendaylight.neutron.spi.INeutronVPNServiceAware;
import org.opendaylight.neutron.spi.INeutronVPNServiceCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronVPNService;

/**
 * Neutron Northbound REST APIs for VPN Service.<br>
 * This class provides REST APIs for managing neutron VPN Services
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

@Path("/vpn/vpnservices")
public class NeutronVPNServicesNorthbound {

    private NeutronVPNService extractFields(NeutronVPNService o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN Services
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response listVPNServices(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack VPNService attributes
            @QueryParam("id") String queryID, @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName, @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("router_id") String queryRouterID, @QueryParam("status") String queryStatus,
            @QueryParam("subnet_id") String querySubnetID,
            // pagination
            @QueryParam("limit") String limit, @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronVPNServiceCRUD VPNServiceInterface = NeutronCRUDInterfaces.getINeutronVPNServiceCRUD(this);
        if (VPNServiceInterface == null) {
            throw new ServiceUnavailableException("VPNService CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronVPNService> allVPNService = VPNServiceInterface.getAllVPNService();
        List<NeutronVPNService> ans = new ArrayList<NeutronVPNService>();
        Iterator<NeutronVPNService> i = allVPNService.iterator();
        while (i.hasNext()) {
            NeutronVPNService oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID()))
                    && (queryName == null || queryName.equals(oSS.getName()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(oSS.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(oSS.getStatus()))
                    && (querySubnetID == null || querySubnetID.equals(oSS.getSubnetUUID()))
                    && (queryRouterID == null || queryRouterID.equals(oSS.getRouterUUID()))
                    && (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0)
                    ans.add(extractFields(oSS, fields));
                else
                    ans.add(oSS);
            }
        }

        return Response.status(200).entity(new NeutronVPNServiceRequest(ans)).build();
    }

    /**
     * Returns a specific VPN Service
     */

    @Path("{serviceID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"), @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response showVPNService(@PathParam("serviceID") String serviceID,
    // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronVPNServiceCRUD VPNServiceInterface = NeutronCRUDInterfaces.getINeutronVPNServiceCRUD(this);
        if (VPNServiceInterface == null) {
            throw new ServiceUnavailableException("VPNService CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!VPNServiceInterface.neutronVPNServiceExists(serviceID)) {
            throw new ResourceNotFoundException("VPNService UUID does not exist.");
        }
        if (fields.size() > 0) {
            NeutronVPNService ans = VPNServiceInterface.getVPNService(serviceID);
            return Response.status(200).entity(new NeutronVPNServiceRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(200)
                    .entity(new NeutronVPNServiceRequest(VPNServiceInterface.getVPNService(serviceID))).build();
        }
    }

    /**
     * Creates new VPN Service
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNService.class)
    @StatusCodes({ @ResponseCode(code = 201, condition = "Created"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"), @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"), @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response createVPNService(final NeutronVPNServiceRequest input) {
        INeutronVPNServiceCRUD VPNServiceInterface = NeutronCRUDInterfaces.getINeutronVPNServiceCRUD(this);
        if (VPNServiceInterface == null) {
            throw new ServiceUnavailableException("VPNService CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronVPNService singleton = input.getSingleton();

            /*
             * Verify that the VPNService doesn't already exist.
             */
            if (VPNServiceInterface.neutronVPNServiceExists(singleton.getID())) {
                throw new BadRequestException("VPNService UUID already exists");
            }
            Object[] instances = NeutronUtil.getInstances(INeutronVPNServiceAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                        int status = service.canCreateNeutronVPNService(singleton);
                        if (status < 200 || status > 299) {
                            return Response.status(status).build();
                        }
                    }
                } else {
                    throw new ServiceUnavailableException("No providers registered.  Please try again later");
                }
            } else {
                throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
            }

            VPNServiceInterface.addVPNService(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                    service.neutronVPNServiceCreated(singleton);
                }
            }
        } else {
            List<NeutronVPNService> bulk = input.getBulk();
            Iterator<NeutronVPNService> i = bulk.iterator();
            HashMap<String, NeutronVPNService> testMap = new HashMap<String, NeutronVPNService>();
            Object[] instances = NeutronUtil.getInstances(INeutronVPNServiceAware.class, this);
            while (i.hasNext()) {
                NeutronVPNService test = i.next();

                /*
                 * Verify that the VPNService doesn't already exist
                 */

                if (VPNServiceInterface.neutronVPNServiceExists(test.getID())) {
                    throw new BadRequestException("VPN Service UUID already is already created");
                }
                if (testMap.containsKey(test.getID())) {
                    throw new BadRequestException("VPN Service UUID already exists");
                }
                if (instances != null) {
                    if (instances.length > 0) {
                        for (Object instance : instances) {
                            INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                            int status = service.canCreateNeutronVPNService(test);
                            if (status < 200 || status > 299) {
                                return Response.status(status).build();
                            }
                        }
                    } else {
                        throw new ServiceUnavailableException("No providers registered.  Please try again later");
                    }
                } else {
                    throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
                }
            }
            /*
             * now, each element of the bulk request can be added to the cache
             */
            i = bulk.iterator();
            while (i.hasNext()) {
                NeutronVPNService test = i.next();
                VPNServiceInterface.addVPNService(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                        service.neutronVPNServiceCreated(test);
                    }
                }
            }
        }
        return Response.status(201).entity(input).build();
    }

    /**
     * Updates a VPN Service
     */
    @Path("{serviceID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"), @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response updateVPNService(@PathParam("serviceID") String serviceID, final NeutronVPNServiceRequest input) {
        INeutronVPNServiceCRUD VPNServiceInterface = NeutronCRUDInterfaces.getINeutronVPNServiceCRUD(this);
        if (VPNServiceInterface == null) {
            throw new ServiceUnavailableException("VPNService CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * verify the VPNService exists and there is only one delta provided
         */
        if (!VPNServiceInterface.neutronVPNServiceExists(serviceID)) {
            throw new ResourceNotFoundException("VPNService UUID does not exist.");
        }
        if (!input.isSingleton()) {
            throw new BadRequestException("Only singleton edit supported");
        }
        NeutronVPNService delta = input.getSingleton();
        NeutronVPNService original = VPNServiceInterface.getVPNService(serviceID);

        /*
         * updates restricted by Neutron
         */
        if (delta.getID() != null || delta.getTenantID() != null || delta.getName() != null
                || delta.getRouterUUID() != null || delta.getStatus() != null || delta.getAdminStateUp() != null
                || delta.getSubnetUUID() != null) {
            throw new BadRequestException("Attribute edit blocked by Neutron");
        }

        Object[] instances = NeutronUtil.getInstances(INeutronVPNServiceAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                    int status = service.canUpdateNeutronVPNService(delta, original);
                    if (status < 200 || status > 299) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException("No providers registered.  Please try again later");
            }
        } else {
            throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
        }

        /*
         * update the object and return it
         */
        VPNServiceInterface.updateVPNService(serviceID, delta);
        NeutronVPNService updatedVPNService = VPNServiceInterface.getVPNService(serviceID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                service.neutronVPNServiceUpdated(updatedVPNService);
            }
        }
        return Response.status(200).entity(new NeutronVPNServiceRequest(VPNServiceInterface.getVPNService(serviceID)))
                .build();
    }

    /**
     * Deletes a VPN Service
     */

    @Path("{serviceID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = 204, condition = "No Content"),
            @ResponseCode(code = 401, condition = "Unauthorized"), @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response deleteVPNService(@PathParam("serviceID") String serviceID) {
        INeutronVPNServiceCRUD VPNServiceInterface = NeutronCRUDInterfaces.getINeutronVPNServiceCRUD(this);
        if (VPNServiceInterface == null) {
            throw new ServiceUnavailableException("VPNService CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * verify the VPNService exists and it isn't currently in use
         */
        if (!VPNServiceInterface.neutronVPNServiceExists(serviceID)) {
            throw new ResourceNotFoundException("VPNService UUID does not exist.");
        }
        if (VPNServiceInterface.neutronVPNServiceInUse(serviceID)) {
            return Response.status(409).build();
        }
        NeutronVPNService singleton = VPNServiceInterface.getVPNService(serviceID);
        Object[] instances = NeutronUtil.getInstances(INeutronVPNServiceAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                    int status = service.canDeleteNeutronVPNService(singleton);
                    if (status < 200 || status > 299) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException("No providers registered.  Please try again later");
            }
        } else {
            throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
        }

        VPNServiceInterface.removeVPNService(serviceID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNServiceAware service = (INeutronVPNServiceAware) instance;
                service.neutronVPNServiceDeleted(singleton);
            }
        }
        return Response.status(204).build();
    }
}
