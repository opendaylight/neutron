/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.opendaylight.neutron.spi.INeutronSFCPortChainCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSFCPortChain;

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
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Neutron Northbound REST APIs for OpenStack SFC Port Chain.<br>
 * This class provides REST APIs for managing OpenStack SFC Port Chain
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

@Path("/sfc/portchains")
public class NeutronSFCPortChainsNorthbound
        extends AbstractNeutronNorthbound<NeutronSFCPortChain,
        NeutronSFCPortChainRequest, INeutronSFCPortChainCRUD> {

    private static final String RESOURCE_NAME = "Sfc Port Chain";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronSFCPortChain extractFields(NeutronSFCPortChain o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronSFCPortChainRequest newNeutronRequest(NeutronSFCPortChain o) {
        return new NeutronSFCPortChainRequest(o);
    }

    @Override
    protected INeutronSFCPortChainCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSFCPortChainCRUD(this);
        if (answer.getSFCPortChainInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getSFCPortChainInterface();
    }

    /**
     * Returns a list of all SFC Port Chains*/

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        Iterator<NeutronSFCPortChain> i = allSFCPortChain.iterator();
        while (i.hasNext()) {
            NeutronSFCPortChain oSFCPC = i.next();
            if ((queryID == null || queryID.equals(oSFCPC.getID())) &&
                    (queryName == null || queryName.equals(oSFCPC.getName())) &&
                    (queryTenantID == null || queryTenantID.equals(oSFCPC.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSFCPC,fields));
                } else {
                    ans.add(oSFCPC);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCPortChainRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Port Chain */

    @Path("{portChainUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCPortChain(
            @PathParam("portChainUUID") String sfcPortChainUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcPortChainUUID, fields);
    }

    /**
     * Creates new SFC Port Chain*/
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCPortChain(final NeutronSFCPortChainRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCPortChain delta, NeutronSFCPortChain original) {
        /*
         *  note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */

        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates an existing SFC Port Chain */
    @Path("{portChainUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCPortChain(
            @PathParam("portChainUUID") String sfcPortChainUUID, final NeutronSFCPortChainRequest input) {
        return update(sfcPortChainUUID, input);
    }

    /**
     * Deletes the SFC Port Chain */
    @Path("{portChainUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCPortChain(
            @PathParam("portChainUUID") String sfcPortChainUUID) {
        return delete(sfcPortChainUUID);
    }
}
