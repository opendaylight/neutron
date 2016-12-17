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
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyCRUD;
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
public final class NeutronVPNIKEPoliciesNorthbound
        extends AbstractNeutronNorthbound<NeutronVPNIKEPolicy, NeutronVPNIKEPolicyRequest, INeutronVPNIKEPolicyCRUD> {
    private static final String RESOURCE_NAME = "VPNIKEPolicy";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all VPN IKE Policies */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVPNIKEPolicies(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("auth_algorithm") String queryAuthAlgorithm,
            @QueryParam("encryption_algorithm") String queryEncryptionAlgorithm,
            @QueryParam("phase1_negotiation_mode") String queryPhase1NegotiationMode,
            @QueryParam("pfs") String queryPFS,
            @QueryParam("ike_version") String queryIKEVersion
    // pagination and sorting are TODO
    ) {
        INeutronVPNIKEPolicyCRUD labelInterface = getNeutronCRUD();
        List<NeutronVPNIKEPolicy> allNeutronVPNIKEPolicy = labelInterface.getAll();
        List<NeutronVPNIKEPolicy> ans = new ArrayList<>();
        for (NeutronVPNIKEPolicy policy : allNeutronVPNIKEPolicy) {
            if ((queryID == null || queryID.equals(policy.getID()))
                    && (queryName == null || queryName.equals(policy.getName()))
                    && (queryAuthAlgorithm == null || queryAuthAlgorithm.equals(policy.getAuthAlgorithm()))
                    && (queryEncryptionAlgorithm == null
                            || queryEncryptionAlgorithm.equals(policy.getEncryptionAlgorithm()))
                    && (queryPhase1NegotiationMode == null
                            || queryPhase1NegotiationMode.equals(policy.getPhase1NegotiationMode()))
                    && (queryPFS == null || queryPFS.equals(policy.getPerfectForwardSecrecy()))
                    && (queryIKEVersion == null || queryIKEVersion.equals(policy.getIkeVersion()))
                    && (queryTenantID == null || queryTenantID.equals(policy.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(policy.extractFields(fields));
                } else {
                    ans.add(policy);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVPNIKEPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IKE Policy */

    @Path("{policyID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showVPNIKEPolicy(@PathParam("policyID") String policyUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(policyUUID, fields);
    }

    /**
     * Creates new VPN IKE Policy */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVPNIKEPolicy.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVPNIKEPolicy(final NeutronVPNIKEPolicyRequest input) {
        return create(input);
    }

    /**
     * Updates a VPN IKE Policy */
    @Path("{policyID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVPNIKEPolicy(@PathParam("policyID") String policyUUID,
            final NeutronVPNIKEPolicyRequest input) {
        return update(policyUUID, input);
    }

    /**
     * Deletes a VPN IKE Policy */

    @Path("{policyID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVPNIKEPolicy(@PathParam("policyID") String policyUUID) {
        return delete(policyUUID);
    }
}
