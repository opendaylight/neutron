/*
 * Copyright (c) 2014, 2015 SDN Hub, LLC. and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberAware;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPool;
import org.opendaylight.neutron.spi.NeutronLoadBalancerPoolMember;

/**
 * Neutron Northbound REST APIs for LoadBalancerPool Policies.<br>
 * This class provides REST APIs for managing neutron LoadBalancerPool Policies
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

/**
 * For now, the LB pool member data is maintained with the INeutronLoadBalancerPoolCRUD,
 * and not duplicated within the INeutronLoadBalancerPoolMemberCRUD's cache.
 */

@Path("/lbaas/pools")
public class NeutronLoadBalancerPoolNorthbound
    extends AbstractNeutronNorthboundIAware<NeutronLoadBalancerPool, NeutronLoadBalancerPoolRequest, INeutronLoadBalancerPoolCRUD, INeutronLoadBalancerPoolAware> {

    private static final String RESOURCE_NAME = "LoadBalancerPool";

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    @Override
    protected NeutronLoadBalancerPool extractFields(NeutronLoadBalancerPool o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Override
    protected NeutronLoadBalancerPoolRequest newNeutronRequest(NeutronLoadBalancerPool o) {
        return new NeutronLoadBalancerPoolRequest(o);
    }

    @Override
    protected INeutronLoadBalancerPoolCRUD getNeutronCRUD() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().
            fetchINeutronLoadBalancerPoolCRUD(this);
        if (answer.getLoadBalancerPoolInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable());
        }
        return answer.getLoadBalancerPoolInterface();
    }

    @Override
    protected Object[] getInstances() {
        return NeutronUtil.getInstances(INeutronLoadBalancerPoolAware.class, this);
    }

    private Object[] getInstancesOfPoolMemberAware() {
        return NeutronUtil.getInstances(INeutronLoadBalancerPoolMemberAware.class, this);
    }

    @Override
    protected int canCreate(Object instance, NeutronLoadBalancerPool singleton) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        return service.canCreateNeutronLoadBalancerPool(singleton);
    }

    @Override
    protected void created(Object instance, NeutronLoadBalancerPool singleton) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        service.neutronLoadBalancerPoolCreated(singleton);
    }

    @Override
    protected int canUpdate(Object instance, NeutronLoadBalancerPool delta, NeutronLoadBalancerPool original) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        return service.canUpdateNeutronLoadBalancerPool(delta, original);
    }

    @Override
    protected void updated(Object instance, NeutronLoadBalancerPool updated) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        service.neutronLoadBalancerPoolUpdated(updated);
    }

    @Override
    protected int canDelete(Object instance, NeutronLoadBalancerPool singleton) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        return service.canDeleteNeutronLoadBalancerPool(singleton);
    }

    @Override
    protected void deleted(Object instance, NeutronLoadBalancerPool singleton) {
        INeutronLoadBalancerPoolAware service = (INeutronLoadBalancerPoolAware) instance;
        service.neutronLoadBalancerPoolDeleted(singleton);
    }

    /**
     * Returns a list of all LoadBalancerPool
     * */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        List<NeutronLoadBalancerPool> allLoadBalancerPools = loadBalancerPoolInterface.getAllNeutronLoadBalancerPools();
        List<NeutronLoadBalancerPool> ans = new ArrayList<NeutronLoadBalancerPool>();
        Iterator<NeutronLoadBalancerPool> i = allLoadBalancerPools.iterator();
        while (i.hasNext()) {
            NeutronLoadBalancerPool nsg = i.next();
            if ((queryLoadBalancerPoolID == null ||
                    queryLoadBalancerPoolID.equals(nsg.getID())) &&
                    (queryLoadBalancerPoolTenantID == null ||
                            queryLoadBalancerPoolTenantID.equals(nsg.getTenantID())) &&
                    (queryLoadBalancerPoolName == null ||
                            queryLoadBalancerPoolName.equals(nsg.getLoadBalancerPoolName())) &&
                    (queryLoadBalancerPoolLbAlgorithm == null ||
                            queryLoadBalancerPoolLbAlgorithm.equals(nsg.getLoadBalancerPoolLbAlgorithm())) &&
                    (queryLoadBalancerPoolHealthMonitorID == null ||
                            queryLoadBalancerPoolHealthMonitorID.equals(nsg.getNeutronLoadBalancerPoolHealthMonitorID())) &&
                    (queryLoadBalancerIsAdminStateUp == null ||
                            queryLoadBalancerIsAdminStateUp.equals(nsg.getLoadBalancerPoolAdminIsStateIsUp())) &&
                    (queryLoadBalancerPoolMembers.size() == 0 ||
                            queryLoadBalancerPoolMembers.equals(nsg.getLoadBalancerPoolMembers()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg,fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronLoadBalancerPoolRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancerPool */

    @Path("{loadBalancerPoolID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
     * Creates new LoadBalancerPool */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerPools(final NeutronLoadBalancerPoolRequest input) {
        return create(input);
    }

    /**
     * Updates a LoadBalancerPool Policy
     */
    @Path("{loadBalancerPoolID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateLoadBalancerPool(
            @PathParam("loadBalancerPoolID") String loadBalancerPoolID, final NeutronLoadBalancerPoolRequest input) {
        return update(loadBalancerPoolID, input);
    }

    /**
     * Deletes a LoadBalancerPool
     */

    @Path("{loadBalancerPoolUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerPool(
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID) {
        return delete(loadBalancerPoolUUID);
    }

    protected NeutronLoadBalancerPoolMember extractFields(NeutronLoadBalancerPoolMember o, List<String> fields) {
        return o.extractFields(fields);
    }

    /**
     * Returns a list of all LoadBalancerPoolMembers in specified pool
     */
    @Path("{loadBalancerPoolUUID}/members")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        if (!loadBalancerPoolInterface.neutronLoadBalancerPoolExists(loadBalancerPoolUUID)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        List<NeutronLoadBalancerPoolMember> members =
                    loadBalancerPoolInterface.getNeutronLoadBalancerPool(loadBalancerPoolUUID).getLoadBalancerPoolMembers();
        List<NeutronLoadBalancerPoolMember> ans = new ArrayList<NeutronLoadBalancerPoolMember>();
        Iterator<NeutronLoadBalancerPoolMember> i = members.iterator();
        while (i.hasNext()) {
            NeutronLoadBalancerPoolMember nsg = i.next();
            if ((queryLoadBalancerPoolMemberID == null ||
                queryLoadBalancerPoolMemberID.equals(nsg.getID())) &&
                loadBalancerPoolUUID.equals(nsg.getPoolID()) &&
                (queryLoadBalancerPoolMemberTenantID == null ||
                        queryLoadBalancerPoolMemberTenantID.equals(nsg.getTenantID())) &&
                (queryLoadBalancerPoolMemberAddress == null ||
                        queryLoadBalancerPoolMemberAddress.equals(nsg.getPoolMemberAddress())) &&
                (queryLoadBalancerPoolMemberAdminStateUp == null ||
                        queryLoadBalancerPoolMemberAdminStateUp.equals(nsg.getPoolMemberAdminStateIsUp())) &&
                (queryLoadBalancerPoolMemberWeight == null ||
                        queryLoadBalancerPoolMemberWeight.equals(nsg.getPoolMemberWeight())) &&
                (queryLoadBalancerPoolMemberSubnetID == null ||
                        queryLoadBalancerPoolMemberSubnetID.equals(nsg.getPoolMemberSubnetID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg, fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronLoadBalancerPoolMemberRequest(ans)).build();
    }

    /**
     * Returns a specific LoadBalancerPoolMember
     */
    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackLoadBalancerPoolMembers.class)
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showLoadBalancerPoolMember(
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID,
            // return fields
            @QueryParam("fields") List<String> fields ) {

        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
        if (!loadBalancerPoolInterface.neutronLoadBalancerPoolExists(loadBalancerPoolUUID)) {
            throw new ResourceNotFoundException(uuidNoExist());
        }
        List<NeutronLoadBalancerPoolMember> members =
                    loadBalancerPoolInterface.getNeutronLoadBalancerPool(loadBalancerPoolUUID).getLoadBalancerPoolMembers();
        for (NeutronLoadBalancerPoolMember ans: members) {
            if (!ans.getID().equals(loadBalancerPoolMemberUUID)) {
                continue;
            }

            if (fields.size() > 0) {
                return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronLoadBalancerPoolMemberRequest(extractFields(ans, fields))).build();
            } else {
                return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronLoadBalancerPoolMemberRequest(ans)).build();
            }
        }
        throw new ResourceNotFoundException(uuidNoExist());
    }

    /**
     * Adds a Member to an LBaaS Pool member
     */
    @Path("{loadBalancerPoolUUID}/members")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createLoadBalancerPoolMember(
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            final NeutronLoadBalancerPoolMemberRequest input) {

        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
        NeutronLoadBalancerPool singletonPool = loadBalancerPoolInterface.getNeutronLoadBalancerPool(loadBalancerPoolUUID);

        if (input.isSingleton()) {
            NeutronLoadBalancerPoolMember singleton = input.getSingleton();
            singleton.setPoolID(loadBalancerPoolUUID);

            Object[] instances = getInstancesOfPoolMemberAware();
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                    int status = service.canCreateNeutronLoadBalancerPoolMember(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            }

            if (instances != null) {
                for (Object instance : instances) {
                    INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                    service.neutronLoadBalancerPoolMemberCreated(singleton);
                }
            }

            /**
             * Add the member from the neutron load balancer pool as well
             */

            loadBalancerPoolInterface.addNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, singleton);
        } else {
            Object[] instances = getInstancesOfPoolMemberAware();
            if (instances != null) {
                for (NeutronLoadBalancerPoolMember test : input.getBulk()) {
                    for (Object instance : instances) {
                        INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                        int status = service.canCreateNeutronLoadBalancerPoolMember(test);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                }
            }
            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (NeutronLoadBalancerPoolMember test : input.getBulk()) {
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                        service.neutronLoadBalancerPoolMemberCreated(test);
                    }
                }
                loadBalancerPoolInterface.addNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, test);
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a LB member pool
     */

    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found") })
    public Response updateLoadBalancerPoolMember(
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID,
            final NeutronLoadBalancerPoolMemberRequest input) {
        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();
        NeutronLoadBalancerPool singletonPool = loadBalancerPoolInterface.getNeutronLoadBalancerPool(loadBalancerPoolUUID);
        NeutronLoadBalancerPoolMember singleton = input.getSingleton();
        singleton.setPoolID(loadBalancerPoolUUID);

        if (singletonPool == null) {
            throw new ResourceNotFoundException("Pool doesn't Exist");
        }
        NeutronLoadBalancerPoolMember original = singletonPool.getNeutronLoadBalancerPoolMember(loadBalancerPoolMemberUUID);

        Object[] instances = getInstancesOfPoolMemberAware();
        if (instances != null) {
            for (Object instance : instances) {
                INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                int status = service.canUpdateNeutronLoadBalancerPoolMember(singleton, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        loadBalancerPoolInterface.updateNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, loadBalancerPoolMemberUUID, singleton);

        if (instances != null) {
            for (Object instance : instances) {
                INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                service.neutronLoadBalancerPoolMemberUpdated(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
    }

    /**
     * Deletes a LoadBalancerPoolMember
     */

    @Path("{loadBalancerPoolUUID}/members/{loadBalancerPoolMemberUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteLoadBalancerPoolMember(
            @PathParam("loadBalancerPoolUUID") String loadBalancerPoolUUID,
            @PathParam("loadBalancerPoolMemberUUID") String loadBalancerPoolMemberUUID) {
        INeutronLoadBalancerPoolCRUD loadBalancerPoolInterface = getNeutronCRUD();

        //Verify that the LB pool member exists
        NeutronLoadBalancerPoolMember singleton = null;
        List<NeutronLoadBalancerPoolMember> members =
                loadBalancerPoolInterface.getNeutronLoadBalancerPool(loadBalancerPoolUUID).getLoadBalancerPoolMembers();
        for (NeutronLoadBalancerPoolMember member: members) {
            if (member.getID().equals(loadBalancerPoolMemberUUID)) {
                singleton = member;
                break;
            }
        }
        if (singleton == null) {
            throw new BadRequestException("LoadBalancerPoolMember UUID does not exist.");
        }

        Object[] instances = getInstancesOfPoolMemberAware();
        if (instances != null) {
            for (Object instance : instances) {
                INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                int status = service.canDeleteNeutronLoadBalancerPoolMember(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        if (instances != null) {
            for (Object instance : instances) {
                INeutronLoadBalancerPoolMemberAware service = (INeutronLoadBalancerPoolMemberAware) instance;
                service.neutronLoadBalancerPoolMemberDeleted(singleton);
            }
        }

        /**
         * Remove the member from the neutron load balancer pool
         */
        loadBalancerPoolInterface.removeNeutronLoadBalancerPoolMember(loadBalancerPoolUUID, loadBalancerPoolMemberUUID);

        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
