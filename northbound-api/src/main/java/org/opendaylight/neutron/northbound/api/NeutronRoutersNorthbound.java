/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronRouterAware;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronRouter;
import org.opendaylight.neutron.spi.NeutronRouter_Interface;
import org.opendaylight.neutron.spi.NeutronSubnet;


/**
 * Neutron Northbound REST APIs.<br>
 * This class provides REST APIs for managing neutron routers
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

@Path("/routers")
public class NeutronRoutersNorthbound {
    static final String ROUTER_INTERFACE_STR = "network:router_interface";
    static final String ROUTER_GATEWAY_STR = "network:router_gateway";
    private static final int HTTP_OK_BOTTOM = 200;
    private static final int HTTP_OK_TOP = 299;
    private static final String INTERFACE_NAME = "Router CRUD Interface";
    private static final String UUID_NO_EXIST = "Router UUID does not exist.";
    private static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    private static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";

    private NeutronRouter extractFields(NeutronRouter o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces(boolean flag) {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronRouterCRUD(this);
        if (answer.getRouterInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (flag) {
            answer = answer.fetchINeutronNetworkCRUD(this);
            if (answer.getNetworkInterface() == null) {
                throw new ServiceUnavailableException("Network CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
            }
        }
        return answer;
    }

    private NeutronCRUDInterfaces getAttachInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronRouterCRUD(this);
        if (answer.getRouterInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        answer = answer.fetchINeutronPortCRUD(this).fetchINeutronSubnetCRUD(this);
        if (answer.getPortInterface() == null) {
            throw new ServiceUnavailableException("Port CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (answer.getSubnetInterface() == null) {
            throw new ServiceUnavailableException("Subnet CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    /**
     * Returns a list of all Routers */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listRouters(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("admin_state_up") String queryAdminStateUp,
            @QueryParam("status") String queryStatus,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("external_gateway_info") String queryExternalGatewayInfo,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
            // sorting not supported
            ) {
        INeutronRouterCRUD routerInterface = getNeutronInterfaces(false).getRouterInterface();
        if (routerInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronRouter> allRouters = routerInterface.getAllRouters();
        List<NeutronRouter> ans = new ArrayList<NeutronRouter>();
        Iterator<NeutronRouter> i = allRouters.iterator();
        while (i.hasNext()) {
            NeutronRouter oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getName())) &&
                    (queryAdminStateUp == null || queryAdminStateUp.equals(oSS.getAdminStateUp())) &&
                    (queryStatus == null || queryStatus.equals(oSS.getStatus())) &&
                    (queryExternalGatewayInfo == null || queryExternalGatewayInfo.equals(oSS.getExternalGatewayInfo())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS,fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronRouterRequest(ans)).build();
    }

    /**
     * Returns a specific Router */

    @Path("{routerUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showRouter(
            @PathParam("routerUUID") String routerUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronRouterCRUD routerInterface = getNeutronInterfaces(false).getRouterInterface();
        if (routerInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!routerInterface.routerExists(routerUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (fields.size() > 0) {
            NeutronRouter ans = routerInterface.getRouter(routerUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronRouterRequest(extractFields(ans, fields))).build();
        } else
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronRouterRequest(routerInterface.getRouter(routerUUID))).build();
    }

    /**
     * Creates new Routers */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createRouters(final NeutronRouterRequest input) {
        NeutronCRUDInterfaces interfaces = getNeutronInterfaces(true);
        INeutronRouterCRUD routerInterface = interfaces.getRouterInterface();
        INeutronNetworkCRUD networkInterface = interfaces.getNetworkInterface();
        if (input.isSingleton()) {
            NeutronRouter singleton = input.getSingleton();

            /*
             * verify that the router doesn't already exist (issue: is deeper inspection necessary?)
             * if there is external gateway information provided, verify that the specified network
             * exists and has been designated as "router:external"
             */
            if (routerInterface.routerExists(singleton.getID())) {
                throw new BadRequestException("router UUID already exists");
            }
            if (singleton.getExternalGatewayInfo() != null) {
                String externNetworkPtr = singleton.getExternalGatewayInfo().getNetworkID();
                if (!networkInterface.networkExists(externNetworkPtr)) {
                    throw new BadRequestException("External Network Pointer doesn't exist");
                }
                NeutronNetwork externNetwork = networkInterface.getNetwork(externNetworkPtr);
                if (!externNetwork.isRouterExternal()) {
                    throw new BadRequestException("External Network Pointer isn't marked as router:external");
                }
            }
            Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronRouterAware service = (INeutronRouterAware) instance;
                        int status = service.canCreateRouter(singleton);
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
             * add router to the cache
             */
            routerInterface.addRouter(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    service.neutronRouterCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton router creates supported
             */
            throw new BadRequestException("Only singleton router creates supported");
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Router */

    @Path("{routerUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouters.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateRouter(
            @PathParam("routerUUID") String routerUUID,
            NeutronRouterRequest input
            ) {
        NeutronCRUDInterfaces interfaces = getNeutronInterfaces(true);
        INeutronRouterCRUD routerInterface = interfaces.getRouterInterface();
        INeutronNetworkCRUD networkInterface = interfaces.getNetworkInterface();

        /*
         * router has to exist and only a single delta can be supplied
         */
        if (!routerInterface.routerExists(routerUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (!input.isSingleton()) {
            throw new BadRequestException("Only single router deltas supported");
        }
        NeutronRouter singleton = input.getSingleton();
        NeutronRouter original = routerInterface.getRouter(routerUUID);

        /*
         * attribute changes blocked by Neutron
         */
        if (singleton.getID() != null || singleton.getTenantID() != null ||
                singleton.getStatus() != null) {
            throw new BadRequestException("Request attribute change not allowed");
        }

        Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    int status = service.canUpdateRouter(singleton, original);
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
         * if the external gateway info is being changed, verify that the new network
         * exists and has been designated as an external network
         */
        if (singleton.getExternalGatewayInfo() != null) {
            String externNetworkPtr = singleton.getExternalGatewayInfo().getNetworkID();
            if (!networkInterface.networkExists(externNetworkPtr)) {
                throw new BadRequestException("External Network Pointer does not exist");
            }
            NeutronNetwork externNetwork = networkInterface.getNetwork(externNetworkPtr);
            if (!externNetwork.isRouterExternal()) {
                throw new BadRequestException("External Network Pointer isn't marked as router:external");
            }
        }

        /*
         * update the router entry and return the modified object
         */
        routerInterface.updateRouter(routerUUID, singleton);
        NeutronRouter updatedRouter = routerInterface.getRouter(routerUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronRouterAware service = (INeutronRouterAware) instance;
                service.neutronRouterUpdated(updatedRouter);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronRouterRequest(routerInterface.getRouter(routerUUID))).build();

    }

    /**
     * Deletes a Router */

    @Path("{routerUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteRouter(
            @PathParam("routerUUID") String routerUUID) {
        INeutronRouterCRUD routerInterface = getNeutronInterfaces(false).getRouterInterface();

        /*
         * verify that the router exists and is not in use before removing it
         */
        if (!routerInterface.routerExists(routerUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (routerInterface.routerInUse(routerUUID)) {
            throw new ResourceConflictException("Router UUID in Use");
        }
        NeutronRouter singleton = routerInterface.getRouter(routerUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    int status = service.canDeleteRouter(singleton);
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
        routerInterface.removeRouter(routerUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronRouterAware service = (INeutronRouterAware) instance;
                service.neutronRouterDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }

    /**
     * Adds an interface to a router */

    @Path("{routerUUID}/add_router_interface")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouterInterfaces.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response addRouterInterface(
            @PathParam("routerUUID") String routerUUID,
            NeutronRouter_Interface input
            ) {
        NeutronCRUDInterfaces interfaces = getAttachInterfaces();
        INeutronRouterCRUD routerInterface = interfaces.getRouterInterface();
        INeutronPortCRUD portInterface = interfaces.getPortInterface();
        INeutronSubnetCRUD subnetInterface = interfaces.getSubnetInterface();

        /*
         *  While the Neutron specification says that the router has to exist and the input can only specify either a subnet id
         *  or a port id, but not both, this code assumes that the plugin has filled everything in for us and so both must be present
         */
        if (!routerInterface.routerExists(routerUUID)) {
            throw new BadRequestException(UUID_NO_EXIST);
        }
        NeutronRouter target = routerInterface.getRouter(routerUUID);
        if (input.getSubnetUUID() == null ||
                    input.getPortUUID() == null) {
            throw new BadRequestException("Must specify at subnet id, port id or both");
        }

        // check that the port is part of the subnet
        NeutronSubnet targetSubnet = subnetInterface.getSubnet(input.getSubnetUUID());
        if (targetSubnet == null) {
            throw new BadRequestException("Subnet id doesn't exist");
        }
        NeutronPort targetPort = portInterface.getPort(input.getPortUUID());
        if (targetPort == null) {
            throw new BadRequestException("Port id doesn't exist");
        }
        if (!targetSubnet.getPortsInSubnet().contains(targetPort)) {
            throw new BadRequestException("Port id not part of subnet id");
        }

        if (targetPort.getFixedIPs().size() != 1) {
            throw new BadRequestException("Port id must have a single fixedIP address");
        }
        if (targetPort.getDeviceID() != null && !targetPort.getDeviceID().equals(routerUUID)) {
            throw new ResourceConflictException("Target Port already allocated to a different device id");
        }
        if (targetPort.getDeviceOwner() != null &&
            !targetPort.getDeviceOwner().equalsIgnoreCase(ROUTER_INTERFACE_STR) &&
            !targetPort.getDeviceOwner().equalsIgnoreCase(ROUTER_GATEWAY_STR)) {
            throw new ResourceConflictException("Target Port already allocated to non-router interface");
        }
        Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    int status = service.canAttachInterface(target, input);
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

        //mark the port device id and device owner fields
        if (targetPort.getDeviceOwner() == null || targetPort.getDeviceOwner().isEmpty()) {
            targetPort.setDeviceOwner(ROUTER_INTERFACE_STR);
        }
        targetPort.setDeviceID(routerUUID);

        target.addInterface(input.getPortUUID(), input);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronRouterAware service = (INeutronRouterAware) instance;
                service.neutronRouterInterfaceAttached(target, input);
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
    }

    /**
     * Removes an interface to a router */

    @Path("{routerUUID}/remove_router_interface")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackRouterInterfaces.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response removeRouterInterface(
            @PathParam("routerUUID") String routerUUID,
            NeutronRouter_Interface input
            ) {
        NeutronCRUDInterfaces interfaces = getAttachInterfaces();
        INeutronRouterCRUD routerInterface = interfaces.getRouterInterface();
        INeutronPortCRUD portInterface = interfaces.getPortInterface();
        INeutronSubnetCRUD subnetInterface = interfaces.getSubnetInterface();

        // verify the router exists
        if (!routerInterface.routerExists(routerUUID)) {
            throw new BadRequestException("Router does not exist");
        }
        NeutronRouter target = routerInterface.getRouter(routerUUID);

        /*
         * remove by subnet id.  Collect information about the impacted router for the response and
         * remove the port corresponding to the gateway IP address of the subnet
         */
        if (input.getPortUUID() == null &&
                input.getSubnetUUID() != null) {
            NeutronPort port = portInterface.getGatewayPort(input.getSubnetUUID());
            if (port == null) {
                throw new ResourceNotFoundException("Port UUID not found");
            }
            input.setPortUUID(port.getID());
            input.setID(target.getID());
            input.setTenantID(target.getTenantID());

            Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronRouterAware service = (INeutronRouterAware) instance;
                        int status = service.canDetachInterface(target, input);
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

            // reset the port ownership
            port.setDeviceID(null);
            port.setDeviceOwner(null);

            target.removeInterface(input.getPortUUID());
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    service.neutronRouterInterfaceDetached(target, input);
                }
            }
            return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
        }

        /*
         * remove by port id. collect information about the impacted router for the response
         * remove the interface and reset the port ownership
         */
        if (input.getPortUUID() != null &&
                input.getSubnetUUID() == null) {
            NeutronRouter_Interface targetInterface = target.getInterfaces().get(input.getPortUUID());
            if (targetInterface == null) {
                throw new ResourceNotFoundException("Router interface not found for given Port UUID");
            }
            input.setSubnetUUID(targetInterface.getSubnetUUID());
            input.setID(target.getID());
            input.setTenantID(target.getTenantID());
            Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronRouterAware service = (INeutronRouterAware) instance;
                        int status = service.canDetachInterface(target, input);
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
            NeutronPort port = portInterface.getPort(input.getPortUUID());
            port.setDeviceID(null);
            port.setDeviceOwner(null);
            target.removeInterface(input.getPortUUID());
            for (Object instance : instances) {
                INeutronRouterAware service = (INeutronRouterAware) instance;
                service.neutronRouterInterfaceDetached(target, input);
            }
            return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
        }

        /*
         * remove by both port and subnet ID.  Verify that the first fixed IP of the port is a valid
         * IP address for the subnet, and then remove the interface, collecting information about the
         * impacted router for the response and reset port ownership
         */
        if (input.getPortUUID() != null &&
                input.getSubnetUUID() != null) {
            NeutronPort port = portInterface.getPort(input.getPortUUID());
            if (port == null) {
                throw new ResourceNotFoundException("Port UUID not found");
            }
            if (port.getFixedIPs() == null) {
                throw new ResourceNotFoundException("Port UUID has no fixed IPs");
            }
            NeutronSubnet subnet = subnetInterface.getSubnet(input.getSubnetUUID());
            if (subnet == null) {
                throw new ResourceNotFoundException("Subnet UUID not found");
            }
            if (!subnet.isValidIP(port.getFixedIPs().get(0).getIpAddress())) {
                throw new ResourceConflictException("Target Port IP not in Target Subnet");
            }
            Object[] instances = NeutronUtil.getInstances(INeutronRouterAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronRouterAware service = (INeutronRouterAware) instance;
                        int status = service.canDetachInterface(target, input);
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
            input.setID(target.getID());
            input.setTenantID(target.getTenantID());
            port.setDeviceID(null);
            port.setDeviceOwner(null);
            target.removeInterface(input.getPortUUID());
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronRouterAware service = (INeutronRouterAware) instance;
                    service.canDetachInterface(target, input);
                }
            }            for (Object instance : instances) {
                INeutronRouterAware service = (INeutronRouterAware) instance;
                service.neutronRouterInterfaceDetached(target, input);
            }
            return Response.status(HttpURLConnection.HTTP_OK).entity(input).build();
        }

        // have to specify either a port ID or a subnet ID
        throw new BadRequestException("Must specify port id or subnet id or both");
    }
}
