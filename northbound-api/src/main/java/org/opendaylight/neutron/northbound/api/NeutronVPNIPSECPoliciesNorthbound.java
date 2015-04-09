/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyAware;
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;

/**
 * Neutron Northbound REST APIs for VPN IPSEC Policy.<br>
 * This class provides REST APIs for managing neutron VPN IPSEC Policies
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

@Path("/vpn/ipsecpolicies")
public class NeutronVPNIPSECPoliciesNorthbound {

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN IPSEC Policies */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response listVPNIPSECPolicies(
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("description") String queryDescription,
            @QueryParam("auth_algorithm") String queryAuthAlgorithm,
            @QueryParam("encryption_algorithm") String queryEncryptionAlgorithm,
            @QueryParam("phase1_negotiation_mode") String queryPhase1NegotiationMode,
            @QueryParam("pfs") String queryPFS,
            @QueryParam("ike_version") String queryIKEVersion
            // pagination and sorting are TODO
            ) {
        INeutronVPNIPSECPolicyCRUD labelInterface = NeutronCRUDInterfaces.getINeutronVPNIPSECPolicyCRUD(this);
        if (labelInterface == null) {
            throw new ServiceUnavailableException("NeutronVPNIPSECPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronVPNIPSECPolicy> allNeutronVPNIPSECPolicies = labelInterface.getAllNeutronVPNIPSECPolicies();
        List<NeutronVPNIPSECPolicy> ans = new ArrayList<NeutronVPNIPSECPolicy>();
        Iterator<NeutronVPNIPSECPolicy> i = allNeutronVPNIPSECPolicies.iterator();
        while (i.hasNext()) {
            NeutronVPNIPSECPolicy oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getName())) &&
                    (queryDescription == null || queryDescription.equals(oSS.getDescription())) &&
                    (queryAuthAlgorithm == null || queryAuthAlgorithm.equals(oSS.getAuthAlgorithm())) &&
                    (queryEncryptionAlgorithm == null || queryEncryptionAlgorithm.equals(oSS.getEncryptionAlgorithm())) &&
                    (queryPhase1NegotiationMode == null || queryPhase1NegotiationMode.equals(oSS.getPhase1NegotiationMode())) &&
                    (queryPFS == null || queryPFS.equals(oSS.getPerfectForwardSecrecy())) &&
                    (queryIPSECVersion == null || queryIPSECVersion.equals(oSS.getIkeVersion())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0)
                    ans.add(extractFields(oSS,fields));
                else
                    ans.add(oSS);
            }
        }
        //TODO: apply pagination to results
        return Response.status(200).entity(
                new NeutronVPNIPSECPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IPSEC Policy */

    @Path("{policyUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response showVPNIPSECPolicy(
            @PathParam("policyUUID") String policyUUID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        INeutronVPNIPSECPolicyCRUD policyInterface = NeutronCRUDInterfaces.getINeutronVPNIPSECPolicyCRUD(this);
        if (policyInterface == null) {
            throw new ServiceUnavailableException("VPNIPSECPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!policyInterface.neutronVPNIPSECPolicyExists(policyUUID)) {
            throw new ResourceNotFoundException("VPNIPSECPolicy UUID not found");
        }
        if (fields.size() > 0) {
            NeutronVPNIPSECPolicy ans = policyInterface.getNeutronVPNIPSECPolicy(policyUUID);
            return Response.status(200).entity(
                    new NeutronVPNIPSECPolicyRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(200).entity(
                    new NeutronVPNIPSECPolicyRequest(policyInterface.getNeutronVPNIPSECPolicy(policyUUID))).build();
        }
    }

    /**
     * Creates new VPN IPSEC Policy */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNIPSECPolicy.class)
    @StatusCodes({
            @ResponseCode(code = 201, condition = "Created"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response createVPNIPSECPolicy(final NeutronVPNIPSECPolicyRequest input) {
        INeutronVPNIPSECPolicyCRUD ipsecPolicyInterface = NeutronCRUDInterfaces.getINeutronVPNIPSECPolicyCRUD(this);
        if (ipsecPolicyInterface == null) {
            throw new ServiceUnavailableException("VPNIPSECPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronVPNIPSECPolicy singleton = input.getSingleton();

            /*
             * verify that the ipsecPolicy doesn't already exist (issue: is deeper inspection necessary?)
             */
            if (ipsecPolicyInterface.neutronVPNIPSECPolicyExists(singleton.getID()))
                throw new BadRequestException("ipsecPolicy UUID already exists");
            Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECPolicyAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                        int status = service.canCreateNeutronVPNIPSECPolicy(singleton);
                        if (status < 200 || status > 299)
                            return Response.status(status).build();
                    }
                } else {
                    throw new ServiceUnavailableException("No providers registered.  Please try again later");
                }
            } else {
                throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
            }

            /*
             * add ipsecPolicy to the cache
             */
            ipsecPolicyInterface.addNeutronVPNIPSECPolicy(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                    service.neutronVPNIPSECPolicyCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton ipsecPolicy creates supported
             */
            throw new BadRequestException("Only singleton ipsecPolicy creates supported");
        }
        return Response.status(201).entity(input).build();
    }

    /**
     * Updates a VPN IPSEC Policy */
    @Path("{policyID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response updateVPNIPSECPolicy(
            @PathParam("policyID") String policyUUID, final NeutronVPNIPSECPolicyRequest input
            ) {
        INeutronVPNIPSECPolicyCRUD ipsecPolicyInterface = NeutronCRUDInterfaces.getINeutronVPNIPSECPolicyCRUD(this);
        if (ipsecPolicyInterface == null) {
            throw new ServiceUnavailableException("VPNIPSECPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * ipsecPolicy has to exist and only a single delta can be supplied
         */
        if (!ipsecPolicyInterface.neutronVPNIPSECPolicyExists(policyUUID))
            throw new ResourceNotFoundException("VPNIPSECPolicy UUID not found");
        if (!input.isSingleton())
            throw new BadRequestException("Only single ipsecPolicy deltas supported");
        NeutronVPNIPSECPolicy singleton = input.getSingleton();
        NeutronVPNIPSECPolicy original = ipsecPolicyInterface.getNeutronVPNIPSECPolicy(policyUUID);

        /*
         * attribute changes blocked by Neutron
         */
        if (singleton.getID() != null || singleton.getTenantID() != null)
            throw new BadRequestException("Request attribute change not allowed");

        Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECPolicyAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                    int status = service.canUpdateNeutronVPNIPSECPolicy(singleton, original);
                    if (status < 200 || status > 299)
                        return Response.status(status).build();
                }
            } else {
                throw new ServiceUnavailableException("No providers registered.  Please try again later");
            }
        } else {
            throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
        }
        /*
         * update the ipsecPolicy entry and return the modified object
         */
        ipsecPolicyInterface.updateNeutronVPNIPSECPolicy(policyUUID, singleton);
        NeutronVPNIPSECPolicy updatedVPNIPSECPolicy = ipsecPolicyInterface.getNeutronVPNIPSECPolicy(policyUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                service.neutronVPNIPSECPolicyUpdated(updatedVPNIPSECPolicy);
            }
        }
        return Response.status(200).entity(
                new NeutronVPNIPSECPolicyRequest(ipsecPolicyInterface.getNeutronVPNIPSECPolicy(policyUUID))).build();
    }

    /**
     * Deletes a VPN IPSEC Policy */

    @Path("{policyID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = 204, condition = "No Content"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response deleteVPNIPSECPolicy(
            @PathParam("policyID") String policyUUID) {
        throw new UnimplementedException("Not Implemented");
    }
}
