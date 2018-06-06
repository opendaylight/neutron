/*
 * Copyright (c) 2014, 2015 SDN Hub, LLC. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

import static java.net.HttpURLConnection.HTTP_OK;

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
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.controller.md.sal.common.api.data.ReadFailedException;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for LoadBalancerPool Policies.
 *
 * <p>For now, the LB pool member data is maintained with the INeutronLoadBalancerPoolCRUD,
 * and not duplicated within the INeutronLoadBalancerPoolMemberCRUD's cache.
 */
@Singleton
@Path("/lbaas/pools")
public final class NeutronLoadBalancerPoolNorthbound extends AbstractNeutronNorthbound<NeutronLoadBalancerPool,
        NeutronLoadBalancerPoolRequest, INeutronLoadBalancerPoolCRUD> {

    private static final String RESOURCE_NAME = "LoadBalancerPool";

    private final DataBroker dataBroker;

    @Inject
    public NeutronLoadBalancerPoolNorthbound(
            @OsgiService INeutronLoadBalancerPoolCRUD neutronCRUD,
            @OsgiService DataBroker dataBroker) {
        super(neutronCRUD);
        this.dataBroker = dataBroker;
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all LoadBalancerPool.
     * */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listGroups(
            // return fields
            @QueryParam("fields") List<String> fields,
            // OpenStack LoadBalancerPool attributes
            @QueryParam("id") String queryLoadBalancerPoolID,
            @QueryParam("tenant_id") String queryLoadBalancerPoolTenantID,
            @QueryParam("name") String queryLoadBalancerPoolName,
            @QueryParam("protocol") String queryLoadBalancerProtocol,
            @QueryParam("lb_algorithm") String queryLoadBalancerPoolLbAlgorithm,
            @QueryParam("healthmonitor_id") String queryLoadBalancerPoolHealthMonitorID,
            @QueryParam("admin_state_up") Boolean queryLoadBalancerIsAdminStateUp,
            @QueryParam("members") List<NeutronLoadBalancerPoolMember> queryLoadBalancerPoolMembers,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
        List<NeutronLoadBalancerPool> allLoadBalancerPools = loadBalancerPoolInterface.getAll();
        List<NeutronLoadBalancerPool> ans = new ArrayList<>();
        for (NeutronLoadBalancerPool nsg : allLoadBalancerPools) {
            if ((queryLoadBalancerPoolID == null || queryLoadBalancerPoolID.equals(nsg.getID()))
                    && (queryLoadBalancerPoolTenantID == null
                            || queryLoadBalancerPoolTenantID.equals(nsg.getTenantID()))
                    && (queryLoadBalancerPoolName == null
                            || queryLoadBalancerPoolName.equals(nsg.getName()))
                    && (queryLoadBalancerPoolLbAlgorithm == null
                            || queryLoadBalancerPoolLbAlgorithm.equals(nsg.getLoadBalancerPoolLbAlgorithm()))
                    && (queryLoadBalancerPoolHealthMonitorID == null || queryLoadBalancerPoolHealthMonitorID
                            .equals(nsg.getLoadBalancerPoolHealthMonitorID()))
                    && (queryLoadBalancerIsAdminStateUp == null
                            || queryLoadBalancerIsAdminStateUp.equals(nsg.getLoadBalancerPoolAdminIsStateIsUp()))
                    && (queryLoadBalancerPoolMembers.size() == 0
                            || queryLoadBalancerPoolMembers.equals(nsg.getLoadBalancerPoolMembers()))) {
                if (fields.size() > 0) {
                    ans.add(nsg.extractFields(fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronLoadBalancerPoolRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancerPool.
     */
    @Path("{loadBalancerPoolID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancerPool(@PathParam("loadBalancerPoolID") String loadBalancerPoolID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(loadBalancerPoolID, fields);
    }

    /**
     * Creates new LoadBalancerPool.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerPools(final NeutronLoadBalancerPoolRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancerPool Policy.
     */
    @Path("{loadBalancerPoolID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancerPool(@PathParam("loadBalancerPoolID") String loadBalancerPoolID,
            final NeutronLoadBalancerPoolRequest input) {
        return update(loadBalancerPoolID, input);
    }

    /**
     * Deletes a LoadBalancerPool.
     */
    @Path("{loadBalancerPoolUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerPool(@PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID) {
        return delete(loadBalancerPoolUUID);
    }

    /**
     * Returns a list of all LoadBalancerPoolMembers in specified pool.
     */
    @Path("{loadBalancerPoolUUID}/members")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listMembers(
            //Path param
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,

            // return fields
            @QueryParam("fields") List<String> fields,

            // OpenStack LoadBalancerPool attributes
            @QueryParam("id") String queryLoadBalancerPoolMemberID,
            @QueryParam("tenant_id") String queryLoadBalancerPoolMemberTenantID,
            @QueryParam("address") String queryLoadBalancerPoolMemberAddress,
            @QueryParam("protocol_port") String queryLoadBalancerPoolMemberProtoPort,
            @QueryParam("admin_state_up") Boolean queryLoadBalancerPoolMemberAdminStateUp,
            @QueryParam("weight") Integer queryLoadBalancerPoolMemberWeight,
            @QueryParam("subnet_id") String queryLoadBalancerPoolMemberSubnetID,

            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
    // sorting not supported
    ) {
        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
        try {
            try (ReadOnlyTransaction tx = dataBroker.newReadOnlyTransaction()) {
                if (!loadBalancerPoolInterface.exists(loadBalancerPoolUUID, tx)) {
                    throw new ResourceNotFoundException(uuidNoExist());
                }
            }
            List<NeutronLoadBalancerPoolMember> members = loadBalancerPoolInterface.get(loadBalancerPoolUUID)
                    .getLoadBalancerPoolMembers();
            List<NeutronLoadBalancerPoolMember> ans = new ArrayList<>();
            for (NeutronLoadBalancerPoolMember nsg : members) {
                if ((queryLoadBalancerPoolMemberID == null || queryLoadBalancerPoolMemberID.equals(nsg.getID()))
                        && loadBalancerPoolUUID.equals(nsg.getPoolID())
                        && (queryLoadBalancerPoolMemberTenantID == null
                                || queryLoadBalancerPoolMemberTenantID.equals(nsg.getTenantID()))
                        && (queryLoadBalancerPoolMemberAddress == null
                                || queryLoadBalancerPoolMemberAddress.equals(nsg.getPoolMemberAddress()))
                        && (queryLoadBalancerPoolMemberAdminStateUp == null
                                || queryLoadBalancerPoolMemberAdminStateUp.equals(nsg.getPoolMemberAdminStateIsUp()))
                        && (queryLoadBalancerPoolMemberWeight == null
                                || queryLoadBalancerPoolMemberWeight.equals(nsg.getPoolMemberWeight()))
                        && (queryLoadBalancerPoolMemberSubnetID == null
                                || queryLoadBalancerPoolMemberSubnetID.equals(nsg.getPoolMemberSubnetID()))) {
                    if (fields.size() > 0) {
                        ans.add(nsg.extractFields(fields));
                    } else {
                        ans.add(nsg);
                    }
                }
            }
            return Response.status(HTTP_OK).entity(new NeutronLoadBalancerPoolMemberRequest(ans)).build();
        } catch (ReadFailedException e) {
            throw new DatastoreOperationFailedWebApplicationException(e);
        }
    }

    /**
     * Returns a specific LoadBalancerPoolMember.
     */
    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackLoadBalancerPoolMembers.class)
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancerPoolMember(@PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {

        try {
            INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
            try (ReadOnlyTransaction tx = dataBroker.newReadOnlyTransaction()) {
                if (!loadBalancerPoolInterface.exists(loadBalancerPoolUUID, tx)) {
                    throw new ResourceNotFoundException(uuidNoExist());
                }
            }
            List<NeutronLoadBalancerPoolMember> members = loadBalancerPoolInterface.get(loadBalancerPoolUUID)
                    .getLoadBalancerPoolMembers();
            for (NeutronLoadBalancerPoolMember ans : members) {
                if (!ans.getID().equals(loadBalancerPoolMemberUUID)) {
                    continue;
                }

                if (fields.size() > 0) {
                    return Response.status(HttpURLConnection.HTTP_OK)
                            .entity(new NeutronLoadBalancerPoolMemberRequest(ans.extractFields(fields))).build();
                } else {
                    return Response.status(HttpURLConnection.HTTP_OK)
                            .entity(new NeutronLoadBalancerPoolMemberRequest(ans)).build();
                }
            }
            throw new ResourceNotFoundException(uuidNoExist());
        } catch (ReadFailedException e) {
            throw new DatastoreOperationFailedWebApplicationException(e);
        }
    }

    /**
     * Adds a Member to an LBaaS Pool member.
     */
    @Path("{loadBalancerPoolUUID}/members")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerPoolMember(@PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            final NeutronLoadBalancerPoolMemberRequest input) {
        try {
            INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();

            if (input.isSingleton()) {
                NeutronLoadBalancerPoolMember singleton = input.getSingleton();
                singleton.setPoolID(loadBalancerPoolUUID);
                /**
                 * Add the member from the neutron load balancer pool as well
                 */

                loadBalancerPoolInterface.addNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, singleton);
            } else {
                /*
                 * now, each element of the bulk request can be added to the cache
                 */
                for (NeutronLoadBalancerPoolMember test : input.getBulk()) {
                    loadBalancerPoolInterface.addNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, test);
                }
            }
            return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
        } catch (OperationFailedException e) {
            throw new DatastoreOperationFailedWebApplicationException(e);
        }
    }

    /**
     * Updates a LB member pool.
     */
    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found") })
    public Response updateLoadBalancerPoolMember(@PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID,
            final NeutronLoadBalancerPoolMemberRequest input) {
        try {
            INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
            NeutronLoadBalancerPool singletonPool = loadBalancerPoolInterface.get(loadBalancerPoolUUID);
            NeutronLoadBalancerPoolMember singleton = input.getSingleton();
            singleton.setPoolID(loadBalancerPoolUUID);

            if (singletonPool == null) {
                throw new ResourceNotFoundException("Pool doesn't Exist");
            }
            loadBalancerPoolInterface.updateNeutronLoadBalancerPoolMember(loadBalancerPoolUUID,
                    loadBalancerPoolMemberUUID, singleton);
            return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
        } catch (OperationFailedException e) {
            throw new DatastoreOperationFailedWebApplicationException(e);
        }
    }

    /**
     * Deletes a LoadBalancerPoolMember.
     */
    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerPoolMember(@PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID) {
        try {
            INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
            // Verify that the LB pool member exists
            NeutronLoadBalancerPoolMember singleton = null;
            List<NeutronLoadBalancerPoolMember> members = loadBalancerPoolInterface.get(loadBalancerPoolUUID)
                    .getLoadBalancerPoolMembers();
            for (NeutronLoadBalancerPoolMember member : members) {
                if (member.getID().equals(loadBalancerPoolMemberUUID)) {
                    singleton = member;
                    break;
                }
            }
            if (singleton == null) {
                throw new BadRequestException("LoadBalancerPoolMember UUID does not exist.");
            }

            /**
             * Remove the member from the neutron load balancer pool
             */
            loadBalancerPoolInterface.removeNeutronLoadBalancerPoolMember(loadBalancerPoolUUID,
                    loadBalancerPoolMemberUUID);

            return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
        } catch (OperationFailedException e) {
            throw new DatastoreOperationFailedWebApplicationException(e);
        }
    }
}
