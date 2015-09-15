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
import org.opendaylight.neutron.spi.INeutronSecurityGroupAware;
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
@Path ("/security-groups")
public class NeutronSecurityGroupsNorthbound {
    private static final int HTTP_OK_BOTTOM = 200;
    private static final int HTTP_OK_TOP = 299;
    private static final String INTERFACE_NAME = "Security Group CRUD Interface";
    private static final String UUID_NO_EXIST = "Security Group UUID does not exist.";
    private static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    private static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";

    private NeutronSecurityGroup extractFields(NeutronSecurityGroup o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronSecurityGroupCRUD(this);
        if (answer.getSecurityGroupInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    /**
     * Returns a list of all Security Groups
     */
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })

    public Response listGroups(
            // return fields
            @QueryParam ("fields") List<String> fields,
            // OpenStack security group attributes
            @QueryParam ("id") String querySecurityGroupUUID,
            @QueryParam ("name") String querySecurityGroupName,
            @QueryParam ("description") String querySecurityDescription,
            @QueryParam ("tenant_id") String querySecurityTenantID,
            @QueryParam ("limit") String limit,
            @QueryParam ("marker") String marker,
            @QueryParam ("page_reverse") String pageReverse
    ) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronInterfaces().getSecurityGroupInterface();
        List<NeutronSecurityGroup> allSecurityGroups = securityGroupInterface.getAllNeutronSecurityGroups();
        List<NeutronSecurityGroup> ans = new ArrayList<NeutronSecurityGroup>();
        Iterator<NeutronSecurityGroup> i = allSecurityGroups.iterator();
        while (i.hasNext()) {
            NeutronSecurityGroup nsg = i.next();
            if ((querySecurityGroupUUID == null ||
                    querySecurityGroupUUID.equals(nsg.getID())) &&
                    (querySecurityGroupName == null ||
                            querySecurityGroupName.equals(nsg.getSecurityGroupName())) &&
                    (querySecurityDescription == null ||
                            querySecurityDescription.equals(nsg.getSecurityGroupDescription())) &&
                    (querySecurityTenantID == null ||
                            querySecurityTenantID.equals(nsg.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg, fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronSecurityGroupRequest(ans)).build();
    }

    /**
     * Returns a specific Security Group
     */

    @Path ("{securityGroupUUID}")
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode (code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSecurityGroup(@PathParam ("securityGroupUUID") String securityGroupUUID,
                                      // return fields
                                      @QueryParam ("fields") List<String> fields) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronInterfaces().getSecurityGroupInterface();
        if (!securityGroupInterface.neutronSecurityGroupExists(securityGroupUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (!fields.isEmpty()) {
            NeutronSecurityGroup ans = securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronSecurityGroupRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityGroupRequest(securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID))).build();
        }
    }

    /**
     * Creates new Security Group
     */

    @POST
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSecurityGroups(final NeutronSecurityGroupRequest input) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronInterfaces().getSecurityGroupInterface();

        if (input.isSingleton()) {
            NeutronSecurityGroup singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronSecurityGroupAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                        int status = service.canCreateNeutronSecurityGroup(singleton);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDERS);
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDER_LIST);
            }
            // Add to Neutron cache
            securityGroupInterface.addNeutronSecurityGroup(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                    service.neutronSecurityGroupCreated(singleton);
                }
            }
        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronSecurityGroupAware.class, this);
            for (NeutronSecurityGroup test : input.getBulk()) {
                if (instances != null) {
                    if (instances.length > 0) {
                        for (Object instance : instances) {
                            INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                            int status = service.canCreateNeutronSecurityGroup(test);
                            if ((status < HTTP_OK_BOTTOM) || (status > HTTP_OK_TOP)) {
                                return Response.status(status).build();
                            }
                        }
                    } else {
                        throw new BadRequestException(NO_PROVIDERS);
                    }
                } else {
                    throw new ServiceUnavailableException(NO_PROVIDER_LIST);
                }
            }

            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (NeutronSecurityGroup test : input.getBulk()) {
                securityGroupInterface.addNeutronSecurityGroup(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                        service.neutronSecurityGroupCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Security Group
     */

    @Path ("{securityGroupUUID}")
    @PUT
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSecurityGroup(
            @PathParam ("securityGroupUUID") String securityGroupUUID, final NeutronSecurityGroupRequest input) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronInterfaces().getSecurityGroupInterface();

        NeutronSecurityGroup delta = input.getSingleton();
        NeutronSecurityGroup original = securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID);

        Object[] instances =  NeutronUtil.getInstances(INeutronSecurityGroupAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                    int status = service.canUpdateNeutronSecurityGroup(delta, original);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * update the object and return it
         */
        securityGroupInterface.updateNeutronSecurityGroup(securityGroupUUID, delta);
        NeutronSecurityGroup updatedSecurityGroup = securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                service.neutronSecurityGroupUpdated(updatedSecurityGroup);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSecurityGroupRequest(securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID))).build();
    }

    /**
     * Deletes a Security Group
     */

    @Path ("{securityGroupUUID}")
    @DELETE
    @StatusCodes ({
            @ResponseCode (code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSecurityGroup(
            @PathParam ("securityGroupUUID") String securityGroupUUID) {
        INeutronSecurityGroupCRUD securityGroupInterface = getNeutronInterfaces().getSecurityGroupInterface();

        NeutronSecurityGroup singleton = securityGroupInterface.getNeutronSecurityGroup(securityGroupUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronSecurityGroupAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                    int status = service.canDeleteNeutronSecurityGroup(singleton);
                    if ((status < HTTP_OK_BOTTOM) || (status > HTTP_OK_TOP)) {
                        return Response.status(status).build();
                    }
                }
            } else {
                throw new ServiceUnavailableException(NO_PROVIDERS);
            }
        } else {
            throw new ServiceUnavailableException(NO_PROVIDER_LIST);
        }

        /*
         * remove it and return 204 status
         */
        securityGroupInterface.removeNeutronSecurityGroup(securityGroupUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronSecurityGroupAware service = (INeutronSecurityGroupAware) instance;
                service.neutronSecurityGroupDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
