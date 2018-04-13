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
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.NeutronFirewallRule;

/**
 * Neutron Northbound REST APIs for Firewall Rule.
 */
@Path("fw/firewall_rules")
public final class NeutronFirewallRulesNorthbound
        extends AbstractNeutronNorthbound<NeutronFirewallRule, NeutronFirewallRuleRequest, INeutronFirewallRuleCRUD> {

    private static final String RESOURCE_NAME = "Firewall Rule";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Firewall Rules.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listRules(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack firewall rule attributes
            @QueryParam("id") String queryFirewallRuleUUID,
            @QueryParam("tenant_id") String queryFirewallRuleTenantID,
            @QueryParam("name") String queryFirewallRuleName,
            @QueryParam("shared") Boolean queryFirewallRuleIsShared,
            @QueryParam("firewall_policy_id") String queryFirewallRulePolicyID,
            @QueryParam("protocol") String queryFirewallRuleProtocol,
            @QueryParam("ip_version") Integer queryFirewallRuleIpVer,
            @QueryParam("source_ip_address") String queryFirewallRuleSrcIpAddr,
            @QueryParam("destination_ip_address") String queryFirewallRuleDstIpAddr,
            @QueryParam("source_port_range_min") Integer queryFirewallRuleSrcPortRangeMin,
            @QueryParam("source_port_range_max") Integer queryFirewallRuleSrcPortRangeMax,
            @QueryParam("destination_port_range_min") Integer queryFirewallRuleDstPortRangeMin,
            @QueryParam("destination_port_range_max") Integer queryFirewallRuleDstPortRangeMax,
            @QueryParam("position") Integer queryFirewallRulePosition,
            @QueryParam("action") String queryFirewallRuleAction,
            @QueryParam("enabled") Boolean queryFirewallRuleIsEnabled,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronFirewallRuleCRUD firewallRuleInterface = getNeutronCRUD();
        List<NeutronFirewallRule> ans = new ArrayList<>();
        for (NeutronFirewallRule nsr : firewallRuleInterface.getAll()) {
            if ((queryFirewallRuleUUID == null || queryFirewallRuleUUID.equals(nsr.getID()))
                    && (queryFirewallRuleTenantID == null || queryFirewallRuleTenantID.equals(nsr.getTenantID()))
                    && (queryFirewallRuleName == null || queryFirewallRuleName.equals(nsr.getName()))
                    && (queryFirewallRuleIsShared == null
                            || queryFirewallRuleIsShared.equals(nsr.getFirewallRuleIsShared()))
                    && (queryFirewallRulePolicyID == null
                            || queryFirewallRulePolicyID.equals(nsr.getFirewallRulePolicyID()))
                    && (queryFirewallRuleProtocol == null
                            || queryFirewallRuleProtocol.equals(nsr.getFirewallRuleProtocol()))
                    && (queryFirewallRuleIpVer == null || queryFirewallRuleIpVer.equals(nsr.getFirewallRuleIpVer()))
                    && (queryFirewallRuleSrcIpAddr == null
                            || queryFirewallRuleSrcIpAddr.equals(nsr.getFirewallRuleSrcIpAddr()))
                    && (queryFirewallRuleDstIpAddr == null
                            || queryFirewallRuleDstIpAddr.equals(nsr.getFirewallRuleDstIpAddr()))
                    && (queryFirewallRuleSrcPortRangeMin == null
                            || queryFirewallRuleSrcPortRangeMin.equals(nsr.getFirewallRuleSrcPortRangeMin()))
                    && (queryFirewallRuleSrcPortRangeMax == null
                            || queryFirewallRuleSrcPortRangeMax.equals(nsr.getFirewallRuleSrcPortRangeMax()))
                    && (queryFirewallRuleDstPortRangeMin == null
                            || queryFirewallRuleDstPortRangeMin.equals(nsr.getFirewallRuleDstPortRangeMin()))
                    && (queryFirewallRuleDstPortRangeMax == null
                            || queryFirewallRuleDstPortRangeMax.equals(nsr.getFirewallRuleDstPortRangeMax()))
                    && (queryFirewallRulePosition == null
                            || queryFirewallRulePosition.equals(nsr.getFirewallRulePosition()))
                    && (queryFirewallRuleAction == null || queryFirewallRuleAction.equals(nsr.getFirewallRuleAction()))
                    && (queryFirewallRuleIsEnabled == null
                            || queryFirewallRuleIsEnabled.equals(nsr.getFirewallRuleIsEnabled()))) {
                if (fields.size() > 0) {
                    ans.add(nsr.extractFields(fields));
                } else {
                    ans.add(nsr);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFirewallRuleRequest(ans)).build();
    }

    /**
     * Returns a specific Firewall Rule.
     */
    @Path("{firewallRuleUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFirewallRule(@PathParam("firewallRuleUUID") String firewallRuleUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(firewallRuleUUID, fields);
    }

    /**
     * Creates new Firewall Rule.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFirewallRules(final NeutronFirewallRuleRequest input) {
        return create(input);
    }

    /**
     * Updates a Firewall Rule.
     */
    @Path("{firewallRuleUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFirewallRule(@PathParam("firewallRuleUUID") String firewallRuleUUID,
            final NeutronFirewallRuleRequest input) {
        return update(firewallRuleUUID, input);
    }

    /**
     * Deletes a Firewall Rule.
     */
    @Path("{firewallRuleUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFirewallRule(@PathParam("firewallRuleUUID") String firewallRuleUUID) {
        return delete(firewallRuleUUID);
    }
}
