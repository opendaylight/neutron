/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronSFCPortChainCRUD;
import org.opendaylight.neutron.spi.NeutronSFCPortChain;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for OpenStack SFC Port Chain.
 */
@Singleton
@Path("/sfc/portchains")
public final class NeutronSFCPortChainsNorthbound
        extends AbstractNeutronNorthbound<NeutronSFCPortChain, NeutronSFCPortChainRequest, INeutronSFCPortChainCRUD> {

    private static final String RESOURCE_NAME = "Sfc Port Chain";

    @Inject
    public NeutronSFCPortChainsNorthbound(@OsgiService INeutronSFCPortChainCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all SFC Port Chains.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSFCPortChains(
            // return fields
            @QueryParam("fields") List<String> fields,
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID) {
        INeutronSFCPortChainCRUD sfcPortChainInterface = getNeutronCRUD();
        List<NeutronSFCPortChain> allSFCPortChain = sfcPortChainInterface.getAll();
        List<NeutronSFCPortChain> ans = new ArrayList<>();
        for (NeutronSFCPortChain sfcPortChain : allSFCPortChain) {
            if ((queryID == null || queryID.equals(sfcPortChain.getID()))
                    && (queryName == null || queryName.equals(sfcPortChain.getName()))
                    && (queryTenantID == null || queryTenantID.equals(sfcPortChain.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(sfcPortChain.extractFields(fields));
                } else {
                    ans.add(sfcPortChain);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCPortChainRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Port Chain.
     */
    @Path("{portChainUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCPortChain(@PathParam("portChainUUID") String sfcPortChainUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcPortChainUUID, fields);
    }

    /**
     * Creates new SFC Port Chain.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCPortChain(final NeutronSFCPortChainRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCPortChain delta, NeutronSFCPortChain original) {
        /*
         * note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */
        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates an existing SFC Port Chain.
     */
    @Path("{portChainUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCPortChain(@PathParam("portChainUUID") String sfcPortChainUUID,
            final NeutronSFCPortChainRequest input) {
        return update(sfcPortChainUUID, input);
    }

    /**
     * Deletes the SFC Port Chain.
     */
    @Path("{portChainUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCPortChain(@PathParam("portChainUUID") String sfcPortChainUUID) {
        return delete(sfcPortChainUUID);
    }
}
