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
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronLoadBalancerListener;

/**
 * Neutron Northbound REST APIs for LoadBalancerListener Policies.<br>
 * This class provides REST APIs for managing neutron LoadBalancerListener Policies
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
@Path("/lbaas/listeners")
public class NeutronLoadBalancerListenerNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronLoadBalancerListener, NeutronLoadBalancerListenerRequest, INeutronLoadBalancerListenerCRUD, INeutronLoadBalancerListenerAware> {

    private static final String RESOURCE_NAME = "LoadBalancerListener";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronLoadBalancerListener extractFields(NeutronLoadBalancerListener o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronLoadBalancerListenerRequest newNeutronRequest(NeutronLoadBalancerListener o) {
        return new NeutronLoadBalancerListenerRequest(o);
    }

    @Override
    protected INeutronLoadBalancerListenerCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronLoadBalancerListenerCRUD(this);
        if (answer.getLoadBalancerListenerInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getLoadBalancerListenerInterface();
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronLoadBalancerListenerAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronLoadBalancerListener singleton) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        return service.canCreateNeutronLoadBalancerListener(singleton);
    }

    @Override
    protected void created(Object instance, NeutronLoadBalancerListener singleton) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        service.neutronLoadBalancerListenerCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronLoadBalancerListener delta, NeutronLoadBalancerListener original) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        return service.canUpdateNeutronLoadBalancerListener(delta, original);
    }

    @Override
    protected void updated(Object instance, NeutronLoadBalancerListener updated) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        service.neutronLoadBalancerListenerUpdated(updated);
    }

    @Override
    protected int canDelete(Object instance, NeutronLoadBalancerListener singleton) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        return service.canDeleteNeutronLoadBalancerListener(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronLoadBalancerListener singleton) {
        INeutronLoadBalancerListenerAware service = (INeutronLoadBalancerListenerAware) instance;
        service.neutronLoadBalancerListenerDeleted(singleton);
    }

    /**
     * Returns a list of all LoadBalancerListener */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        List<NeutronLoadBalancerListener> allLoadBalancerListeners = loadBalancerListenerInterface.getAllNeutronLoadBalancerListeners();
        List<NeutronLoadBalancerListener> ans = new ArrayList<NeutronLoadBalancerListener>();
        Iterator<NeutronLoadBalancerListener> i = allLoadBalancerListeners.iterator();
        while (i.hasNext()) {
            NeutronLoadBalancerListener nsg = i.next();
            if ((queryLoadBalancerListenerID == null ||
                    queryLoadBalancerListenerID.equals(nsg.getID())) &&
                    (queryLoadBalancerListenerDefaultPoolID == null ||
                            queryLoadBalancerListenerDefaultPoolID.equals(nsg.getNeutronLoadBalancerListenerDefaultPoolID())) &&
                    (queryLoadBalancerListenerTenantID == null ||
                            queryLoadBalancerListenerTenantID.equals(nsg.getTenantID())) &&
                    (queryLoadBalancerListenerName == null ||
                            queryLoadBalancerListenerName.equals(nsg.getLoadBalancerListenerName())) &&
                    (queryLoadBalancerListenerProtocol == null ||
                            queryLoadBalancerListenerProtocol.equals(nsg.getNeutronLoadBalancerListenerProtocol())) &&
                    (queryLoadBalancerListenerProtocolPort == null ||
                            queryLoadBalancerListenerProtocolPort.equals(nsg.getNeutronLoadBalancerListenerProtocolPort())) &&
                    (queryLoadBalancerListenerAdminIsUp == null ||
                            queryLoadBalancerListenerAdminIsUp.equals(nsg.getLoadBalancerListenerAdminStateIsUp()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg,fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronLoadBalancerListenerRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancerListener */

    @Path("{loadBalancerListenerID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
     * Creates new LoadBalancerListener */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerListeners(final NeutronLoadBalancerListenerRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancerListener Policy
     */
    @Path("{loadBalancerListenerID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancerListener(
            @PathParam("loadBalancerListenerID") String loadBalancerListenerID, final NeutronLoadBalancerListenerRequest input) {
        return update(loadBalancerListenerID, input);
    }

    /**
     * Deletes a LoadBalancerListener */

    @Path("{loadBalancerListenerID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerListener(
            @PathParam("loadBalancerListenerID") String loadBalancerListenerID) {
        return delete(loadBalancerListenerID);
    }
}
