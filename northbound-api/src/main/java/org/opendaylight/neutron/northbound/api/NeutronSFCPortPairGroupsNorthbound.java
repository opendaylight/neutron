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
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSFCPortPairGroup;

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

@Path("/sfc/portpairgroups")
public class NeutronSFCPortPairGroupsNorthbound extends AbstractNeutronNorthbound<NeutronSFCPortPairGroup,
        NeutronSFCPortPairGroupRequest, INeutronSFCPortPairGroupCRUD> {

    private static final String RESOURCE_NAME = "Sfc Port Pair Group";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronSFCPortPairGroup extractFields(NeutronSFCPortPairGroup o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronSFCPortPairGroupRequest newNeutronRequest(NeutronSFCPortPairGroup o) {
        return new NeutronSFCPortPairGroupRequest(o);
    }

    @Override
    protected INeutronSFCPortPairGroupCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSFCPortPairGroupCRUD(this);
        if (answer.getSFCPortPairGroupInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getSFCPortPairGroupInterface();
    }

    /**
     * Returns a list of all SFC Port Pair Groups*/

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSFCPortPairGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("port_pairs") List<String> queryPortPairsUUID) {
        INeutronSFCPortPairGroupCRUD sfcPortPairGroupInterface = getNeutronCRUD();
        List<NeutronSFCPortPairGroup> allSFCPortPairGroup = sfcPortPairGroupInterface.getAll();
        List<NeutronSFCPortPairGroup> ans = new ArrayList<>();
        Iterator<NeutronSFCPortPairGroup> i = allSFCPortPairGroup.iterator();
        while (i.hasNext()) {
            NeutronSFCPortPairGroup oSFCPPG = i.next();
            if ((queryID == null || queryID.equals(oSFCPPG.getID()))
                    && (queryName == null || queryName.equals(oSFCPPG.getName()))
                    && (queryTenantID == null || queryTenantID.equals(oSFCPPG.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSFCPPG, fields));
                } else {
                    ans.add(oSFCPPG);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCPortPairGroupRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Port Pair Group*/

    @Path("{portPairGroupUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcPortPairGroupUUID, fields);
    }

    /**
     * Creates new SFC Port Pair Group*/
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCPortPairGroup(final NeutronSFCPortPairGroupRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCPortPairGroup delta, NeutronSFCPortPairGroup original) {
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
     * Updates an existing SFC Port Pair Group*/
    @Path("{portPairGroupUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID,
            final NeutronSFCPortPairGroupRequest input) {
        return update(sfcPortPairGroupUUID, input);
    }

    /**
     * Deletes the SFC Port Pair Group*/

    @Path("{portPairGroupUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCPortPairGroup(@PathParam("portPairGroupUUID") String sfcPortPairGroupUUID) {
        return delete(sfcPortPairGroupUUID);
    }
}
