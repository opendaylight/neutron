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

import org.opendaylight.neutron.spi.INeutronMeteringLabelAware;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;

/**
 * Neutron Northbound REST APIs for Metering Lables.<br>
 * This class provides REST APIs for managing neutron metering labels
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

@Path("/metering/metering-labels")
public class NeutronMeteringLabelsNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronMeteringLabel, NeutronMeteringLabelRequest, INeutronMeteringLabelCRUD, INeutronMeteringLabelAware> {
    private static final String RESOURCE_NAME = "Metering Label";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronMeteringLabel extractFields(NeutronMeteringLabel o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected INeutronMeteringLabelCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronMeteringLabelCRUD(this);
        if (answer.getMeteringLabelInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getMeteringLabelInterface();
    }

    @Override
    protected NeutronMeteringLabelRequest newNeutronRequest(NeutronMeteringLabel o) {
        return new NeutronMeteringLabelRequest(o);
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronMeteringLabelAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronMeteringLabel singleton) {
        INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
        return service.canCreateMeteringLabel(singleton);
    }

    @Override
    protected void created(Object instance, NeutronMeteringLabel singleton) {
        INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
        service.neutronMeteringLabelCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronMeteringLabel delta, NeutronMeteringLabel original) {
        return 0;
    }

    @Override
    protected void updated(Object instance, NeutronMeteringLabel updated) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int canDelete(Object instance, NeutronMeteringLabel singleton) {
        INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
        return service.canDeleteMeteringLabel(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronMeteringLabel singleton) {
        INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
        service.neutronMeteringLabelDeleted(singleton);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering labels */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listMeteringLabels(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID
            // pagination and sorting are TODO
            ) {
        INeutronMeteringLabelCRUD labelInterface = getNeutronCRUD();
        List<NeutronMeteringLabel> allNeutronMeteringLabel = labelInterface.getAllNeutronMeteringLabels();
        List<NeutronMeteringLabel> ans = new ArrayList<NeutronMeteringLabel>();
        Iterator<NeutronMeteringLabel> i = allNeutronMeteringLabel.iterator();
        while (i.hasNext()) {
            NeutronMeteringLabel oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getMeteringLabelName())) &&
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
                new NeutronMeteringLabelRequest(ans)).build();
    }

    /**
     * Returns a specific metering label */

    @Path("{labelUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showMeteringLabel(
            @PathParam("labelUUID") String labelUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(labelUUID, fields);
    }

    /**
     * Creates new metering label */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createMeteringLabel(final NeutronMeteringLabelRequest input) {
        return create(input);
    }

    /**
     * Deletes a Metering Label */

    @Path("{labelUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteMeteringLabel(
            @PathParam("labelUUID") String labelUUID) {
        return delete(labelUUID);
    }
}
