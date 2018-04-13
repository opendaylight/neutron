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
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerHealthMonitor;

/**
 * Neutron Northbound REST APIs for Load Balancer HealthMonitor.
 *
 */
@Path("/lbaas/healthmonitors")
public final class NeutronLoadBalancerHealthMonitorNorthbound
        extends AbstractNeutronNorthbound<NeutronLoadBalancerHealthMonitor, NeutronLoadBalancerHealthMonitorRequest,
                INeutronLoadBalancerHealthMonitorCRUD> {

    private static final String RESOURCE_NAME = "LoadBalancerHealthMonitor";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all LoadBalancerHealthMonitor.
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
            // OpenStack LoadBalancerHealthMonitor attributes
            @QueryParam("id") String queryLoadBalancerHealthMonitorID,
            @QueryParam("tenant_id") String queryLoadBalancerHealthMonitorTenantID,
            // TODO "type" is being a property by the JSON parser.
            @QueryParam("type") String queryLoadBalancerHealthMonitorType,
            @QueryParam("delay") Integer queryLoadBalancerHealthMonitorDelay,
            @QueryParam("timeout") Integer queryLoadBalancerHealthMonitorTimeout,
            @QueryParam("max_retries") Integer queryLoadBalancerHealthMonitorMaxRetries,
            @QueryParam("http_method") String queryLoadBalancerHealthMonitorHttpMethod,
            @QueryParam("url_path") String queryLoadBalancerHealthMonitorUrlPath,
            @QueryParam("expected_codes") String queryLoadBalancerHealthMonitorExpectedCodes,
            @QueryParam("admin_state_up") Boolean queryLoadBalancerHealthMonitorIsAdminStateUp,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronLoadBalancerHealthMonitorCRUD loadBalancerHealthMonitorInterface = getNeutronCRUD();
        List<NeutronLoadBalancerHealthMonitor> allLoadBalancerHealthMonitors = loadBalancerHealthMonitorInterface
                .getAll();
        List<NeutronLoadBalancerHealthMonitor> ans = new ArrayList<>();
        for (NeutronLoadBalancerHealthMonitor nsg : allLoadBalancerHealthMonitors) {
            if ((queryLoadBalancerHealthMonitorID == null || queryLoadBalancerHealthMonitorID.equals(nsg.getID()))
                    && (queryLoadBalancerHealthMonitorTenantID == null
                            || queryLoadBalancerHealthMonitorTenantID.equals(nsg.getTenantID()))
                    && (queryLoadBalancerHealthMonitorType == null
                            || queryLoadBalancerHealthMonitorType.equals(nsg.getLoadBalancerHealthMonitorType()))
                    && (queryLoadBalancerHealthMonitorDelay == null
                            || queryLoadBalancerHealthMonitorDelay.equals(nsg.getLoadBalancerHealthMonitorDelay()))
                    && (queryLoadBalancerHealthMonitorTimeout == null
                            || queryLoadBalancerHealthMonitorTimeout.equals(nsg.getLoadBalancerHealthMonitorTimeout()))
                    && (queryLoadBalancerHealthMonitorMaxRetries == null || queryLoadBalancerHealthMonitorMaxRetries
                            .equals(nsg.getLoadBalancerHealthMonitorMaxRetries()))
                    && (queryLoadBalancerHealthMonitorHttpMethod == null || queryLoadBalancerHealthMonitorHttpMethod
                            .equals(nsg.getLoadBalancerHealthMonitorHttpMethod()))
                    && (queryLoadBalancerHealthMonitorUrlPath == null
                            || queryLoadBalancerHealthMonitorUrlPath.equals(nsg.getLoadBalancerHealthMonitorUrlPath()))
                    && (queryLoadBalancerHealthMonitorExpectedCodes == null
                            || queryLoadBalancerHealthMonitorExpectedCodes
                                    .equals(nsg.getLoadBalancerHealthMonitorExpectedCodes()))
                    && (queryLoadBalancerHealthMonitorIsAdminStateUp == null
                            || queryLoadBalancerHealthMonitorIsAdminStateUp
                                    .equals(nsg.getLoadBalancerHealthMonitorAdminStateIsUp()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronLoadBalancerHealthMonitorRequest(ans))
                .build();
    }

    /**
     * Returns a specific LoadBalancerHealthMonitor.
     */
    @Path("{loadBalancerHealthMonitorID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancerHealthMonitor(
            @PathParam("loadBalancerHealthMonitorID") String loadBalancerHealthMonitorID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(loadBalancerHealthMonitorID, fields);
    }

    /**
     * Creates new LoadBalancerHealthMonitor.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerHealthMonitors(final NeutronLoadBalancerHealthMonitorRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancerHealthMonitor Policy.
     */
    @Path("{loadBalancerHealthMonitorID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancerHealthMonitor(
            @PathParam("loadBalancerHealthMonitorID") String loadBalancerHealthMonitorID,
            final NeutronLoadBalancerHealthMonitorRequest input) {
        return update(loadBalancerHealthMonitorID, input);
    }

    /**
     * Deletes a LoadBalancerHealthMonitor.
     */
    @Path("{loadBalancerHealthMonitorID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerHealthMonitor(
            @PathParam("loadBalancerHealthMonitorID") String loadBalancerHealthMonitorID) {
        return delete(loadBalancerHealthMonitorID);
    }
}
