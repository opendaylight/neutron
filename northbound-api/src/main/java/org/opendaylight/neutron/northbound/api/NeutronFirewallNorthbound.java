/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronFirewallAware;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFirewall;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Neutron Northbound REST APIs for Firewall.<br>
 * This class provides REST APIs for managing neutron Firewall
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
@Path("/fw/firewalls")
public class NeutronFirewallNorthbound extends AbstractNeutronNorthbound {

    private static final String RESOURCE_NAME = "Firewall";

    private NeutronFirewall extractFields(NeutronFirewall o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronFirewallCRUD(this);
        if (answer.getFirewallInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable(RESOURCE_NAME));
        }
        return answer;
    }

    /**
     * Returns a list of all Firewalls */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })

    public Response listGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack firewall attributes
            @QueryParam("id") String queryFirewallUUID,
            @QueryParam("tenant_id") String queryFirewallTenantID,
            @QueryParam("name") String queryFirewallName,
            @QueryParam("description") String queryFirewallDescription,
            @QueryParam("shared") Boolean queryFirewallAdminStateIsUp,
            @QueryParam("status") String queryFirewallStatus,
            @QueryParam("shared") Boolean queryFirewallIsShared,
            @QueryParam("firewall_policy_id") String queryFirewallPolicyID,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
            // sorting not supported
    ) {
        INeutronFirewallCRUD firewallInterface = getNeutronInterfaces().getFirewallInterface();
        List<NeutronFirewall> ans = new ArrayList<NeutronFirewall>();
        for (NeutronFirewall nsg : firewallInterface.getAllNeutronFirewalls()) {
            if ((queryFirewallUUID == null ||
                queryFirewallUUID.equals(nsg.getID())) &&
                (queryFirewallTenantID == null ||
                    queryFirewallTenantID.equals(nsg.getTenantID())) &&
                (queryFirewallName == null ||
                    queryFirewallName.equals(nsg.getFirewallName())) &&
                (queryFirewallDescription == null ||
                    queryFirewallDescription.equals(nsg.getFirewallDescription())) &&
                (queryFirewallAdminStateIsUp == null ||
                    queryFirewallAdminStateIsUp.equals(nsg.getFirewallAdminStateIsUp())) &&
                (queryFirewallStatus == null ||
                    queryFirewallStatus.equals(nsg.getFirewallStatus())) &&
                (queryFirewallIsShared == null ||
                    queryFirewallIsShared.equals(nsg.getFirewallIsShared())) &&
                (queryFirewallPolicyID == null ||
                    queryFirewallPolicyID.equals(nsg.getFirewallPolicyID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg,fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronFirewallRequest(ans)).build();
    }

    /**
     * Returns a specific Firewall */

    @Path("{firewallUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFirewall(@PathParam("firewallUUID") String firewallUUID,
                                      // return fields
                                      @QueryParam("fields") List<String> fields) {
        INeutronFirewallCRUD firewallInterface = getNeutronInterfaces().getFirewallInterface();
        if (!firewallInterface.neutronFirewallExists(firewallUUID)) {
            throw new ResourceNotFoundException(uuidNoExist(RESOURCE_NAME));
        }
        if (fields.size() > 0) {
            NeutronFirewall ans = firewallInterface.getNeutronFirewall(firewallUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronFirewallRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFirewallRequest(firewallInterface.getNeutronFirewall(firewallUUID))).build();
        }
    }

    /**
     * Creates new Firewall */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFirewalls(final NeutronFirewallRequest input) {
        INeutronFirewallCRUD firewallInterface = getNeutronInterfaces().getFirewallInterface();
        if (input.isSingleton()) {
            NeutronFirewall singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronFirewallAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronFirewallAware service = (INeutronFirewallAware) instance;
                        int status = service.canCreateNeutronFirewall(singleton);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDERS);
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDER_LIST);
            }
            firewallInterface.addNeutronFirewall(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronFirewallAware service = (INeutronFirewallAware) instance;
                    service.neutronFirewallCreated(singleton);
                }
            }
        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronFirewallAware.class, this);
            for (NeutronFirewall test : input.getBulk()) {
                if (instances != null) {
                    if (instances.length > 0) {
                        for (Object instance : instances) {
                            INeutronFirewallAware service = (INeutronFirewallAware) instance;
                            int status = service.canCreateNeutronFirewall(test);
                            if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                                return Response.status(status).build();
                            }
                        }
                    } else {
                        throw new ServiceUnavailableException(NO_PROVIDERS);
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDER_LIST);
                }
            }

            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (NeutronFirewall test : input.getBulk()) {
                firewallInterface.addNeutronFirewall(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronFirewallAware service = (INeutronFirewallAware) instance;
                        service.neutronFirewallCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Firewall */

    @Path("{firewallUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFirewall(
            @PathParam("firewallUUID") String firewallUUID, final NeutronFirewallRequest input) {
        INeutronFirewallCRUD firewallInterface = getNeutronInterfaces().getFirewallInterface();

        NeutronFirewall delta = input.getSingleton();
        NeutronFirewall original = firewallInterface.getNeutronFirewall(firewallUUID);

        Object[] instances = NeutronUtil.getInstances(INeutronFirewallAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronFirewallAware service = (INeutronFirewallAware) instance;
                    int status = service.canUpdateNeutronFirewall(delta, original);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * update the object and return it
         */
        firewallInterface.updateNeutronFirewall(firewallUUID, delta);
        NeutronFirewall updatedFirewall = firewallInterface.getNeutronFirewall(firewallUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallAware service = (INeutronFirewallAware) instance;
                service.neutronFirewallUpdated(updatedFirewall);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFirewallRequest(firewallInterface.getNeutronFirewall(firewallUUID))).build();
    }

    /**
     * Deletes a Firewall */

    @Path("{firewallUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFirewall(
            @PathParam("firewallUUID") String firewallUUID) {
        final INeutronFirewallCRUD firewallInterface = getNeutronInterfaces().getFirewallInterface();

        NeutronFirewall singleton = firewallInterface.getNeutronFirewall(firewallUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronFirewallAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronFirewallAware service = (INeutronFirewallAware) instance;
                    int status = service.canDeleteNeutronFirewall(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * remove it and return 204 status
         */
        deleteUuid(RESOURCE_NAME, firewallUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return firewallInterface.removeNeutronFirewall(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallAware service = (INeutronFirewallAware) instance;
                service.neutronFirewallDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
