/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronBgpvpn;

/**
 * Neutron Northbound REST APIs for Bgpvpn.<br>
 * This class provides REST APIs for managing neutron Bgpvpns
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

@Path("/bgpvpns")
public class NeutronBgpvpnsNorthbound {

    @Context
    UriInfo uriInfo;

    private static final String RESOURCE_NAME = "Bgpvpn";
    private NeutronBgpvpn extractFields(NeutronBgpvpn o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronBgpvpnCRUD(this);
        if (answer.getBgpvpnInterface() == null) {
            throw new ServiceUnavailableException("Service is unavailable");
        }
        return answer;
    }

    /**
     * Returns a list of all Bgpvpns */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listBgpvpns(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("type") String queryType,
            @QueryParam("auto_aggregate") String queryAutoAggregate,
            // pagination
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
            // sorting not supported
            ) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronInterfaces().getBgpvpnInterface();
        List<NeutronBgpvpn> allBgpvpns = bgpvpnInterface.getAllBgpvpns();
        List<NeutronBgpvpn> ans = new ArrayList<NeutronBgpvpn>();
        Iterator<NeutronBgpvpn> i = allBgpvpns.iterator();
        while (i.hasNext()) {
            NeutronBgpvpn oSN = i.next();
            //match filters: TODO provider extension
            Boolean bAdminStateUp = null;
            Boolean bAutoAggregate = null;
            if (queryAdminStateUp != null) {
                bAdminStateUp = Boolean.valueOf(queryAdminStateUp);
            }
            if (queryAutoAggregate != null) {
                bAutoAggregate = Boolean.valueOf(queryAutoAggregate);
            }
            if ((queryID == null || queryID.equals(oSN.getID())) &&
                    (queryName == null || queryName.equals(oSN.getBgpvpnName())) &&
                    (bAdminStateUp == null || bAdminStateUp.booleanValue() == oSN.isAdminStateUp()) &&
                    (queryStatus == null || queryStatus.equals(oSN.getStatus())) &&
                    (bAutoAggregate == null || bAutoAggregate.booleanValue() == oSN.isAutoAggregate()) &&
                    (queryTenantID == null || queryTenantID.equals(oSN.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSN,fields));
                } else {
                    ans.add(oSN);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronBgpvpnRequest request = (NeutronBgpvpnRequest) PaginatedRequestFactory.createRequest(limit,
                    marker, pageReverse, uriInfo, ans, NeutronBgpvpn.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

    return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronBgpvpnRequest(ans)).build();

    }

    /**
     * Returns a specific Bgpvpn */

    @Path("{bgpvpnUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showBgpvpn(
            @PathParam("bgpvpnUUID") String bgpvpnUUID,
            // return fields
            @QueryParam("fields") List<String> fields
            ) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronInterfaces().getBgpvpnInterface();
        if (!bgpvpnInterface.bgpvpnExists(bgpvpnUUID)) {
            throw new ResourceNotFoundException("UUID does not exist");
        }
        if (fields.size() > 0) {
            NeutronBgpvpn ans = bgpvpnInterface.getBgpvpn(bgpvpnUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronBgpvpnRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronBgpvpnRequest(bgpvpnInterface.getBgpvpn(bgpvpnUUID))).build();
        }
    }

    /**
     * Creates new Bgpvpns */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @TypeHint(NeutronBgpvpn.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createBgpvpns(final NeutronBgpvpnRequest input) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronInterfaces().getBgpvpnInterface();
        if (input.isSingleton()) {
            NeutronBgpvpn singleton = input.getSingleton();

            // add bgpvpn to MDSAL
            singleton.initDefaults();
            bgpvpnInterface.addBgpvpn(singleton);
        } else {
            // add items to MDSAL
            for (NeutronBgpvpn test : input.getBulk()) {
                test.initDefaults();
                bgpvpnInterface.addBgpvpn(test);
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Bgpvpn */
    @Path("{bgpvpnUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackBgpvpns.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateBgpvpn(
            @PathParam("bgpvpnUUID") String bgpvpnUUID, final NeutronBgpvpnRequest input
            ) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronInterfaces().getBgpvpnInterface();

        NeutronBgpvpn updatedObject = input.getSingleton();
        NeutronBgpvpn original = bgpvpnInterface.getBgpvpn(bgpvpnUUID);

        /*
         *  note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */

        updatedObject.setID(bgpvpnUUID);
        updatedObject.setTenantID(original.getTenantID());
        //Fill in defaults if they're missing in update
        updatedObject.initDefaults();

        // update bgpvpn object
        bgpvpnInterface.updateBgpvpn(bgpvpnUUID, updatedObject);

        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronBgpvpnRequest(bgpvpnInterface.getBgpvpn(bgpvpnUUID))).build();
    }

    /**
     * Deletes a Bgpvpn */

    @Path("{bgpvpnUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteBgpvpn(
            @PathParam("bgpvpnUUID") String bgpvpnUUID) {
        INeutronBgpvpnCRUD bgpvpnInterface = getNeutronInterfaces().getBgpvpnInterface();

        if (!bgpvpnInterface.removeBgpvpn(bgpvpnUUID)) {
            throw new InternalServerErrorException("Could not delete bgpvpn");
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
