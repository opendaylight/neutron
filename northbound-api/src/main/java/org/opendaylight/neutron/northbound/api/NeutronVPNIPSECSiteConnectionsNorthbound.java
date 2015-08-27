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
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionAware;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronVPNIPSECSiteConnection;

/**
 * Neutron Northbound REST APIs for VPN IPSEC SiteConnection.<br>
 * This class provides REST APIs for managing neutron VPN IPSEC SiteConnections
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

@Path("/vpn/ipsecsiteconnections")
public class NeutronVPNIPSECSiteConnectionsNorthbound extends AbstractNeutronNorthbound {

    private static final String RESOURCE_NAME = "VPNIPSECSiteConnections";

    private NeutronVPNIPSECSiteConnection extractFields(NeutronVPNIPSECSiteConnection o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronVPNIPSECSiteConnectionsCRUD(this);
        if (answer.getVPNIPSECSiteConnectionsInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable(RESOURCE_NAME));
        }
        return answer;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN IPSEC SiteConnections
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVPNIPSECSiteConnections(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID, @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName, @QueryParam("description") String queryDescription,
            @QueryParam("peer_address") String queryPeerAddress, @QueryParam("peer_id") String queryPeerID,
            @QueryParam("route_mode") String queryRouteMode, @QueryParam("mtu") Integer queryMtu,
            @QueryParam("auth_mode") String queryAuthMode, @QueryParam("psk") String queryPsk,
            @QueryParam("initiator") String queryInitiator, @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus, @QueryParam("ikepolicy_id") String queryIkePolicyID,
            @QueryParam("ipsecpolicy_id") String queryIpSecPolicyID,
            @QueryParam("vpnservice_id") String queryVpnServiceID
    // pagination and sorting are TODO
    ) {
        INeutronVPNIPSECSiteConnectionsCRUD labelInterface = getNeutronInterfaces()
                .getVPNIPSECSiteConnectionsInterface();
        List<NeutronVPNIPSECSiteConnection> allNeutronVPNIPSECSiteConnection = labelInterface
                .getAllNeutronVPNIPSECSiteConnections();
        List<NeutronVPNIPSECSiteConnection> ans = new ArrayList<NeutronVPNIPSECSiteConnection>();
        Iterator<NeutronVPNIPSECSiteConnection> i = allNeutronVPNIPSECSiteConnection.iterator();
        while (i.hasNext()) {
            NeutronVPNIPSECSiteConnection oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID()))
                    && (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))
                    && (queryName == null || queryName.equals(oSS.getName()))
                    && (queryDescription == null || queryDescription.equals(oSS.getDescription()))
                    && (queryPeerAddress == null || queryPeerAddress.equals(oSS.getPeerAddress()))
                    && (queryPeerID == null || queryPeerID.equals(oSS.getPeerID()))
                    && (queryRouteMode == null || queryRouteMode.equals(oSS.getRouteMode()))
                    && (queryMtu == null || queryMtu.equals(oSS.getMtu()))
                    && (queryAuthMode == null || queryAuthMode.equals(oSS.getAuthMode()))
                    && (queryPsk == null || queryPsk.equals(oSS.getPreSharedKey()))
                    && (queryInitiator == null || queryInitiator.equals(oSS.getInitiator()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(oSS.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(oSS.getStatus()))
                    && (queryIkePolicyID == null || queryIkePolicyID.equals(oSS.getIkePolicyID()))
                    && (queryIpSecPolicyID == null || queryIpSecPolicyID.equals(oSS.getIpsecPolicyID()))
                    && (queryVpnServiceID == null || queryVpnServiceID.equals(oSS.getVpnServiceID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS, fields));
                } else {
                    ans.add(oSS);
                }
            }
        }

        // TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVPNIPSECSiteConnectionRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IPSEC SiteConnection
     */

    @Path("{connectionID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showVPNIPSECSiteConnection(@PathParam("connectionID") String connectionID,
    // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronVPNIPSECSiteConnectionsCRUD connectionInterface = getNeutronInterfaces()
                .getVPNIPSECSiteConnectionsInterface();
        if (!connectionInterface.neutronVPNIPSECSiteConnectionsExists(connectionID)) {
            throw new ResourceNotFoundException("NeutronVPNIPSECSiteConnections ID not found");
        }
        if (fields.size() > 0) {
            NeutronVPNIPSECSiteConnection ans = connectionInterface.getNeutronVPNIPSECSiteConnections(connectionID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVPNIPSECSiteConnectionRequest(extractFields(ans, fields)))
                    .build();
        } else {
            return Response
                    .status(HttpURLConnection.HTTP_OK)
                    .entity(new NeutronVPNIPSECSiteConnectionRequest(connectionInterface
                            .getNeutronVPNIPSECSiteConnections(connectionID))).build();
        }
    }

    /**
     * Creates new VPN IPSEC SiteConnection
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNIPSECSiteConnection.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVPNIPSECSiteConnection(final NeutronVPNIPSECSiteConnectionRequest input) {
        INeutronVPNIPSECSiteConnectionsCRUD ipsecSiteConnectionsInterface = getNeutronInterfaces()
                .getVPNIPSECSiteConnectionsInterface();
        if (input.isSingleton()) {
            NeutronVPNIPSECSiteConnection singleton = input.getSingleton();
            Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECSiteConnectionAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                        int status = service.canCreateNeutronVPNIPSECSiteConnection(singleton);
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
             * add ipsecSiteConnections to the cache
             */
            ipsecSiteConnectionsInterface.addNeutronVPNIPSECSiteConnections(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                    service.neutronVPNIPSECSiteConnectionCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton ipsecSiteConnections creates supported
             */
            throw new BadRequestException("Only singleton ipsecSiteConnections creates supported");
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a VPN IPSEC SiteConnection
     */
    @Path("{connectionID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNIPSECSiteConnection(@PathParam("connectionID") String connectionID,
            final NeutronVPNIPSECSiteConnectionRequest input) {
        INeutronVPNIPSECSiteConnectionsCRUD ipsecSiteConnectionsInterface = getNeutronInterfaces()
                .getVPNIPSECSiteConnectionsInterface();

        NeutronVPNIPSECSiteConnection singleton = input.getSingleton();
        NeutronVPNIPSECSiteConnection original = ipsecSiteConnectionsInterface
                .getNeutronVPNIPSECSiteConnections(connectionID);

        Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECSiteConnectionAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                    int status = service.canUpdateNeutronVPNIPSECSiteConnection(singleton, original);
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
         * update the ipsecSiteConnections entry and return the modified object
         */
        ipsecSiteConnectionsInterface.updateNeutronVPNIPSECSiteConnections(connectionID, singleton);
        NeutronVPNIPSECSiteConnection updatedVPNIKEPolicy = ipsecSiteConnectionsInterface
                .getNeutronVPNIPSECSiteConnections(connectionID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                service.neutronVPNIPSECSiteConnectionUpdated(updatedVPNIKEPolicy);
            }
        }
        return Response
                .status(HttpURLConnection.HTTP_OK)
                .entity(new NeutronVPNIPSECSiteConnectionRequest(ipsecSiteConnectionsInterface
                        .getNeutronVPNIPSECSiteConnections(connectionID))).build();
    }

    /**
     * Deletes a VPN IPSEC SiteConnection
     */

    @Path("{connectionID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVPNIPSECSiteConnection(@PathParam("connectionID") String connectionID) {
        final INeutronVPNIPSECSiteConnectionsCRUD ipsecSiteConnectionsInterface = getNeutronInterfaces()
                .getVPNIPSECSiteConnectionsInterface();

        NeutronVPNIPSECSiteConnection singleton = ipsecSiteConnectionsInterface
                .getNeutronVPNIPSECSiteConnections(connectionID);
        Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECSiteConnectionAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                    int status = service.canDeleteNeutronVPNIPSECSiteConnection(singleton);
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
        deleteUuid(RESOURCE_NAME, connectionID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return ipsecSiteConnectionsInterface.removeNeutronVPNIPSECSiteConnections(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
                service.neutronVPNIPSECSiteConnectionDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }

}
