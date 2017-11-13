/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.NeutronPort;

/**
 * Neutron Northbound REST APIs.<br>
 * This class provides REST APIs for managing neutron port objects
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

@Path("/ports")
public final class NeutronPortsNorthbound
        extends AbstractNeutronNorthbound<NeutronPort, NeutronPortRequest, INeutronPortCRUD> {

    private static final String RESOURCE_NAME = "Port";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all Ports.
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listPorts(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("network_id") String queryNetworkID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") Boolean queryAdminStateUp,
            @QueryParam("mac_address") String queryMACAddress,
            @QueryParam("device_id") String queryDeviceID,
            @QueryParam("device_owner") String queryDeviceOwner,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("port_security_enabled") Boolean queryPortSecurityEnabled,
            @QueryParam("qos_policy_id") String queryQosPolicyId,
            // linkTitle
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    // sorting not supported
    ) {
        INeutronPortCRUD portInterface = getNeutronCRUD();
        List<NeutronPort> allPorts = portInterface.getAll();
        List<NeutronPort> ans = new ArrayList<>();
        for (NeutronPort port: allPorts) {
            if ((queryID == null || queryID.equals(port.getID()))
                    && (queryNetworkID == null || queryNetworkID.equals(port.getNetworkUUID()))
                    && (queryName == null || queryName.equals(port.getName()))
                    && (queryAdminStateUp == null || queryAdminStateUp.equals(port.getAdminStateUp()))
                    && (queryMACAddress == null || queryMACAddress.equals(port.getMacAddress()))
                    && (queryDeviceID == null || queryDeviceID.equals(port.getDeviceID()))
                    && (queryDeviceOwner == null || queryDeviceOwner.equals(port.getDeviceOwner()))
                    && (queryTenantID == null || queryTenantID.equals(port.getTenantID()))
                    && (queryPortSecurityEnabled == null
                            || queryPortSecurityEnabled.equals(port.getPortSecurityEnabled()))
                    && (queryQosPolicyId == null || queryQosPolicyId.equals(port.getQosPolicyId()))) {
                if (fields.size() > 0) {
                    ans.add(port.extractFields(fields));
                } else {
                    ans.add(port);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronPortRequest request = (NeutronPortRequest) PaginatedRequestFactory.createRequest(limit, marker,
                    pageReverse, uriInfo, ans, NeutronPort.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronPortRequest(ans)).build();
    }

    /**
     * Returns a specific Port.
     */

    @Path("{portUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showPort(@PathParam("portUUID") String portUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(portUUID, fields);
    }

    /**
     * Creates new Ports.
     */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createPorts(final NeutronPortRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronPort delta, NeutronPort original) {
        /*
         * note: what we would like to get is the complete object as it
         * is known by neutron.  Until then, patch what we *do* get
         * so that we don't lose already known information
         */
        if (delta.getID() == null) {
            delta.setID(uuid);
        }
        if (delta.getTenantID() == null) {
            delta.setTenantID(original.getTenantID());
        }
        if (delta.getNetworkUUID() == null) {
            delta.setNetworkUUID(original.getNetworkUUID());
        }
        if (delta.getMacAddress() == null) {
            delta.setMacAddress(original.getMacAddress());
        }
        if (delta.getFixedIps() == null) {
            delta.setFixedIps(original.getFixedIps());
        }
    }

    /**
     * Updates a Port.
     */

    @Path("{portUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackPorts.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updatePort(@PathParam("portUUID") String portUUID, NeutronPortRequest input) {
        //        TODO: Support change of security groups
        // update the port and return the modified object
        return update(portUUID, input);
    }

    /**
     * Deletes a Port.
     */

    @Path("{portUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deletePort(@PathParam("portUUID") String portUUID) {
        return delete(portUUID);
    }
}
