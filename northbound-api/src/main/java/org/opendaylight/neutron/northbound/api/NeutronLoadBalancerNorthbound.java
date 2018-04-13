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
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;

/**
 * Neutron Northbound REST APIs for LoadBalancers.<br>
 */
@Path("/lbaas/loadbalancers")
public final class NeutronLoadBalancerNorthbound
        extends AbstractNeutronNorthbound<NeutronLoadBalancer, NeutronLoadBalancerRequest, INeutronLoadBalancerCRUD> {

    private static final String RESOURCE_NAME = "LoadBalancer";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all LoadBalancer.
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
            // OpenStack LoadBalancer attributes
            @QueryParam("id") String queryLoadBalancerID,
            @QueryParam("tenant_id") String queryLoadBalancerTenantID,
            @QueryParam("name") String queryLoadBalancerName,
            @QueryParam("status") String queryLoadBalancerStatus,
            @QueryParam("vip_address") String queryLoadBalancerVipAddress,
            @QueryParam("vip_subnet") String queryLoadBalancerVipSubnet,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronLoadBalancerCRUD loadBalancerInterface = getNeutronCRUD();
        List<NeutronLoadBalancer> allLoadBalancers = loadBalancerInterface.getAll();
        List<NeutronLoadBalancer> ans = new ArrayList<>();
        for (NeutronLoadBalancer nsg : allLoadBalancers) {
            if ((queryLoadBalancerID == null || queryLoadBalancerID.equals(nsg.getID()))
                    && (queryLoadBalancerTenantID == null || queryLoadBalancerTenantID.equals(nsg.getTenantID()))
                    && (queryLoadBalancerName == null || queryLoadBalancerName.equals(nsg.getName()))
                    && (queryLoadBalancerVipAddress == null
                            || queryLoadBalancerVipAddress.equals(nsg.getLoadBalancerVipAddress()))
                    && (queryLoadBalancerVipSubnet == null
                            || queryLoadBalancerVipSubnet.equals(nsg.getLoadBalancerVipSubnetID()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronLoadBalancerRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancer.
     */
    @Path("{loadBalancerID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancer(@PathParam("loadBalancerID") String loadBalancerID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(loadBalancerID, fields);
    }

    /**
     * Creates new LoadBalancer.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancers(final NeutronLoadBalancerRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancer Policy.
     */
    @Path("{loadBalancerID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancer(@PathParam("loadBalancerID") String loadBalancerID,
            final NeutronLoadBalancerRequest input) {
        return update(loadBalancerID, input);
    }

    /**
     * Deletes a LoadBalancer.
     */
    @Path("{loadBalancerID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancer(@PathParam("loadBalancerID") String loadBalancerID) {
        return delete(loadBalancerID);
    }
}
