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
import org.opendaylight.neutron.spi.INeutronFloatingIPAware;
import org.opendaylight.neutron.spi.INeutronFloatingIPCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFloatingIP;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSubnet;
import org.opendaylight.neutron.spi.Neutron_IPs;

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
public class NeutronFloatingIPsNorthbound {
    private static final int HTTP_OK_BOTTOM = 200;
    private static final int HTTP_OK_TOP = 299;
    private static final String INTERFACE_NAME = "Floating IP CRUD Interface";
    private static final String UUID_NO_EXIST = "Floating IP UUID does not exist.";
    private static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    private static final String NO_PROVIDER_LIST = "Couldn't get providers list.  Please try again later";


    private NeutronFloatingIP extractFields(NeutronFloatingIP o, List<String> fields) {
        return o.extractFields(fields);
    }

    /**
     * Returns a list of all FloatingIPs */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
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
        INeutronFloatingIPCRUD floatingIPInterface = NeutronCRUDInterfaces.getINeutronFloatingIPCRUD(this);
        if (floatingIPInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronFloatingIP> allFloatingIPs = floatingIPInterface.getAllFloatingIPs();
        List<NeutronFloatingIP> ans = new ArrayList<NeutronFloatingIP>();
        Iterator<NeutronFloatingIP> i = allFloatingIPs.iterator();
        while (i.hasNext()) {
            NeutronFloatingIP oSS = i.next();
            //match filters: TODO provider extension and router extension
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryFloatingNetworkId == null || queryFloatingNetworkId.equals(oSS.getFloatingNetworkUUID())) &&
                    (queryPortId == null || queryPortId.equals(oSS.getPortUUID())) &&
                    (queryFixedIPAddress == null || queryFixedIPAddress.equals(oSS.getFixedIPAddress())) &&
                    (queryFloatingIPAddress == null || queryFloatingIPAddress.equals(oSS.getFloatingIPAddress())) &&
                    (queryStatus == null || queryStatus.equals(oSS.getStatus())) &&
                    (queryRouterID == null || queryRouterID.equals(oSS.getRouterUUID())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getTenantUUID()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(oSS,fields));
                } else {
                    ans.add(oSS);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronFloatingIPRequest(ans)).build();
    }

    /**
     * Returns a specific FloatingIP */

    @Path("{floatingipUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFloatingIP(
            @PathParam("floatingipUUID") String floatingipUUID,
            // return fields
            @QueryParam("fields") List<String> fields ) {
        INeutronFloatingIPCRUD floatingIPInterface = NeutronCRUDInterfaces.getINeutronFloatingIPCRUD(this);
        if (floatingIPInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!floatingIPInterface.floatingIPExists(floatingipUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (fields.size() > 0) {
            NeutronFloatingIP ans = floatingIPInterface.getFloatingIP(floatingipUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronFloatingIPRequest(extractFields(ans, fields))).build();
        } else
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronFloatingIPRequest(floatingIPInterface.getFloatingIP(floatingipUUID))).build();

    }

    /**
     * Creates new FloatingIPs */

    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
        @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
        @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
        @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFloatingIPs(final NeutronFloatingIPRequest input) {
        INeutronFloatingIPCRUD floatingIPInterface = NeutronCRUDInterfaces.getINeutronFloatingIPCRUD(this);
        if (floatingIPInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronNetworkCRUD networkInterface = NeutronCRUDInterfaces.getINeutronNetworkCRUD( this);
        if (networkInterface == null) {
            throw new ServiceUnavailableException("Network CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronSubnetCRUD subnetInterface = NeutronCRUDInterfaces.getINeutronSubnetCRUD( this);
        if (subnetInterface == null) {
            throw new ServiceUnavailableException("Subnet CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronPortCRUD portInterface = NeutronCRUDInterfaces.getINeutronPortCRUD( this);
        if (portInterface == null) {
            throw new ServiceUnavailableException("Port CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronFloatingIP singleton = input.getSingleton();
            // check existence of id in cache and return badrequest if exists
            if (floatingIPInterface.floatingIPExists(singleton.getID())) {
                throw new BadRequestException("Floating IP UUID already exists.");
            }
            // check if the external network is specified, exists, and is an external network
            String externalNetworkUUID = singleton.getFloatingNetworkUUID();
            if (externalNetworkUUID == null) {
                throw new BadRequestException("external network UUID doesn't exist.");
            }
            if (!networkInterface.networkExists(externalNetworkUUID)) {
                throw new BadRequestException("external network UUID doesn't exist.");
            }
            NeutronNetwork externNetwork = networkInterface.getNetwork(externalNetworkUUID);
            if (!externNetwork.isRouterExternal()) {
                throw new BadRequestException("external network isn't marked router:external");
            }
            // if floating IP is specified, make sure it can come from the network
            String floatingIP = singleton.getFloatingIPAddress();
            if (floatingIP != null) {
                if (externNetwork.getSubnets().size() != 1) {
                    throw new BadRequestException("external network doesn't have a subnet");
                }
                NeutronSubnet externSubnet = subnetInterface.getSubnet(externNetwork.getSubnets().get(0));
                if (!externSubnet.isValidIP(floatingIP)) {
                    throw new BadRequestException("external IP isn't valid for the specified subnet.");
                }
                if (externSubnet.getFloatingIpPortsInSubnet(floatingIP).isEmpty() && externSubnet.isIPInUse(floatingIP)) {
                    throw new ResourceConflictException("floating IP is in use.");
                }
            }
            // if port_id is specified, then check that the port exists and has at least one IP
            String port_id = singleton.getPortUUID();
            if (port_id != null) {
                String fixedIP = null;
                if (!portInterface.portExists(port_id)) {
                    throw new ResourceNotFoundException("Port UUID doesn't exist.");
                }
                NeutronPort port = portInterface.getPort(port_id);
                if (port.getFixedIPs().size() < 1) {
                    throw new BadRequestException("port UUID doesn't have an IP address.");
                }
                // if there is more than one fixed IP then check for fixed_ip_address
                // and that it is in the list of port addresses
                if (port.getFixedIPs().size() > 1) {
                    fixedIP = singleton.getFixedIPAddress();
                    if (fixedIP == null) {
                        throw new BadRequestException("fixed IP address doesn't exist.");
                    }
                    Iterator<Neutron_IPs> i = port.getFixedIPs().iterator();
                    boolean validFixedIP = false;
                    while (i.hasNext() && !validFixedIP) {
                        Neutron_IPs ip = i.next();
                        if (ip.getIpAddress().equals(fixedIP)) {
                            validFixedIP = true;
                        }
                    }
                    if (!validFixedIP) {
                        throw new BadRequestException("can't find a valid fixed IP address");
                    }
                } else {
                    fixedIP = port.getFixedIPs().get(0).getIpAddress();
                    if (singleton.getFixedIPAddress() != null && !fixedIP.equalsIgnoreCase(singleton.getFixedIPAddress())) {
                        throw new BadRequestException("mismatched fixed IP address in request");
                    }
                }
                //lastly check that this fixed IP address isn't already used
                if (port.isBoundToFloatingIP(fixedIP)) {
                    throw new ResourceConflictException("fixed IP is in use.");
                }
                singleton.setFixedIPAddress(fixedIP);
            }
            Object[] instances = NeutronUtil.getInstances(INeutronFloatingIPAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                        int status = service.canCreateFloatingIP(singleton);
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
            floatingIPInterface.addFloatingIP(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                    service.neutronFloatingIPCreated(singleton);
                }
            }
        } else {
            throw new BadRequestException("only singleton requests allowed.");
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a FloatingIP */

    @Path("{floatingipUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFloatingIP(
            @PathParam("floatingipUUID") String floatingipUUID,
            NeutronFloatingIPRequest input
            ) {
        INeutronFloatingIPCRUD floatingIPInterface = NeutronCRUDInterfaces.getINeutronFloatingIPCRUD(this);
        if (floatingIPInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronNetworkCRUD networkInterface = NeutronCRUDInterfaces.getINeutronNetworkCRUD( this);
        if (networkInterface == null) {
            throw new ServiceUnavailableException("Network CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronSubnetCRUD subnetInterface = NeutronCRUDInterfaces.getINeutronSubnetCRUD( this);
        if (subnetInterface == null) {
            throw new ServiceUnavailableException("Subnet CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        INeutronPortCRUD portInterface = NeutronCRUDInterfaces.getINeutronPortCRUD( this);
        if (portInterface == null) {
            throw new ServiceUnavailableException("Port CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!floatingIPInterface.floatingIPExists(floatingipUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }

        NeutronFloatingIP sourceFloatingIP = floatingIPInterface.getFloatingIP(floatingipUUID);
        if (!input.isSingleton()) {
            throw new BadRequestException("only singleton requests allowed.");
        }
        NeutronFloatingIP singleton = input.getSingleton();
        if (singleton.getID() == null) {
            throw new BadRequestException("singleton UUID doesn't exist.");
        }

        NeutronNetwork externNetwork = networkInterface.getNetwork(
                sourceFloatingIP.getFloatingNetworkUUID());

        // if floating IP is specified, make sure it can come from the network
        String floatingIP = singleton.getFloatingIPAddress();
        if (floatingIP != null) {
            if (externNetwork.getSubnets().size() != 1) {
                throw new BadRequestException("external network doesn't have a subnet.");
            }
            NeutronSubnet externSubnet = subnetInterface.getSubnet(externNetwork.getSubnets().get(0));
            if (!externSubnet.isValidIP(floatingIP)) {
                throw new BadRequestException("floating IP not valid for external subnet");
            }
            if (externSubnet.isIPInUse(floatingIP)) {
                throw new ResourceConflictException("floating IP is in use.");
            }
        }

        // if port_id is specified, then check that the port exists and has at least one IP
        String port_id = singleton.getPortUUID();
        if (port_id != null) {
            String fixedIP = null;
            if (!portInterface.portExists(port_id)) {
                throw new ResourceNotFoundException("Port UUID doesn't exist.");
            }
            NeutronPort port = portInterface.getPort(port_id);
            if (port.getFixedIPs().size() < 1) {
                throw new BadRequestException("port ID doesn't have a fixed IP address.");
            }
            // if there is more than one fixed IP then check for fixed_ip_address
            // and that it is in the list of port addresses
            if (port.getFixedIPs().size() > 1) {
                fixedIP = singleton.getFixedIPAddress();
                if (fixedIP == null) {
                    throw new BadRequestException("request doesn't have a fixed IP address");
                }
                Iterator<Neutron_IPs> i = port.getFixedIPs().iterator();
                boolean validFixedIP = false;
                while (i.hasNext() && !validFixedIP) {
                    Neutron_IPs ip = i.next();
                    if (ip.getIpAddress().equals(fixedIP)) {
                        validFixedIP = true;
                    }
                }
                if (!validFixedIP) {
                    throw new BadRequestException("couldn't find a valid fixed IP address");
                }
            } else {
                fixedIP = port.getFixedIPs().get(0).getIpAddress();
                if (singleton.getFixedIPAddress() != null &&
                        !fixedIP.equalsIgnoreCase(singleton.getFixedIPAddress())) {
                    throw new BadRequestException("mismatch in fixed IP addresses");
                }
            }
            //lastly check that this fixed IP address isn't already used
            if (port.isBoundToFloatingIP(fixedIP)) {
                throw new ResourceConflictException("fixed IP is in use.");
            }
            singleton.setFixedIPAddress(fixedIP);
        }
        NeutronFloatingIP target = floatingIPInterface.getFloatingIP(floatingipUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronFloatingIPAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                    int status = service.canUpdateFloatingIP(singleton, target);
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
        floatingIPInterface.updateFloatingIP(floatingipUUID, singleton);
        target = floatingIPInterface.getFloatingIP(floatingipUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                service.neutronFloatingIPUpdated(target);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronFloatingIPRequest(target)).build();

    }

    /**
     * Deletes a FloatingIP */

    @Path("{floatingipUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFloatingIP(
            @PathParam("floatingipUUID") String floatingipUUID) {
        INeutronFloatingIPCRUD floatingIPInterface = NeutronCRUDInterfaces.getINeutronFloatingIPCRUD(this);
        if (floatingIPInterface == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!floatingIPInterface.floatingIPExists(floatingipUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        // TODO: need to undo port association if it exists
        NeutronFloatingIP singleton = floatingIPInterface.getFloatingIP(floatingipUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronFloatingIPAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                    int status = service.canDeleteFloatingIP(singleton);
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
        floatingIPInterface.removeFloatingIP(floatingipUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFloatingIPAware service = (INeutronFloatingIPAware) instance;
                service.neutronFloatingIPDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
