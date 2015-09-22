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
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronVPNIPSECPolicy;

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
public class NeutronVPNIPSECPoliciesNorthbound extends AbstractNeutronNorthbound {

    private static final String RESOURCE_NAME = "VPNIPSECPolicy";

    private NeutronVPNIPSECPolicy extractFields(NeutronVPNIPSECPolicy o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronVPNIPSECPolicyCRUD(this);
        if (answer.getVPNIPSECPolicyInterface() == null) {
            throw new ServiceUnavailableException("NeutronVPNIPSECPolicy CRUD Interface "
                + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all VPN IPSEC Policies */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVPNIPSECPolicies(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName,
            @QueryParam("description") String queryDescription,
            @QueryParam("transform_protocol") String queryTransformProtocol,
            @QueryParam("encapsulation_mode") String queryEncapsulationMode,
            @QueryParam("auth_algorithm") String queryAuthAlgorithm,
            @QueryParam("encryption_algorithm") String queryEncryptionAlgorithm,
            @QueryParam("pfs") String queryPFS
            // pagination and sorting are TODO
            ) {
        INeutronVPNIPSECPolicyCRUD policyInterface = getNeutronInterfaces().getVPNIPSECPolicyInterface();
        List<NeutronVPNIPSECPolicy> allNeutronVPNIPSECPolicies = policyInterface.getAllNeutronVPNIPSECPolicies();
        List<NeutronVPNIPSECPolicy> ans = new ArrayList<NeutronVPNIPSECPolicy>();
        Iterator<NeutronVPNIPSECPolicy> i = allNeutronVPNIPSECPolicies.iterator();
        while (i.hasNext()) {
            NeutronVPNIPSECPolicy oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getName())) &&
                    (queryDescription == null || queryDescription.equals(oSS.getDescription())) &&
                    (queryAuthAlgorithm == null || queryAuthAlgorithm.equals(oSS.getAuthAlgorithm())) &&
                    (queryEncryptionAlgorithm == null || queryEncryptionAlgorithm.equals(oSS.getEncryptionAlgorithm())) &&
                    (queryPFS == null || queryPFS.equals(oSS.getPerfectForwardSecrecy())) &&
                    (queryTransformProtocol == null || queryTransformProtocol.equals(oSS.getTransformProtocol())) &&
                    (queryEncapsulationMode == null || queryEncapsulationMode.equals(oSS.getEncapsulationMode())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS,fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronVPNIPSECPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IPSEC Policy */

    @Path("{policyID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showVPNIPSECPolicy(
            @PathParam("policyID") String policyUUID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        INeutronVPNIPSECPolicyCRUD policyInterface = getNeutronInterfaces().getVPNIPSECPolicyInterface();
        if (!policyInterface.neutronVPNIPSECPolicyExists(policyUUID)) {
            throw new ResourceNotFoundException(uuidNoExist(RESOURCE_NAME));
        }
        if (fields.size() > 0) {
            NeutronVPNIPSECPolicy ans = policyInterface.getNeutronVPNIPSECPolicy(policyUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronVPNIPSECPolicyRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(
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
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVPNIPSECPolicy(final NeutronVPNIPSECPolicyRequest input) {
        INeutronVPNIPSECPolicyCRUD ipsecPolicyInterface = getNeutronInterfaces().getVPNIPSECPolicyInterface();
        if (input.isSingleton()) {
            NeutronVPNIPSECPolicy singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECPolicyAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                    int status = service.canCreateNeutronVPNIPSECPolicy(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
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
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a VPN IPSEC Policy */
    @Path("{policyID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNIPSECPolicy(
            @PathParam("policyID") String policyUUID, final NeutronVPNIPSECPolicyRequest input
            ) {
        INeutronVPNIPSECPolicyCRUD ipsecPolicyInterface = getNeutronInterfaces().getVPNIPSECPolicyInterface();

        NeutronVPNIPSECPolicy singleton = input.getSingleton();
        NeutronVPNIPSECPolicy original = ipsecPolicyInterface.getNeutronVPNIPSECPolicy(policyUUID);

        Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECPolicyAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                int status = service.canUpdateNeutronVPNIPSECPolicy(singleton, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
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
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronVPNIPSECPolicyRequest(ipsecPolicyInterface.getNeutronVPNIPSECPolicy(policyUUID))).build();
    }

    /**
     * Deletes a VPN IPSEC Policy */

    @Path("{policyID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVPNIPSECPolicy(
            @PathParam("policyID") String policyUUID) {
        final INeutronVPNIPSECPolicyCRUD policyInterface = getNeutronInterfaces().getVPNIPSECPolicyInterface();

        NeutronVPNIPSECPolicy singleton = policyInterface.getNeutronVPNIPSECPolicy(policyUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronVPNIPSECPolicyAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                int status = service.canDeleteNeutronVPNIPSECPolicy(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }
        deleteUuid(RESOURCE_NAME, policyUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return policyInterface.removeNeutronVPNIPSECPolicy(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronVPNIPSECPolicyAware service = (INeutronVPNIPSECPolicyAware) instance;
                service.neutronVPNIPSECPolicyDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
