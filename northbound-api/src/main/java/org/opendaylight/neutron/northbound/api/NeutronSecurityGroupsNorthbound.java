/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronSecurityGroup;

/**
 * Neutron Northbound REST APIs for Security Group.<br>
 * This class provides REST APIs for managing neutron Security Group
 * <p>
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
 */
@Path("/security-groups")
public class NeutronSecurityGroupsNorthbound extends
        AbstractNeutronNorthbound<NeutronSecurityGroup, NeutronSecurityGroupRequest, INeutronSecurityGroupCRUD> {
    private static final String RESOURCE_NAME = "Security Group";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronSecurityGroup extractFields(NeutronSecurityGroup o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronSecurityGroupRequest newNeutronRequest(NeutronSecurityGroup o) {
        return new NeutronSecurityGroupRequest(o);
    }

    @Override
    protected INeutronSecurityGroupCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSecurityGroupCRUD(this);
        if (answer.getSecurityGroupInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getSecurityGroupInterface();
    }

    /**
     * Returns a list of all Security Groups
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })

    public Response listGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack security group attributes
            @QueryParam("id") String querySecurityGroupUUID,
            @QueryParam("name") String querySecurityGroupName,
            @QueryParam("tenant_id") String querySecurityTenantID,
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronCRUD();
        List<NeutronSecurityGroup> allSecurityGroups = securityGroupInterface.getAll();
        List<NeutronSecurityGroup> ans = new ArrayList<>();
        Iterator<NeutronSecurityGroup> i = allSecurityGroups.iterator();
        while (i.hasNext()) {
            NeutronSecurityGroup nsg = i.next();
            if ((querySecurityGroupUUID == null || querySecurityGroupUUID.equals(nsg.getID()))
                    && (querySecurityGroupName == null || querySecurityGroupName.equals(nsg.getSecurityGroupName()))
                    && (querySecurityTenantID == null || querySecurityTenantID.equals(nsg.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg, fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityGroupRequest(ans)).build();
    }

    /**
     * Returns a specific Security Group
     */

    @Path("{securityGroupUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSecurityGroup(@PathParam("securityGroupUUID") String securityGroupUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(securityGroupUUID, fields);
    }

    /**
     * Creates new Security Group
     */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSecurityGroups(final NeutronSecurityGroupRequest input) {
        return create(input);
    }

    /**
     * Updates a Security Group
     */

    @Path("{securityGroupUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSecurityGroup(@PathParam("securityGroupUUID") String securityGroupUUID,
            final NeutronSecurityGroupRequest input) {
        return update(securityGroupUUID, input);
    }

    /**
     * Deletes a Security Group
     */

    @Path("{securityGroupUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSecurityGroup(@PathParam("securityGroupUUID") String securityGroupUUID) {
        return delete(securityGroupUUID);
    }
}
