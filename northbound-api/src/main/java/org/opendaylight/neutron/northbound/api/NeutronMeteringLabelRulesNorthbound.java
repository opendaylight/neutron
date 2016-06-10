/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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

import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronMeteringLabelRule;

/**
 * Neutron Northbound REST APIs for Metering Lable Rules.<br>
 * This class provides REST APIs for managing neutron metering label rules
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

@Path("/metering/metering-label-rules")
public class NeutronMeteringLabelRulesNorthbound
    extends AbstractNeutronNorthbound<NeutronMeteringLabelRule, NeutronMeteringLabelRuleRequest, INeutronMeteringLabelRuleCRUD> {
    private static final String RESOURCE_NAME = "Metering Label Rule";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronMeteringLabelRule extractFields(NeutronMeteringLabelRule o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected INeutronMeteringLabelRuleCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronMeteringLabelRuleCRUD(this);
        if (answer.getMeteringLabelRuleInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getMeteringLabelRuleInterface();
    }

    @Override
    protected NeutronMeteringLabelRuleRequest newNeutronRequest(NeutronMeteringLabelRule o) {
        return new NeutronMeteringLabelRuleRequest(o);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering label rules */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listMeteringLabelRules(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("direction") String queryDirection,
            @QueryParam("remote_ip_prefix") String queryRemoteIPPrefix,
            @QueryParam("metering_label_id") String queryLabelID
            // pagination and sorting are TODO
            ) {
        INeutronMeteringLabelRuleCRUD ruleInterface = getNeutronCRUD();
        List<NeutronMeteringLabelRule> allNeutronMeteringLabelRule = ruleInterface.getAll();
        List<NeutronMeteringLabelRule> ans = new ArrayList<NeutronMeteringLabelRule>();
        Iterator<NeutronMeteringLabelRule> i = allNeutronMeteringLabelRule.iterator();
        while (i.hasNext()) {
            NeutronMeteringLabelRule oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryDirection == null || queryDirection.equals(oSS.getMeteringLabelRuleDirection())) &&
                    (queryRemoteIPPrefix == null || queryRemoteIPPrefix.equals(oSS.getMeteringLabelRuleRemoteIPPrefix())) &&
                    (queryLabelID == null || queryLabelID.equals(oSS.getMeteringLabelRuleLabelID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS,fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronMeteringLabelRuleRequest(ans)).build();
    }

    /**
     * Returns a specific metering label rule */

    @Path("{ruleUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(ruleUUID, fields);
    }

    /**
     * Creates new metering label rule */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createMeteringLabelRule(final NeutronMeteringLabelRuleRequest input) {
        INeutronMeteringLabelRuleCRUD meteringLabelRuleInterface = getNeutronCRUD();
        if (input.isSingleton()) {
            NeutronMeteringLabelRule singleton = input.getSingleton();

            /*
             * add meteringLabelRule to the cache
             */
            meteringLabelRuleInterface.addNeutronMeteringLabelRule(singleton);
        } else {

            /*
             * only singleton meteringLabelRule creates supported
             */
            throw new BadRequestException("Only singleton meteringLabelRule creates supported");
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Deletes a Metering Label rule */

    @Path("{ruleUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID) {
        return delete(ruleUUID);
    }
}
