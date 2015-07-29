/*
 * Copyright (c) 2015 Hewlett-Packard Development Company and others.  All rights reserved.
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
import org.opendaylight.neutron.spi.INeutronL2GatewayCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronL2Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Neutron Northbound REST APIs for L2 gateway.<br>
 * This class provides REST APIs for managing L2 gateway
 * *
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

@Path ("/l2Gateways")
public class NeutronL2GatewayNorthbound {
    static final Logger logger = LoggerFactory
            .getLogger(NeutronL2GatewayNorthbound.class);

    private static final int HTTP_OK_BOTTOM = 200;
    private static final int HTTP_OK_TOP = 299;
    private static final String INTERFACE_NAME = "L2gateway CRUD Interface";
    private static final String NO_PROVIDERS = "No providers registered.  Please try again later";
    private static final String NO_PROVIDER_LIST = "Couldn't get providers list. Please try again later";
    private static final String UUID_NO_EXIST = "L2gateway UUID does not exist.";

    private NeutronL2Gateway extractFields(NeutronL2Gateway o, List<String> fields) {
        return o.extractFields(fields);
    }
    private NeutronCRUDInterfaces getNeutronInterfaces() {
        logger.debug("Get Neutron interface");
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces()
        .fetchINeutronL2GatewayCRUD(this);
        if (answer.getL2gatewayInterface() == null) {
            throw new ServiceUnavailableException(INTERFACE_NAME
                               + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    /**
     * Creates L2gateway
     */
    @POST
    @Produces ({MediaType.APPLICATION_JSON})
    @Consumes ({MediaType.APPLICATION_JSON})
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_BAD_REQUEST, condition = "Bad Request"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_CONFLICT, condition = "Conflict"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })

    public Response createL2Gateway(final NeutronL2GatewayRequest input) {
        logger.debug("CreateL2Gateway     NeutronL2GatewayRequest");
        INeutronL2GatewayCRUD l2gatewayInterface = getNeutronInterfaces()
                .getL2gatewayInterface();

        if (input.isSingleton()) {
            NeutronL2Gateway singleton = input.getSingleton();

            /* Verify that the L2gateway doesn't already exist.*/
            if (l2gatewayInterface.neutronL2gatewayExists(singleton.getL2gatewayUUID())) {
                throw new BadRequestException("L2gateway already exists");
            }

            /* Add l2gateway to MD-SAL. */
            l2gatewayInterface.addNeutronL2gateway(singleton);

        } else {
            for (NeutronL2Gateway neutronL2Gateway : input.getBulk()) {
                /* Verify that the L2gateway doesn't already exist.*/
                if (l2gatewayInterface.neutronL2gatewayExists(neutronL2Gateway.getL2gatewayUUID())) {
                    throw new BadRequestException("L2gateway already exists");
                }
            }
            for (NeutronL2Gateway neutronL2Gateway : input.getBulk()) {
                /* Add l2gateway to MD-SAL. */
                l2gatewayInterface.addNeutronL2gateway(neutronL2Gateway);
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Returns a list of all L2gateways. */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK,
                condition = "Operation successful"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED,
                condition = "Unauthorized"),
                @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                condition = "Not Implemented"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
                condition = "No providers available") })
    public Response listL2gateways(
                                   // return fields
                                   @QueryParam("fields") List<String> fields,
                                   // OpenStack L2gateway attributes
                                   @QueryParam("id") String queryID,
                                   @QueryParam("name") String queryName,
                                   @QueryParam("tenant_id") String queryTenantID,
                                   @QueryParam("devices") String queryNeutronDevice,
                                   // pagination
                                   @QueryParam("limit") String limit,
                                   @QueryParam("marker") String marker,
                                   @QueryParam("page_reverse") String pageReverse
                                   // sorting not supported
            ){
        INeutronL2GatewayCRUD l2gatewayInterface =
                getNeutronInterfaces().getL2gatewayInterface();
        List<NeutronL2Gateway> allL2gateways =
                l2gatewayInterface.getAllL2gateways();
        List<NeutronL2Gateway> ans = new ArrayList<NeutronL2Gateway>();
        Iterator<NeutronL2Gateway> i = allL2gateways.iterator();
        while (i.hasNext()) {
            NeutronL2Gateway l2gateway = i.next();
            if ((queryID == null || queryID
                    .equals(l2gateway.getL2gatewayUUID())) &&
                    (queryName == null || queryName
                    .equals(l2gateway.getGatewayName())) &&
                    (queryTenantID == null || queryTenantID
                    .equals(l2gateway.getTenantUUID())) &&
                    (queryNeutronDevice == null || queryNeutronDevice
                    .equals(l2gateway.getNeutronDevices()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(l2gateway,fields));
                }else {
                    ans.add(l2gateway);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                                   new NeutronL2GatewayRequest(ans)).build();
    }

    /**
     * Returns a specific L2gateway.
     */
    @Path ("{l2gatewayUUID}")
    @GET
    @Produces ({MediaType.APPLICATION_JSON})
    @StatusCodes ({
        @ResponseCode (code = HttpURLConnection.HTTP_OK,
                condition = "Operation successful"),
                @ResponseCode (code = HttpURLConnection.HTTP_UNAUTHORIZED,
                condition = "Unauthorized"),
                @ResponseCode (code = HttpURLConnection.HTTP_NOT_FOUND,
                condition = "Not Found"),
                @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED,
                condition = "Not Implemented"),
                @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
                condition = "No providers available") })
    public Response showL2gatewayUUID(@PathParam ("l2gatewayUUID") String l2gatewayUUID,
                                      // return fields
                                      @QueryParam ("fields") List<String> fields) {
        INeutronL2GatewayCRUD l2gatewayInterface =
                getNeutronInterfaces().getL2gatewayInterface();
        if (!l2gatewayInterface.neutronL2gatewayExists(l2gatewayUUID)) {
            throw new ResourceNotFoundException(UUID_NO_EXIST);
        }
        if (!fields.isEmpty()) {
            NeutronL2Gateway ans = l2gatewayInterface
                    .getNeutronL2gateway(l2gatewayUUID);
            return Response.status(HttpURLConnection.HTTP_OK)
                    .entity(new NeutronL2GatewayRequest(extractFields(ans, fields)))
                    .build();
        }
            return Response.status(HttpURLConnection.HTTP_OK)
                    .entity(new NeutronL2GatewayRequest(
                      l2gatewayInterface.getNeutronL2gateway(l2gatewayUUID)))
                      .build();
    }

    /**
     * Deletes a L2gateway */

    @Path("{l2gatewayUUID}")
    @DELETE
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT,
                condition = "No Content"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
        condition = "No providers available") })
    public Response deleteL2gateway(
            @PathParam("l2gatewayUUID") String l2gatewayUUID) {
        INeutronL2GatewayCRUD l2gatewayInterface =
                getNeutronInterfaces().getL2gatewayInterface();
        NeutronL2Gateway singleton=l2gatewayInterface
                .getNeutronL2gateway(l2gatewayUUID);
        if(!l2gatewayInterface.removeL2gateway(l2gatewayUUID)){
            throw new InternalServerErrorException("Could not delete l2gateway");
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }

    /**
     * Updates a L2gateway */

    @Path("{l2gatewayUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
        @ResponseCode(code = HttpURLConnection.HTTP_OK,
                condition = "Operation successful"),
        @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE,
        condition = "No providers available") })
    public Response updateL2gateway(
            @PathParam("l2gatewayUUID") String l2gatewayUUID,
            NeutronL2GatewayRequest input) {
        INeutronL2GatewayCRUD l2gatewayInterface =
                getNeutronInterfaces().getL2gatewayInterface();
        NeutronL2Gateway original = l2gatewayInterface
                .getNeutronL2gateway(l2gatewayUUID);

        NeutronL2Gateway updatedObject = input.getSingleton();
        updatedObject.setID(l2gatewayUUID);
        updatedObject.setTenantUUID(original.getTenantUUID());

        // update l2gateway and return the modified object
        l2gatewayInterface.updateL2gateway(l2gatewayUUID, updatedObject);

        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronL2GatewayRequest(updatedObject)).build();

    }
}