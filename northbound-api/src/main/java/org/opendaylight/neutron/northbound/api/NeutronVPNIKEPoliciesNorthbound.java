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
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyAware;
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronVPNIKEPolicy;

/**
 * Neutron Northbound REST APIs for VPN IKE Policy.<br>
 * This class provides REST APIs for managing neutron VPN IKE Policies
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

@Path("/vpn/ikepolicies")
public class NeutronVPNIKEPoliciesNorthbound {

    private NeutronVPNIKEPolicy extractFields(NeutronVPNIKEPolicy o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN IKE Policies */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response listVPNIKEPolicies(
            // return fields
            @QueryParam("fields") List<String> fields,
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
        INeutronVPNIKEPolicyCRUD labelInterface = NeutronCRUDInterfaces.getINeutronVPNIKEPolicyCRUD(this);
        if (labelInterface == null) {
            throw new ServiceUnavailableException("NeutronVPNIKEPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronVPNIKEPolicy> allNeutronVPNIKEPolicies = labelInterface.getAllNeutronVPNIKEPolicies();
        List<NeutronVPNIKEPolicy> ans = new ArrayList<NeutronVPNIKEPolicy>();
        Iterator<NeutronVPNIKEPolicy> i = allNeutronVPNIKEPolicies.iterator();
        while (i.hasNext()) {
            NeutronVPNIKEPolicy oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getName())) &&
                    (queryDescription == null || queryDescription.equals(oSS.getDescription())) &&
                    (queryAuthAlgorithm == null || queryAuthAlgorithm.equals(oSS.getAuthAlgorithm())) &&
                    (queryEncryptionAlgorithm == null || queryEncryptionAlgorithm.equals(oSS.getEncryptionAlgorithm())) &&
                    (queryPhase1NegotiationMode == null || queryPhase1NegotiationMode.equals(oSS.getPhase1NegotiationMode())) &&
                    (queryPFS == null || queryPFS.equals(oSS.getPerfectForwardSecrecy())) &&
                    (queryIKEVersion == null || queryIKEVersion.equals(oSS.getIkeVersion())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0)
                    ans.add(extractFields(oSS,fields));
                else
                    ans.add(oSS);
            }
        }
        //TODO: apply pagination to results
        return Response.status(200).entity(
                new NeutronVPNIKEPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IKE Policy */

    @Path("{policyID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response showVPNIKEPolicy(
            @PathParam("policyID") String policyUUID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        INeutronVPNIKEPolicyCRUD policyInterface = NeutronCRUDInterfaces.getINeutronVPNIKEPolicyCRUD(this);
        if (policyInterface == null) {
            throw new ServiceUnavailableException("VPNIKEPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!policyInterface.neutronVPNIKEPolicyExists(policyUUID)) {
            throw new ResourceNotFoundException("VPNIKEPolicy UUID not found");
        }
        if (fields.size() > 0) {
            NeutronVPNIKEPolicy ans = policyInterface.getNeutronVPNIKEPolicy(policyUUID);
            return Response.status(200).entity(
                    new NeutronVPNIKEPolicyRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(200).entity(
                    new NeutronVPNIKEPolicyRequest(policyInterface.getNeutronVPNIKEPolicy(policyUUID))).build();
        }
    }

    /**
     * Creates new VPN IKE Policy */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNIKEPolicy.class)
    @StatusCodes({
            @ResponseCode(code = 201, condition = "Created"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response createVPNIKEPolicy(final NeutronVPNIKEPolicyRequest input) {
        INeutronVPNIKEPolicyCRUD ikePolicyInterface = NeutronCRUDInterfaces.getINeutronVPNIKEPolicyCRUD(this);
        if (ikePolicyInterface == null) {
            throw new ServiceUnavailableException("VPNIKEPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronVPNIKEPolicy singleton = input.getSingleton();

            /*
             * verify that the ikePolicy doesn't already exist (issue: is deeper inspection necessary?)
             */
            if (ikePolicyInterface.neutronVPNIKEPolicyExists(singleton.getID()))
                throw new BadRequestException("ikePolicy UUID already exists");
            Object[] instances = NeutronUtil.getInstances(INeutronVPNIKEPolicyAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                        int status = service.canCreateNeutronVPNIKEPolicy(singleton);
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
             * add ikePolicy to the cache
             */
            ikePolicyInterface.addNeutronVPNIKEPolicy(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                    service.neutronVPNIKEPolicyCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton ikePolicy creates supported
             */
            throw new BadRequestException("Only singleton ikePolicy creates supported");
        }
        return Response.status(201).entity(input).build();
    }

    /**
     * Updates a VPN IKE Policy */
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
    public Response updateVPNIKEPolicy(
            @PathParam("policyID") String policyUUID, final NeutronVPNIKEPolicyRequest input
            ) {
        INeutronVPNIKEPolicyCRUD ikePolicyInterface = NeutronCRUDInterfaces.getINeutronVPNIKEPolicyCRUD(this);
        if (ikePolicyInterface == null) {
            throw new ServiceUnavailableException("VPNIKEPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * ikePolicy has to exist and only a single delta can be supplied
         */
        if (!ikePolicyInterface.neutronVPNIKEPolicyExists(policyUUID))
            throw new ResourceNotFoundException("VPNIKEPolicy UUID not found");
        if (!input.isSingleton())
            throw new BadRequestException("Only single ikePolicy deltas supported");
        NeutronVPNIKEPolicy singleton = input.getSingleton();
        NeutronVPNIKEPolicy original = ikePolicyInterface.getNeutronVPNIKEPolicy(policyUUID);

        /*
         * attribute changes blocked by Neutron
         */
        if (singleton.getID() != null || singleton.getTenantID() != null)
            throw new BadRequestException("Request attribute change not allowed");

        Object[] instances = NeutronUtil.getInstances(INeutronVPNIKEPolicyAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                    int status = service.canUpdateNeutronVPNIKEPolicy(singleton, original);
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
         * update the ikePolicy entry and return the modified object
         */
        ikePolicyInterface.updateNeutronVPNIKEPolicy(policyUUID, singleton);
        NeutronVPNIKEPolicy updatedVPNIKEPolicy = ikePolicyInterface.getNeutronVPNIKEPolicy(policyUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                service.neutronVPNIKEPolicyUpdated(updatedVPNIKEPolicy);
            }
        }
        return Response.status(200).entity(
                new NeutronVPNIKEPolicyRequest(ikePolicyInterface.getNeutronVPNIKEPolicy(policyUUID))).build();
    }

    /**
     * Deletes a VPN IKE Policy */

    @Path("{policyID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = 204, condition = "No Content"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response deleteVPNIKEPolicy(
            @PathParam("policyID") String policyUUID) {
        INeutronVPNIKEPolicyCRUD policyInterface = NeutronCRUDInterfaces.getINeutronVPNIKEPolicyCRUD(this);
        if (policyInterface == null) {
            throw new ServiceUnavailableException("VPNIKEPolicy CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * verify that the policy exists and is not in use before removing it
         */
        if (!policyInterface.neutronVPNIKEPolicyExists(policyUUID))
            throw new ResourceNotFoundException("VPNIKEPolicy UUID not found");
        NeutronVPNIKEPolicy singleton = policyInterface.getNeutronVPNIKEPolicy(policyUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronVPNIKEPolicyAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                    int status = service.canDeleteNeutronVPNIKEPolicy(singleton);
                    if (status < 200 || status > 299)
                        return Response.status(status).build();
                }
            } else {
                throw new ServiceUnavailableException("No providers registered.  Please try again later");
            }
        } else {
            throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
        }
        policyInterface.removeNeutronVPNIKEPolicy(policyUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIKEPolicyAware service = (INeutronVPNIKEPolicyAware) instance;
                service.neutronVPNIKEPolicyDeleted(singleton);
            }
        }
        return Response.status(204).build();
    }
}
