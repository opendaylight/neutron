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
public class NeutronVPNIPSECSiteConnectionsNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronVPNIPSECSiteConnection, NeutronVPNIPSECSiteConnectionRequest, INeutronVPNIPSECSiteConnectionsCRUD, INeutronVPNIPSECSiteConnectionAware> {

    private static final String RESOURCE_NAME = "VPNIPSECSiteConnections";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronVPNIPSECSiteConnection extractFields(NeutronVPNIPSECSiteConnection o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronVPNIPSECSiteConnectionRequest newNeutronRequest(NeutronVPNIPSECSiteConnection o) {
        return new NeutronVPNIPSECSiteConnectionRequest(o);
    }

    @Override
    protected INeutronVPNIPSECSiteConnectionsCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronVPNIPSECSiteConnectionsCRUD(this);
        if (answer.getVPNIPSECSiteConnectionsInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getVPNIPSECSiteConnectionsInterface();
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronVPNIPSECSiteConnectionAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronVPNIPSECSiteConnection singleton) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        return service.canCreateNeutronVPNIPSECSiteConnection(singleton);
    }

    @Override
    protected void created(Object instance, NeutronVPNIPSECSiteConnection singleton) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        service.neutronVPNIPSECSiteConnectionCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronVPNIPSECSiteConnection delta, NeutronVPNIPSECSiteConnection original) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        return service.canUpdateNeutronVPNIPSECSiteConnection(delta, original);
    }

    @Override
    protected void updated(Object instance, NeutronVPNIPSECSiteConnection updated) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        service.neutronVPNIPSECSiteConnectionUpdated(updated);
    }

    @Override
    protected int canDelete(Object instance, NeutronVPNIPSECSiteConnection singleton) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        return service.canDeleteNeutronVPNIPSECSiteConnection(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronVPNIPSECSiteConnection singleton) {
        INeutronVPNIPSECSiteConnectionAware service = (INeutronVPNIPSECSiteConnectionAware) instance;
        service.neutronVPNIPSECSiteConnectionDeleted(singleton);
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
            @QueryParam("name") String queryName,
            @QueryParam("peer_address") String queryPeerAddress, @QueryParam("peer_id") String queryPeerID,
            @QueryParam("route_mode") String queryRouteMode, @QueryParam("mtu") Integer queryMtu,
            @QueryParam("auth_mode") String queryAuthMode, @QueryParam("psk") String queryPsk,
            @QueryParam("initiator") String queryInitiator, @QueryParam("admin_state_up") Boolean queryAdminStateUp,
            @QueryParam("status") String queryStatus, @QueryParam("ikepolicy_id") String queryIkePolicyID,
            @QueryParam("ipsecpolicy_id") String queryIpSecPolicyID,
            @QueryParam("vpnservice_id") String queryVpnServiceID
    // pagination and sorting are TODO
    ) {
        INeutronVPNIPSECSiteConnectionsCRUD labelInterface = getNeutronCRUD();
        List<NeutronVPNIPSECSiteConnection> allNeutronVPNIPSECSiteConnection = labelInterface
                .getAllNeutronVPNIPSECSiteConnections();
        List<NeutronVPNIPSECSiteConnection> ans = new ArrayList<NeutronVPNIPSECSiteConnection>();
        Iterator<NeutronVPNIPSECSiteConnection> i = allNeutronVPNIPSECSiteConnection.iterator();
        while (i.hasNext()) {
            NeutronVPNIPSECSiteConnection oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID()))
                    && (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))
                    && (queryName == null || queryName.equals(oSS.getName()))
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
        return show(connectionID, fields);
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
        return create(input);
    }

    /**
     * Updates a VPN IPSEC SiteConnection
     */
    @Path("{connectionID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNIPSECSiteConnection(@PathParam("connectionID") String connectionID,
            final NeutronVPNIPSECSiteConnectionRequest input) {
        return update(connectionID, input);
    }

    /**
     * Deletes a VPN IPSEC SiteConnection
     */

    @Path("{connectionID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVPNIPSECSiteConnection(@PathParam("connectionID") String connectionID) {
        return delete(connectionID);
    }

}
