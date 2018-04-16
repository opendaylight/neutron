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
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for LoadBalancerListener Policies.
 */
@Singleton
@Path("/lbaas/listeners")
public final class NeutronLoadBalancerListenerNorthbound extends AbstractNeutronNorthbound<NeutronLoadBalancerListener,
        NeutronLoadBalancerListenerRequest, INeutronLoadBalancerListenerCRUD> {

    private static final String RESOURCE_NAME = "LoadBalancerListener";

    @Inject
    public NeutronLoadBalancerListenerNorthbound(@OsgiService INeutronLoadBalancerListenerCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all LoadBalancerListener.
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
            // OpenStack LoadBalancerListener attributes
            @QueryParam("id") String queryLoadBalancerListenerID,
            @QueryParam("default_pool_id") String queryLoadBalancerListenerDefaultPoolID,
            @QueryParam("tenant_id") String queryLoadBalancerListenerTenantID,
            @QueryParam("name") String queryLoadBalancerListenerName,
            @QueryParam("protocol") String queryLoadBalancerListenerProtocol,
            @QueryParam("protocol_port") String queryLoadBalancerListenerProtocolPort,
            @QueryParam("admin_state_up") Boolean queryLoadBalancerListenerAdminIsUp,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronLoadBalancerListenerCRUD loadBalancerListenerInterface = getNeutronCRUD();
        List<NeutronLoadBalancerListener> allLoadBalancerListeners = loadBalancerListenerInterface.getAll();
        List<NeutronLoadBalancerListener> ans = new ArrayList<>();
        for (NeutronLoadBalancerListener nsg : allLoadBalancerListeners) {
            if ((queryLoadBalancerListenerID == null || queryLoadBalancerListenerID.equals(nsg.getID()))
                    && (queryLoadBalancerListenerDefaultPoolID == null || queryLoadBalancerListenerDefaultPoolID
                            .equals(nsg.getNeutronLoadBalancerListenerDefaultPoolID()))
                    && (queryLoadBalancerListenerTenantID == null
                            || queryLoadBalancerListenerTenantID.equals(nsg.getTenantID()))
                    && (queryLoadBalancerListenerName == null
                            || queryLoadBalancerListenerName.equals(nsg.getName()))
                    && (queryLoadBalancerListenerProtocol == null
                            || queryLoadBalancerListenerProtocol.equals(nsg.getNeutronLoadBalancerListenerProtocol()))
                    && (queryLoadBalancerListenerProtocolPort == null || queryLoadBalancerListenerProtocolPort
                            .equals(nsg.getNeutronLoadBalancerListenerProtocolPort()))
                    && (queryLoadBalancerListenerAdminIsUp == null || queryLoadBalancerListenerAdminIsUp
                            .equals(nsg.getLoadBalancerListenerAdminStateIsUp()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronLoadBalancerListenerRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancerListener.
     */
    @Path("{loadBalancerListenerID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancerListener(@PathParam("loadBalancerListenerID") String loadBalancerListenerID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(loadBalancerListenerID, fields);
    }

    /**
     * Creates new LoadBalancerListener.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerListeners(final NeutronLoadBalancerListenerRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancerListener Policy.
     */
    @Path("{loadBalancerListenerID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancerListener(@PathParam("loadBalancerListenerID") String loadBalancerListenerID,
            final NeutronLoadBalancerListenerRequest input) {
        return update(loadBalancerListenerID, input);
    }

    /**
     * Deletes a LoadBalancerListener.
     */
    @Path("{loadBalancerListenerID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerListener(@PathParam("loadBalancerListenerID") String loadBalancerListenerID) {
        return delete(loadBalancerListenerID);
    }
}
