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
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.neutron.spi.INeutronVpnIpSecPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronVpnIpSecPolicy;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for VPN IPSEC Policy.
 */
@Singleton
@Path("/vpn/ipsecpolicies")
public final class NeutronVpnIpSecPoliciesNorthbound extends
        AbstractNeutronNorthbound<NeutronVpnIpSecPolicy, NeutronVpnIpSecPolicyRequest, INeutronVpnIpSecPolicyCRUD> {

    private static final String RESOURCE_NAME = "VPNIPSECPolicy";

    @Inject
    public NeutronVpnIpSecPoliciesNorthbound(@OsgiService INeutronVpnIpSecPolicyCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all VPN IPSEC Policies.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listVpnIPSecPolicies(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("name") String queryName,
            @QueryParam("transform_protocol") String queryTransformProtocol,
            @QueryParam("encapsulation_mode") String queryEncapsulationMode,
            @QueryParam("auth_algorithm") String queryAuthAlgorithm,
            @QueryParam("encryption_algorithm") String queryEncryptionAlgorithm,
            @QueryParam("pfs") String queryPFS
    // pagination and sorting are TODO
    ) {
        INeutronVpnIpSecPolicyCRUD policyInterface = getNeutronCRUD();
        List<NeutronVpnIpSecPolicy> allNeutronVpnIPSecPolicies = policyInterface.getAll();
        List<NeutronVpnIpSecPolicy> ans = new ArrayList<>();
        for (NeutronVpnIpSecPolicy policy : allNeutronVpnIPSecPolicies) {
            if ((queryID == null || queryID.equals(policy.getID()))
                    && (queryName == null || queryName.equals(policy.getName()))
                    && (queryAuthAlgorithm == null || queryAuthAlgorithm.equals(policy.getAuthAlgorithm()))
                    && (queryEncryptionAlgorithm == null
                            || queryEncryptionAlgorithm.equals(policy.getEncryptionAlgorithm()))
                    && (queryPFS == null || queryPFS.equals(policy.getPerfectForwardSecrecy()))
                    && (queryTransformProtocol == null || queryTransformProtocol.equals(policy.getTransformProtocol()))
                    && (queryEncapsulationMode == null || queryEncapsulationMode.equals(policy.getEncapsulationMode()))
                    && (queryTenantID == null || queryTenantID.equals(policy.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(policy.extractFields(fields));
                } else {
                    ans.add(policy);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronVpnIpSecPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific VPN IPSEC Policy.
     */
    @Path("{policyID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showVpnIPSecPolicy(@PathParam("policyID") String policyUUID,
                                       // return fields
                                       @QueryParam("fields") List<String> fields) {
        return show(policyUUID, fields);
    }

    /**
     * Creates new VPN IPSEC Policy.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronVpnIpSecPolicy.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createVpnIPSecPolicy(final NeutronVpnIpSecPolicyRequest input) {
        return create(input);
    }

    /**
     * Updates a VPN IPSEC Policy.
     */
    @Path("{policyID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateVpnIPSecPolicy(@PathParam("policyID") String policyUUID,
                                         final NeutronVpnIpSecPolicyRequest input) {
        return update(policyUUID, input);
    }

    /**
     * Deletes a VPN IPSEC Policy.
     */
    @Path("{policyID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteVpnIPSecPolicy(@PathParam("policyID") String policyUUID) {
        return delete(policyUUID);
    }
}
