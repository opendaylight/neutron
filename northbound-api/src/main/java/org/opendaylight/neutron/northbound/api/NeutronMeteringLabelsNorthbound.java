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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;

/**
 * Neutron Northbound REST APIs for Metering Lables.
 */
@Path("/metering/metering-labels")
public final class NeutronMeteringLabelsNorthbound extends
        AbstractNeutronNorthbound<NeutronMeteringLabel, NeutronMeteringLabelRequest, INeutronMeteringLabelCRUD> {

    private static final String RESOURCE_NAME = "Metering Label";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all metering labels.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        List<NeutronMeteringLabel> allNeutronMeteringLabel = labelInterface.getAll();
        List<NeutronMeteringLabel> ans = new ArrayList<>();
        for (NeutronMeteringLabel label : allNeutronMeteringLabel) {
            if ((queryID == null || queryID.equals(label.getID()))
                    && (queryName == null || queryName.equals(label.getName()))
                    && (queryTenantID == null || queryTenantID.equals(label.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(label.extractFields(fields));
                } else {
                    ans.add(label);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronMeteringLabelRequest(ans)).build();
    }

    /**
     * Returns a specific metering label.
     */
    @Path("{labelUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showMeteringLabel(@PathParam("labelUUID") String labelUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(labelUUID, fields);
    }

    /**
     * Creates new metering label.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createMeteringLabel(final NeutronMeteringLabelRequest input) {
        return create(input);
    }

    /**
     * Deletes a Metering Label.
     */
    @Path("{labelUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteMeteringLabel(@PathParam("labelUUID") String labelUUID) {
        return delete(labelUUID);
    }
}
