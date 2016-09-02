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
import org.opendaylight.neutron.spi.INeutronSFCPortPairCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSFCPortPair;

/**
 * Neutron Northbound REST APIs for OpenStack SFC Port Pair.<br>
 * This class provides REST APIs for managing OpenStack SFC Port Pair
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

@Path("/sfc/portpairs")
public class NeutronSFCPortPairsNorthbound
        extends AbstractNeutronNorthbound<NeutronSFCPortPair, NeutronSFCPortPairRequest, INeutronSFCPortPairCRUD> {

    private static final String RESOURCE_NAME = "Sfc Port Pair";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected INeutronSFCPortPairCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSFCPortPairCRUD(this);
        if (answer.getSFCPortPairInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getSFCPortPairInterface();
    }

    /**
     * Returns a list of all SFC Port Pairs*/

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSFCPortPairs(
            // return fields
            @QueryParam("fields") List<String> fields,
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("ingress") String queryIngressPort,
            @QueryParam("egress") String queryEgressPort) {
        INeutronSFCPortPairCRUD sfcPortPairInterface = getNeutronCRUD();
        List<NeutronSFCPortPair> allSFCPortPair = sfcPortPairInterface.getAll();
        List<NeutronSFCPortPair> ans = new ArrayList<>();
        Iterator<NeutronSFCPortPair> i = allSFCPortPair.iterator();
        while (i.hasNext()) {
            NeutronSFCPortPair oSFCPP = i.next();
            if ((queryID == null || queryID.equals(oSFCPP.getID()))
                    && (queryName == null || queryName.equals(oSFCPP.getName()))
                    && (queryIngressPort == null || queryIngressPort.equals(oSFCPP.getIngressPortUUID()))
                    && (queryEgressPort == null || queryEgressPort.equals(oSFCPP.getEgressPortUUID()))
                    && (queryTenantID == null || queryTenantID.equals(oSFCPP.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(oSFCPP.extractFields(fields));
                } else {
                    ans.add(oSFCPP);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCPortPairRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Port Pair */

    @Path("{portPairUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCPortPair(@PathParam("portPairUUID") String sfcPortPairUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcPortPairUUID, fields);
    }

    /**
     * Creates new SFC Port Pair*/
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCPortPair(final NeutronSFCPortPairRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCPortPair delta, NeutronSFCPortPair original) {
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
     * Updates an existing SFC Port Pair */
    @Path("{portPairUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCPortPair(@PathParam("portPairUUID") String sfcPortPairUUID,
            final NeutronSFCPortPairRequest input) {
        return update(sfcPortPairUUID, input);
    }

    /**
     * Deletes the SFC Port Pair */

    @Path("{portPairUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCPortPair(@PathParam("portPairUUID") String sfcPortPairUUID) {
        return delete(sfcPortPairUUID);
    }

}
