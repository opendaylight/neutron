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
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFloatingIP;

/**
 * Neutron Northbound REST APIs.<br>
 * This class provides REST APIs for managing Neutron Floating IPs
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

@Path("/floatingips")
public class NeutronFloatingIPsNorthbound
        extends AbstractNeutronNorthbound<NeutronFloatingIP, NeutronFloatingIPRequest, INeutronFloatingIPCRUD> {
    private static final String RESOURCE_NAME = "Floating IP";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronFloatingIP extractFields(NeutronFloatingIP o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronFloatingIPRequest newNeutronRequest(NeutronFloatingIP o) {
        return new NeutronFloatingIPRequest(o);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces(boolean flag) {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronFloatingIPCRUD(this);
        if (answer.getFloatingIPInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        if (flag) {
            answer = answer.fetchINeutronNetworkCRUD(this).fetchINeutronSubnetCRUD(this).fetchINeutronPortCRUD(this);
            if (answer.getNetworkInterface() == null) {
                throw new ServiceUnavailableException(
                        "Network CRUD Interface " + RestMessages.SERVICEUNAVAILABLE.toString());
            }
            if (answer.getSubnetInterface() == null) {
                throw new ServiceUnavailableException(
                        "Subnet CRUD Interface " + RestMessages.SERVICEUNAVAILABLE.toString());
            }
            if (answer.getPortInterface() == null) {
                throw new ServiceUnavailableException(
                        "Port CRUD Interface " + RestMessages.SERVICEUNAVAILABLE.toString());
            }
        }
        return answer;
    }

    @Override
    protected INeutronFloatingIPCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = getNeutronInterfaces(false);
        return answer.getFloatingIPInterface();
    }

    /**
     * Returns a list of all FloatingIPs */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listFloatingIPs(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("floating_network_id") String queryFloatingNetworkId,
            @QueryParam("port_id") String queryPortId,
            @QueryParam("fixed_ip_address") String queryFixedIPAddress,
            @QueryParam("floating_ip_address") String queryFloatingIPAddress,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("router_id") String queryRouterID,
            @QueryParam("status") String queryStatus,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronFloatingIPCRUD floatingIPInterface = getNeutronInterfaces(false).getFloatingIPInterface();
        List<NeutronFloatingIP> allFloatingIPs = floatingIPInterface.getAll();
        List<NeutronFloatingIP> ans = new ArrayList<>();
        Iterator<NeutronFloatingIP> i = allFloatingIPs.iterator();
        while (i.hasNext()) {
            NeutronFloatingIP oSS = i.next();
            //match filters: TODO provider extension and router extension
            if ((queryID == null || queryID.equals(oSS.getID()))
                    && (queryFloatingNetworkId == null || queryFloatingNetworkId.equals(oSS.getFloatingNetworkUUID()))
                    && (queryPortId == null || queryPortId.equals(oSS.getPortUUID()))
                    && (queryFixedIPAddress == null || queryFixedIPAddress.equals(oSS.getFixedIPAddress()))
                    && (queryFloatingIPAddress == null || queryFloatingIPAddress.equals(oSS.getFloatingIPAddress()))
                    && (queryStatus == null || queryStatus.equals(oSS.getStatus()))
                    && (queryRouterID == null || queryRouterID.equals(oSS.getRouterUUID()))
                    && (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS, fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFloatingIPRequest(ans)).build();
    }

    /**
     * Returns a specific FloatingIP */

    @Path("{floatingipUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFloatingIP(@PathParam("floatingipUUID") String floatingipUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(floatingipUUID, fields);
    }

    /**
     * Creates new FloatingIPs */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFloatingIPs(final NeutronFloatingIPRequest input) {
        return create(input);
    }

    /**
     * Updates a FloatingIP */

    @Path("{floatingipUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFloatingIP(@PathParam("floatingipUUID") String floatingipUUID,
            NeutronFloatingIPRequest input) {
        return update(floatingipUUID, input);
    }

    /**
     * Deletes a FloatingIP */

    @Path("{floatingipUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFloatingIP(@PathParam("floatingipUUID") String floatingipUUID) {
        return delete(floatingipUUID);
    }
}
