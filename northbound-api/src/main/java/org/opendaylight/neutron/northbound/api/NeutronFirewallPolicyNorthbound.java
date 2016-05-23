/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.net.HttpURLConnection;

import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;

/**
 * Neutron Northbound REST APIs for Firewall Policies.<br>
 * This class provides REST APIs for managing neutron Firewall Policies
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
@Path("/fw/firewall_policies")
public class NeutronFirewallPolicyNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronFirewallPolicy, NeutronFirewallPolicyRequest, INeutronFirewallPolicyCRUD, INeutronFirewallPolicyAware> {

    private static final String RESOURCE_NAME = "Firewall Policy";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronFirewallPolicy extractFields(NeutronFirewallPolicy o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronFirewallPolicyRequest newNeutronRequest(NeutronFirewallPolicy o) {
        return new NeutronFirewallPolicyRequest(o);
    }

    @Override
    protected INeutronFirewallPolicyCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronFirewallPolicyCRUD(this);
        if (answer.getFirewallPolicyInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getFirewallPolicyInterface();
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronFirewallPolicyAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronFirewallPolicy singleton) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        return service.canCreateNeutronFirewallPolicy(singleton);
    }

    @Override
    protected void created(Object instance, NeutronFirewallPolicy singleton) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        service.neutronFirewallPolicyCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronFirewallPolicy delta, NeutronFirewallPolicy original) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        return service.canUpdateNeutronFirewallPolicy(delta, original);
    }

    @Override
    protected void updated(Object instance, NeutronFirewallPolicy updated) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        service.neutronFirewallPolicyUpdated(updated);
    }

    @Override
    protected int canDelete(Object instance, NeutronFirewallPolicy singleton) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        return service.canDeleteNeutronFirewallPolicy(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronFirewallPolicy singleton) {
        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
        service.neutronFirewallPolicyDeleted(singleton);
    }

    /**
     * Returns a list of all Firewall Policies */
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
            // OpenStack Firewall Policy attributes
            @QueryParam("id") String queryFirewallPolicyUUID,
            @QueryParam("tenant_id") String queryFirewallPolicyTenantID,
            @QueryParam("name") String queryFirewallPolicyName,
            @QueryParam("shared") Boolean querySecurityPolicyIsShared,
            @QueryParam("audited") Boolean querySecurityPolicyIsAudited,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
            // sorting not supported
    ) {
        INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronCRUD();
        List<NeutronFirewallPolicy> ans = new ArrayList<NeutronFirewallPolicy>();
        for (NeutronFirewallPolicy nsg : firewallPolicyInterface.getAllNeutronFirewallPolicies()) {
            if ((queryFirewallPolicyUUID == null ||
                queryFirewallPolicyUUID.equals(nsg.getID())) &&
                (queryFirewallPolicyTenantID == null ||
                    queryFirewallPolicyTenantID.equals(nsg.getTenantID())) &&
                (queryFirewallPolicyName == null ||
                    queryFirewallPolicyName.equals(nsg.getFirewallPolicyName())) &&
                (querySecurityPolicyIsShared == null ||
                    querySecurityPolicyIsShared.equals(nsg.getFirewallPolicyIsShared())) &&
                (querySecurityPolicyIsAudited == null ||
                    querySecurityPolicyIsAudited.equals(nsg.getFirewallPolicyIsAudited()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg,fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronFirewallPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific Firewall Policy */

    @Path("{firewallPolicyUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFirewallPolicy(@PathParam("firewallPolicyUUID") String firewallPolicyUUID,
                                      // return fields
                                      @QueryParam("fields") List<String> fields) {
        return show(firewallPolicyUUID, fields);
    }

    /**
     * Creates new Firewall Policy
     * */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFirewallPolicies(final NeutronFirewallPolicyRequest input) {
        return create(input);
    }

    /**
     * Updates a Firewall Policy
     */
    @Path("{firewallPolicyUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFirewallPolicy(
            @PathParam("firewallPolicyUUID") String firewallPolicyUUID, final NeutronFirewallPolicyRequest input) {
        return update(firewallPolicyUUID, input);
    }

    /**
     * Deletes a Firewall Policy */

    @Path("{firewallPolicyUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFirewallPolicy(
            @PathParam("firewallPolicyUUID") String firewallPolicyUUID) {
        return delete(firewallPolicyUUID);
    }
}
