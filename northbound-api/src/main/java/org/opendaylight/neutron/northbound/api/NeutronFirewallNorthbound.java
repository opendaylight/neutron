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
public class NeutronFirewallNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronFirewall, NeutronFirewallRequest, INeutronFirewallCRUD, INeutronFirewallAware> {

    private static final String RESOURCE_NAME = "Firewall";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronFirewall extractFields(NeutronFirewall o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronFirewallRequest newNeutronRequest(NeutronFirewall o) {
        return new NeutronFirewallRequest(o);
    }

    @Override
    protected INeutronFirewallCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronFirewallCRUD(this);
        if (answer.getFirewallInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getFirewallInterface();
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronFirewallAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronFirewall singleton) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        return service.canCreateNeutronFirewall(singleton);
    }

    @Override
    protected void created(Object instance, NeutronFirewall singleton) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        service.neutronFirewallCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronFirewall delta, NeutronFirewall original) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        return service.canUpdateNeutronFirewall(delta, original);
    }

    @Override
    protected void updated(Object instance, NeutronFirewall updated) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        service.neutronFirewallUpdated(updated);
    }

    @Override
    protected int canDelete(Object instance, NeutronFirewall singleton) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        return service.canDeleteNeutronFirewall(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronFirewall singleton) {
        INeutronFirewallAware service = (INeutronFirewallAware) instance;
        service.neutronFirewallDeleted(singleton);
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
            @QueryParam("admin_state_up") Boolean queryFirewallAdminStateIsUp,
            @QueryParam("shared") Boolean queryFirewallIsShared,
            @QueryParam("firewall_policy_id") String queryFirewallPolicyID,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
            // sorting not supported
    ) {
        INeutronFirewallCRUD firewallInterface = getNeutronCRUD();
        List<NeutronFirewall> ans = new ArrayList<NeutronFirewall>();
        for (NeutronFirewall nsg : firewallInterface.getAllNeutronFirewalls()) {
            if ((queryFirewallUUID == null ||
                queryFirewallUUID.equals(nsg.getID())) &&
                (queryFirewallTenantID == null ||
                    queryFirewallTenantID.equals(nsg.getTenantID())) &&
                (queryFirewallName == null ||
                    queryFirewallName.equals(nsg.getFirewallName())) &&
                (queryFirewallAdminStateIsUp == null ||
                    queryFirewallAdminStateIsUp.equals(nsg.getFirewallAdminStateIsUp())) &&
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
        return show(firewallUUID, fields);
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
        return create(input);
    }

    /**
     * Updates a Firewall */

    @Path("{firewallUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFirewall(
            @PathParam("firewallUUID") String firewallUUID, final NeutronFirewallRequest input) {
        return update(firewallUUID, input);
    }

    /**
     * Deletes a Firewall */

    @Path("{firewallUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFirewall(
            @PathParam("firewallUUID") String firewallUUID) {
        return delete(firewallUUID);
    }
}
