/*
 * Copyright (c) 2016 Intel, Corp. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronQosPolicy;

@Path("/qos/policies")
public final class NeutronQosPolicyNorthbound
        extends AbstractNeutronNorthbound<NeutronQosPolicy, NeutronQosPolicyRequest, INeutronQosPolicyCRUD> {

    private static final String RESOURCE_NAME = "Qos Policy";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all Qos Policies.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack qos Policy attributes
            @QueryParam("id") String queryQosPolicyUUID,
            @QueryParam("tenant_id") String queryQosPolicyTenantID,
            @QueryParam("name") String queryQosPolicyName,
            @QueryParam("shared") Boolean queryQosPolicyIsShared,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {
        INeutronQosPolicyCRUD qosPolicyInterface = getNeutronCRUD();
        List<NeutronQosPolicy> ans = new ArrayList<>();
        for (NeutronQosPolicy nsg : qosPolicyInterface.getAll()) {
            if ((queryQosPolicyUUID == null || queryQosPolicyUUID.equals(nsg.getID()))
                    && (queryQosPolicyTenantID == null || queryQosPolicyTenantID.equals(nsg.getTenantID()))
                    && (queryQosPolicyName == null || queryQosPolicyName.equals(nsg.getName()))
                    && (queryQosPolicyIsShared == null || queryQosPolicyIsShared.equals(nsg.getPolicyIsShared()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronQosPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific Qos Policy.
     */
    @Path("{qosPolicyUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showQosPolicy(@PathParam("qosPolicyUUID") String qosPolicyUUID,
            @QueryParam("fields") List<String> fields) {
        return show(qosPolicyUUID, fields);
    }

    /**
     * Creates new Qos Policy.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createQosPolicies(final NeutronQosPolicyRequest input) {
        return create(input);
    }

    /**
     * Updates a Qos Policy.
     */
    @Path("{qosPolicyUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateQosPolicy(@PathParam("qosPolicyUUID") String qosPolicyUUID,
            final NeutronQosPolicyRequest input) {
        return update(qosPolicyUUID, input);
    }

    /**
     * Deletes a Qos Policy.
     */
    @Path("{qosPolicyUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteQosPolicy(@PathParam("qosPolicyUUID") String qosPolicyUUID) {
        return delete(qosPolicyUUID);
    }
}
