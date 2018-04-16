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
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronSecurityRule;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for Security Rule.<br>
 */
@Singleton
@Path("/security-group-rules")
public final class NeutronSecurityRulesNorthbound
        extends AbstractNeutronNorthbound<NeutronSecurityRule, NeutronSecurityRuleRequest, INeutronSecurityRuleCRUD> {

    private static final String RESOURCE_NAME = "Security Rule";

    @Inject
    public NeutronSecurityRulesNorthbound(@OsgiService INeutronSecurityRuleCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Security Rules.
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
            // OpenStack security rule attributes
            @QueryParam("id") String querySecurityRuleUUID,
            @QueryParam("direction") String querySecurityRuleDirection,
            @QueryParam("protocol") String querySecurityRuleProtocol,
            @QueryParam("port_range_min") Integer querySecurityRulePortMin,
            @QueryParam("port_range_max") Integer querySecurityRulePortMax,
            @QueryParam("ethertype") String querySecurityRuleEthertype,
            @QueryParam("remote_ip_prefix") String querySecurityRuleIpPrefix,
            @QueryParam("remote_group_id") String querySecurityRemoteGroupID,
            @QueryParam("security_group_id") String querySecurityRuleGroupID,
            @QueryParam("tenant_id") String querySecurityRuleTenantID,
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronCRUD();
        List<NeutronSecurityRule> allSecurityRules = securityRuleInterface.getAll();
        List<NeutronSecurityRule> ans = new ArrayList<>();
        for (NeutronSecurityRule nsr : allSecurityRules) {
            if ((querySecurityRuleUUID == null || querySecurityRuleUUID.equals(nsr.getID()))
                    && (querySecurityRuleDirection == null
                            || querySecurityRuleDirection.equals(nsr.getSecurityRuleDirection()))
                    && (querySecurityRuleProtocol == null
                            || querySecurityRuleProtocol.equals(nsr.getSecurityRuleProtocol()))
                    && (querySecurityRulePortMin == null
                            || querySecurityRulePortMin.equals(nsr.getSecurityRulePortMin()))
                    && (querySecurityRulePortMax == null
                            || querySecurityRulePortMax.equals(nsr.getSecurityRulePortMax()))
                    && (querySecurityRuleEthertype == null
                            || querySecurityRuleEthertype.equals(nsr.getSecurityRuleEthertype()))
                    && (querySecurityRuleIpPrefix == null
                            || querySecurityRuleIpPrefix.equals(nsr.getSecurityRuleRemoteIpPrefix()))
                    && (querySecurityRuleGroupID == null
                            || querySecurityRuleGroupID.equals(nsr.getSecurityRuleGroupID()))
                    && (querySecurityRemoteGroupID == null
                            || querySecurityRemoteGroupID.equals(nsr.getSecurityRemoteGroupID()))
                    && (querySecurityRuleTenantID == null || querySecurityRuleTenantID.equals(nsr.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(nsr.extractFields(fields));
                } else {
                    ans.add(nsr);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityRuleRequest(ans)).build();
    }

    /**
     * Returns a specific Security Rule.
     */
    @Path("{securityRuleUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSecurityRule(@PathParam("securityRuleUUID") String securityRuleUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(securityRuleUUID, fields);
    }

    /**
     * Creates new Security Rule.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSecurityRules(final NeutronSecurityRuleRequest input) {
        return create(input);
    }

    /**
     * Updates a Security Rule.
     */
    @Path("{securityRuleUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSecurityRule(@PathParam("securityRuleUUID") String securityRuleUUID,
            final NeutronSecurityRuleRequest input) {
        return update(securityRuleUUID, input);
    }

    /**
     * Deletes a Security Rule.
     */
    @Path("{securityRuleUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSecurityRule(@PathParam("securityRuleUUID") String securityRuleUUID) {
        return delete(securityRuleUUID);
    }
}
