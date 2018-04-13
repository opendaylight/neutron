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
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronSubnet;

/**
 * Neutron Northbound REST APIs for Subnets.<br>
 */
@Path("/subnets")
public final class NeutronSubnetsNorthbound
        extends AbstractNeutronNorthbound<NeutronSubnet, NeutronSubnetRequest, INeutronSubnetCRUD> {

    private static final String RESOURCE_NAME = "Subnet";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all Subnets.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSubnets(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("network_id") String queryNetworkID,
            @QueryParam("name") String queryName,
            @QueryParam("ip_version") Integer queryIPVersion,
            @QueryParam("cidr") String queryCIDR,
            @QueryParam("gateway_ip") String queryGatewayIp,
            @QueryParam("enable_dhcp") Boolean queryEnableDHCP,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("ipv6_address_mode") String queryIpV6AddressMode,
            @QueryParam("ipv6_ra_mode") String queryIpV6RaMode,
            // linkTitle
            @QueryParam("limit") Integer limit,
            @QueryParam("marker") String marker,
            @DefaultValue("false") @QueryParam("page_reverse") Boolean pageReverse
    // sorting not supported
    ) {
        INeutronSubnetCRUD subnetInterface = getNeutronCRUD();
        List<NeutronSubnet> allSubnets = subnetInterface.getAll();
        List<NeutronSubnet> ans = new ArrayList<>();
        for (NeutronSubnet subnet : allSubnets) {
            if ((queryID == null || queryID.equals(subnet.getID()))
                    && (queryNetworkID == null || queryNetworkID.equals(subnet.getNetworkUUID()))
                    && (queryName == null || queryName.equals(subnet.getName()))
                    && (queryIPVersion == null || queryIPVersion.equals(subnet.getIpVersion()))
                    && (queryCIDR == null || queryCIDR.equals(subnet.getCidr()))
                    && (queryGatewayIp == null || queryGatewayIp.equals(subnet.getGatewayIp()))
                    && (queryEnableDHCP == null || queryEnableDHCP.equals(subnet.getEnableDHCP()))
                    && (queryTenantID == null || queryTenantID.equals(subnet.getTenantID()))
                    && (queryIpV6AddressMode == null || queryIpV6AddressMode.equals(subnet.getIpV6AddressMode()))
                    && (queryIpV6RaMode == null || queryIpV6RaMode.equals(subnet.getIpV6RaMode()))) {
                if (fields.size() > 0) {
                    ans.add(subnet.extractFields(fields));
                } else {
                    ans.add(subnet);
                }
            }
        }

        if (limit != null && ans.size() > 1) {
            // Return a paginated request
            NeutronSubnetRequest request = (NeutronSubnetRequest) PaginatedRequestFactory.createRequest(limit, marker,
                    pageReverse, uriInfo, ans, NeutronSubnet.class);
            return Response.status(HttpURLConnection.HTTP_OK).entity(request).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSubnetRequest(ans)).build();
    }

    /**
     * Returns a specific Subnet.
     */
    @Path("{subnetUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSubnet(@PathParam("subnetUUID") String subnetUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(subnetUUID, fields);
    }

    /**
     * Creates new Subnets.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSubnets(final NeutronSubnetRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSubnet delta, NeutronSubnet original) {
        /*
         * note: what we get appears to not be a delta, but rather a
         * complete updated object.  So, that needs to be sent down to
         * folks to check
         */
        delta.setID(uuid);
        delta.setNetworkUUID(original.getNetworkUUID());
        delta.setTenantID(original.getTenantID());
        delta.setIpVersion(original.getIpVersion());
        delta.setCidr(original.getCidr());
    }

    /**
     * Updates a Subnet.
     */
    @Path("{subnetUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSubnet(@PathParam("subnetUUID") String subnetUUID, final NeutronSubnetRequest input) {
        return update(subnetUUID, input);
    }

    /**
     * Deletes a Subnet.
     */
    @Path("{subnetUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSubnet(@PathParam("subnetUUID") String subnetUUID) {
        return delete(subnetUUID);
    }
}
