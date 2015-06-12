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

import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleAware;
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
public class NeutronMeteringLabelRulesNorthbound {

    private NeutronMeteringLabelRule extractFields(NeutronMeteringLabelRule o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering label rules */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
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
        INeutronMeteringLabelRuleCRUD ruleInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelRuleCRUD(this);
        if (ruleInterface == null) {
            throw new ServiceUnavailableException("NeutronMeteringLabelRule CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronMeteringLabelRule> allNeutronMeteringLabelRules = ruleInterface.getAllNeutronMeteringLabelRules();
        List<NeutronMeteringLabelRule> ans = new ArrayList<NeutronMeteringLabelRule>();
        Iterator<NeutronMeteringLabelRule> i = allNeutronMeteringLabelRules.iterator();
        while (i.hasNext()) {
            NeutronMeteringLabelRule oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getMeteringLabelRuleUUID())) &&
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
        return Response.status(200).entity(
                new NeutronMeteringLabelRuleRequest(ans)).build();
    }

    /**
     * Returns a specific metering label rule */

    @Path("{ruleUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response showMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronMeteringLabelRuleCRUD ruleInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelRuleCRUD(this);
        if (ruleInterface == null) {
            throw new ServiceUnavailableException("MeteringLabelRule CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!ruleInterface.neutronMeteringLabelRuleExists(ruleUUID)) {
            throw new ResourceNotFoundException("MeteringLabelRule UUID not found");
        }
        if (fields.size() > 0) {
            NeutronMeteringLabelRule ans = ruleInterface.getNeutronMeteringLabelRule(ruleUUID);
            return Response.status(200).entity(
                    new NeutronMeteringLabelRuleRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(200).entity(
                    new NeutronMeteringLabelRuleRequest(ruleInterface.getNeutronMeteringLabelRule(ruleUUID))).build();
        }
    }

    /**
     * Creates new metering label rule */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({
            @ResponseCode(code = 201, condition = "Created"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response createMeteringLabelRule(final NeutronMeteringLabelRuleRequest input) {
        INeutronMeteringLabelRuleCRUD meteringLabelRuleInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelRuleCRUD(this);
        if (meteringLabelRuleInterface == null) {
            throw new ServiceUnavailableException("MeteringLabelRule CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronMeteringLabelRule singleton = input.getSingleton();

            /*
             * verify that the meteringLabelRule doesn't already exist (issue: is deeper inspection necessary?)
             */
            if (meteringLabelRuleInterface.neutronMeteringLabelRuleExists(singleton.getMeteringLabelRuleUUID())) {
                throw new BadRequestException("meteringLabelRule UUID already exists");
            }
            Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelRuleAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronMeteringLabelRuleAware service = (INeutronMeteringLabelRuleAware) instance;
                        int status = service.canCreateMeteringLabelRule(singleton);
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
             * add meteringLabelRule to the cache
             */
            meteringLabelRuleInterface.addNeutronMeteringLabelRule(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronMeteringLabelRuleAware service = (INeutronMeteringLabelRuleAware) instance;
                    service.neutronMeteringLabelRuleCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton meteringLabelRule creates supported
             */
            throw new BadRequestException("Only singleton meteringLabelRule creates supported");
        }
        return Response.status(201).entity(input).build();
    }

    /**
     * Deletes a Metering Label rule */

    @Path("{ruleUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = 204, condition = "No Content"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response deleteMeteringLabelRule(
            @PathParam("ruleUUID") String ruleUUID) {
        INeutronMeteringLabelRuleCRUD meteringLabelRuleInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelRuleCRUD(this);
        if (meteringLabelRuleInterface == null) {
            throw new ServiceUnavailableException("MeteringLabelRule CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * verify that the meteringLabelRule exists and is not in use before removing it
         */
        if (!meteringLabelRuleInterface.neutronMeteringLabelRuleExists(ruleUUID)) {
            throw new ResourceNotFoundException("MeteringLabelRule UUID not found");
        }
        NeutronMeteringLabelRule singleton = meteringLabelRuleInterface.getNeutronMeteringLabelRule(ruleUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelRuleAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronMeteringLabelRuleAware service = (INeutronMeteringLabelRuleAware) instance;
                    int status = service.canDeleteMeteringLabelRule(singleton);
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
        meteringLabelRuleInterface.removeNeutronMeteringLabelRule(ruleUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronMeteringLabelRuleAware service = (INeutronMeteringLabelRuleAware) instance;
                service.neutronMeteringLabelRuleDeleted(singleton);
            }
        }
        return Response.status(204).build();
    }
}
