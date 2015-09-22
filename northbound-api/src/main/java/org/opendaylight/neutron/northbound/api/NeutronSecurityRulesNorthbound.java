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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronSecurityRuleAware;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSecurityRule;

/**
 * Neutron Northbound REST APIs for Security Rule.<br>
 * This class provides REST APIs for managing neutron Security Rule
 * <p>
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
 */

@Path ("/security-group-rules")
public class NeutronSecurityRulesNorthbound {
    private static final int HTTP_OK_BOTTOM = 200;
    private static final int HTTP_OK_TOP = 299;
    private static final String INTERFACE_NAME = "Security Rule CRUD Interface";
    private static final String UUID_NO_EXIST = "Security Rule UUID does not exist.";


    private NeutronSecurityRule extractFields(NeutronSecurityRule o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSecurityRuleCRUD(this);
        if (answer.getSecurityRuleInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    /**
     * Returns a list of all Security Rules
     */
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listRules(
            // return fields
            @QueryParam ("fields") List<String> fields,
            // OpenStack security rule attributes
            @QueryParam ("id") String querySecurityRuleUUID,
            @QueryParam ("direction") String querySecurityRuleDirection,
            @QueryParam ("protocol") String querySecurityRuleProtocol,
            @QueryParam ("port_range_min") Integer querySecurityRulePortMin,
            @QueryParam ("port_range_max") Integer querySecurityRulePortMax,
            @QueryParam ("ethertype") String querySecurityRuleEthertype,
            @QueryParam ("remote_ip_prefix") String querySecurityRuleIpPrefix,
            @QueryParam ("remote_group_id") String querySecurityRemoteGroupID,
            @QueryParam ("security_group_id") String querySecurityRuleGroupID,
            @QueryParam ("tenant_id") String querySecurityRuleTenantID,
            @QueryParam ("limit") String limit,
            @QueryParam ("marker") String marker,
            @QueryParam ("page_reverse") String pageReverse
    ) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronInterfaces().getSecurityRuleInterface();
        List<NeutronSecurityRule> allSecurityRules = securityRuleInterface.getAllNeutronSecurityRules();
        List<NeutronSecurityRule> ans = new ArrayList<NeutronSecurityRule>();
        Iterator<NeutronSecurityRule> i = allSecurityRules.iterator();
        while (i.hasNext()) {
            NeutronSecurityRule nsr = i.next();
            if ((querySecurityRuleUUID == null ||
                    querySecurityRuleUUID.equals(nsr.getID())) &&
                    (querySecurityRuleDirection == null ||
                            querySecurityRuleDirection.equals(nsr.getSecurityRuleDirection())) &&
                    (querySecurityRuleProtocol == null ||
                            querySecurityRuleProtocol.equals(nsr.getSecurityRuleProtocol())) &&
                    (querySecurityRulePortMin == null ||
                            querySecurityRulePortMin.equals(nsr.getSecurityRulePortMin())) &&
                    (querySecurityRulePortMax == null ||
                            querySecurityRulePortMax.equals(nsr.getSecurityRulePortMax())) &&
                    (querySecurityRuleEthertype == null ||
                            querySecurityRuleEthertype.equals(nsr.getSecurityRuleEthertype())) &&
                    (querySecurityRuleIpPrefix == null ||
                            querySecurityRuleIpPrefix.equals(nsr.getSecurityRuleRemoteIpPrefix())) &&
                    (querySecurityRuleGroupID == null ||
                            querySecurityRuleGroupID.equals(nsr.getSecurityRuleGroupID())) &&
                    (querySecurityRemoteGroupID == null ||
                            querySecurityRemoteGroupID.equals(nsr.getSecurityRemoteGroupID())) &&
                    (querySecurityRuleTenantID == null ||
                            querySecurityRuleTenantID.equals(nsr.getSecurityRuleTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsr, fields));
                } else {
                    ans.add(nsr);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronSecurityRuleRequest(ans)).build();
    }

    /**
     * Returns a specific Security Rule
     */

    @Path ("{securityRuleUUID}")
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode (code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSecurityRule(@PathParam ("securityRuleUUID") String securityRuleUUID,
                                     // return fields
                                     @QueryParam ("fields") List<String> fields) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronInterfaces().getSecurityRuleInterface();
        if (!securityRuleInterface.neutronSecurityRuleExists(securityRuleUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (!fields.isEmpty()) {
            NeutronSecurityRule ans = securityRuleInterface.getNeutronSecurityRule(securityRuleUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronSecurityRuleRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityRuleRequest(securityRuleInterface.getNeutronSecurityRule(securityRuleUUID))).build();
        }
    }

    /**
     * Creates new Security Rule
     */

    @POST
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSecurityRules(final NeutronSecurityRuleRequest input) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronInterfaces().getSecurityRuleInterface();

        if (input.isSingleton()) {
            NeutronSecurityRule singleton = input.getSingleton();
            Object[] instances = NeutronUtil.getInstances(INeutronSecurityRuleAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                    int status = service.canCreateNeutronSecurityRule(singleton);
                    if ((status < HTTP_OK_BOTTOM) || (status > HTTP_OK_TOP)) {
                        return Response.status(status).build();
                    }
                }
            }

            // add rule to cache
            singleton.initDefaults();
            securityRuleInterface.addNeutronSecurityRule(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                    service.neutronSecurityRuleCreated(singleton);
                }
            }
        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronSecurityRuleAware.class, this);
            for (NeutronSecurityRule test : input.getBulk()) {
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                        int status = service.canCreateNeutronSecurityRule(test);
                        if ((status < HTTP_OK_BOTTOM) || (status > HTTP_OK_TOP)) {
                            return Response.status(status).build();
                        }
                    }
                }
            }

            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (NeutronSecurityRule test : input.getBulk()) {
                securityRuleInterface.addNeutronSecurityRule(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                        service.neutronSecurityRuleCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Security Rule
     */

    @Path ("{securityRuleUUID}")
    @PUT
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSecurityRule(
            @PathParam ("securityRuleUUID") String securityRuleUUID, final NeutronSecurityRuleRequest input) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronInterfaces().getSecurityRuleInterface();

        NeutronSecurityRule delta = input.getSingleton();
        NeutronSecurityRule original = securityRuleInterface.getNeutronSecurityRule(securityRuleUUID);

        Object[] instances = NeutronUtil.getInstances(INeutronSecurityRuleAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                int status = service.canUpdateNeutronSecurityRule(delta, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        /*
         * update the object and return it
         */
        securityRuleInterface.updateNeutronSecurityRule(securityRuleUUID, delta);
        NeutronSecurityRule updatedSecurityRule = securityRuleInterface.getNeutronSecurityRule(securityRuleUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                service.neutronSecurityRuleUpdated(updatedSecurityRule);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityRuleRequest(securityRuleInterface.getNeutronSecurityRule(securityRuleUUID))).build();
    }

    /**
     * Deletes a Security Rule
     */

    @Path ("{securityRuleUUID}")
    @DELETE
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSecurityRule(
            @PathParam ("securityRuleUUID") String securityRuleUUID) {
        INeutronSecurityRuleCRUD securityRuleInterface = getNeutronInterfaces().getSecurityRuleInterface();

        NeutronSecurityRule singleton = securityRuleInterface.getNeutronSecurityRule(securityRuleUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronSecurityRuleAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                int status = service.canDeleteNeutronSecurityRule(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }


        /*
         * remove it and return 204 status
         */
        securityRuleInterface.removeNeutronSecurityRule(securityRuleUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityRuleAware service = (INeutronSecurityRuleAware) instance;
                service.neutronSecurityRuleDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
