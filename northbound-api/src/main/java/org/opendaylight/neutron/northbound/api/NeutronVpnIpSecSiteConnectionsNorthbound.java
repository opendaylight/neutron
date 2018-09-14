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
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
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
import org.apache.aries.blueprint.annotation.service.Reference;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronVpnIpSecSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.NeutronVpnIpSecSiteConnection;

/**
 * Neutron Northbound REST APIs for VPN IPSEC SiteConnection.<br>
 */
@Singleton
@Path("/vpn/ipsecsiteconnections")
public final class NeutronVpnIpSecSiteConnectionsNorthbound
        extends AbstractNeutronNorthbound<NeutronVpnIpSecSiteConnection,
        NeutronVpnIpSecSiteConnectionRequest, INeutronVpnIpSecSiteConnectionsCRUD> {

    private static final String RESOURCE_NAME = "VPNIPSECSiteConnections";

    @Inject
    public NeutronVpnIpSecSiteConnectionsNorthbound(@Reference INeutronVpnIpSecSiteConnectionsCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all VPN IPSEC SiteConnections.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVpnIPSecSiteConnections(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName,
            @QueryParam("peer_address") String queryPeerAddress,
            @QueryParam("peer_id") String queryPeerID,
            @QueryParam("route_mode") String queryRouteMode,
            @QueryParam("mtu") Integer queryMtu,
            @QueryParam("auth_mode") String queryAuthMode,
            @QueryParam("psk") String queryPsk,
            @QueryParam("initiator") String queryInitiator,
            @QueryParam("admin_state_up") Boolean queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("ikepolicy_id") String queryIkePolicyID,
            @QueryParam("ipsecpolicy_id") String queryIpSecPolicyID,
            @QueryParam("vpnservice_id") String queryVpnServiceID
    // pagination and sorting are TODO
    ) {
        INeutronVpnIpSecSiteConnectionsCRUD labelInterface = getNeutronCRUD();
        List<NeutronVpnIpSecSiteConnection> allNeutronVpnIPSecSiteConnection = labelInterface.getAll();
        List<NeutronVpnIpSecSiteConnection> ans = new ArrayList<>();
        for (NeutronVpnIpSecSiteConnection siteConnection : allNeutronVpnIPSecSiteConnection) {
            if ((queryID == null || queryID.equals(siteConnection.getID()))
                    && (queryTenantID == null || queryTenantID.equals(siteConnection.getTenantID()))
                    && (queryName == null || queryName.equals(siteConnection.getName()))
                    && (queryPeerAddress == null || queryPeerAddress.equals(siteConnection.getPeerAddress()))
                    && (queryPeerID == null || queryPeerID.equals(siteConnection.getPeerID()))
                    && (queryRouteMode == null || queryRouteMode.equals(siteConnection.getRouteMode()))
                    && (queryMtu == null || queryMtu.equals(siteConnection.getMtu()))
                    && (queryAuthMode == null || queryAuthMode.equals(siteConnection.getAuthMode()))
                    && (queryPsk == null || queryPsk.equals(siteConnection.getPreSharedKey()))
                    && (queryInitiator == null || queryInitiator.equals(siteConnection.getInitiator()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(siteConnection.getAdminStateUp()))
                    && (queryStatus == null || queryStatus.equals(siteConnection.getStatus()))
                    && (queryIkePolicyID == null || queryIkePolicyID.equals(siteConnection.getIkePolicyID()))
                    && (queryIpSecPolicyID == null || queryIpSecPolicyID.equals(siteConnection.getIpsecPolicyID()))
                    && (queryVpnServiceID == null || queryVpnServiceID.equals(siteConnection.getVpnServiceID()))) {
                if (fields.size() > 0) {
                    ans.add(siteConnection.extractFields(fields));
                } else {
                    ans.add(siteConnection);
                }
            }
        }

        // TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVpnIpSecSiteConnectionRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IPSEC SiteConnection.
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
    public Response showVpnIPSecSiteConnection(@PathParam("connectionID") String connectionID,
                                               // return fields
                                               @QueryParam("fields") List<String> fields) {
        return show(connectionID, fields);
    }

    /**
     * Creates new VPN IPSEC SiteConnection.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVpnIpSecSiteConnection.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVpnIPSecSiteConnection(final NeutronVpnIpSecSiteConnectionRequest input) {
        return create(input);
    }

    /**
     * Updates a VPN IPSEC SiteConnection.
     */
    @Path("{connectionID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVpnIPSecSiteConnection(@PathParam("connectionID") String connectionID,
                                                 final NeutronVpnIpSecSiteConnectionRequest input) {
        return update(connectionID, input);
    }

    /**
     * Deletes a VPN IPSEC SiteConnection.
     */
    @Path("{connectionID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVpnIPSecSiteConnection(@PathParam("connectionID") String connectionID) {
        return delete(connectionID);
    }

}
