/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.codehaus.enunciate.jaxrs.TypeHint;

import org.opendaylight.neutron.spi.INeutronMeteringLabelAware;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;

/**
 * Neutron Northbound REST APIs for Metering Lables.<br>
 * This class provides REST APIs for managing neutron metering labels
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

@Path("/metering/metering-labels")
public class NeutronMeteringLabelsNorthbound {

    private NeutronMeteringLabel extractFields(NeutronMeteringLabel o, List<String> fields) {
        return o.extractFields(fields);
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering labels */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response listMeteringLabels(
            // return fields
            @QueryParam("fields") List<String> fields,
            // filter fields
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("description") String queryDescription
            // pagination and sorting are TODO
            ) {
        INeutronMeteringLabelCRUD labelInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelCRUD(this);
        if (labelInterface == null) {
            throw new ServiceUnavailableException("NeutronMeteringLabel CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        List<NeutronMeteringLabel> allNeutronMeteringLabels = labelInterface.getAllNeutronMeteringLabels();
        List<NeutronMeteringLabel> ans = new ArrayList<NeutronMeteringLabel>();
        Iterator<NeutronMeteringLabel> i = allNeutronMeteringLabels.iterator();
        while (i.hasNext()) {
            NeutronMeteringLabel oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getMeteringLabelUUID())) &&
                    (queryName == null || queryName.equals(oSS.getMeteringLabelName())) &&
                    (queryDescription == null || queryDescription.equals(oSS.getMeteringLabelDescription())) &&
                    (queryTenantID == null || queryTenantID.equals(oSS.getMeteringLabelTenantID()))) {
                if (fields.size() > 0)
                    ans.add(extractFields(oSS,fields));
                else
                    ans.add(oSS);
            }
        }
        //TODO: apply pagination to results
        return Response.status(200).entity(
                new NeutronMeteringLabelRequest(ans)).build();
    }

    /**
     * Returns a specific metering label */

    @Path("{labelUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = 200, condition = "Operation successful"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 403, condition = "Forbidden"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response showMeteringLabel(
            @PathParam("labelUUID") String labelUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronMeteringLabelCRUD labelInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelCRUD(this);
        if (labelInterface == null) {
            throw new ServiceUnavailableException("MeteringLabel CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (!labelInterface.neutronMeteringLabelExists(labelUUID)) {
            throw new ResourceNotFoundException("MeteringLabel UUID not found");
        }
        if (fields.size() > 0) {
            NeutronMeteringLabel ans = labelInterface.getNeutronMeteringLabel(labelUUID);
            return Response.status(200).entity(
                    new NeutronMeteringLabelRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(200).entity(
                    new NeutronMeteringLabelRequest(labelInterface.getNeutronMeteringLabel(labelUUID))).build();
        }
    }

    /**
     * Creates new metering label */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(NeutronNetwork.class)
    @StatusCodes({
            @ResponseCode(code = 201, condition = "Created"),
            @ResponseCode(code = 400, condition = "Bad Request"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response createMeteringLabel(final NeutronMeteringLabelRequest input) {
        INeutronMeteringLabelCRUD meteringLabelInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelCRUD(this);
        if (meteringLabelInterface == null) {
            throw new ServiceUnavailableException("MeteringLabel CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        if (input.isSingleton()) {
            NeutronMeteringLabel singleton = input.getSingleton();

            /*
             * verify that the meteringLabel doesn't already exist (issue: is deeper inspection necessary?)
             */
            if (meteringLabelInterface.neutronMeteringLabelExists(singleton.getMeteringLabelUUID()))
                throw new BadRequestException("meteringLabel UUID already exists");
            Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelAware.class, this);
            if (instances != null) {
                if (instances.length > 0) {
                    for (Object instance : instances) {
                        INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                        int status = service.canCreateMeteringLabel(singleton);
                        if (status < 200 || status > 299)
                            return Response.status(status).build();
                    }
                } else {
                    throw new ServiceUnavailableException("No providers registered.  Please try again later");
                }
            } else {
                throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
            }

            /*
             * add meteringLabel to the cache
             */
            meteringLabelInterface.addNeutronMeteringLabel(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                    service.neutronMeteringLabelCreated(singleton);
                }
            }
        } else {

            /*
             * only singleton meteringLabel creates supported
             */
            throw new BadRequestException("Only singleton meteringLabel creates supported");
        }
        return Response.status(201).entity(input).build();
    }

    /**
     * Deletes a Metering Label */

    @Path("{labelUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = 204, condition = "No Content"),
            @ResponseCode(code = 401, condition = "Unauthorized"),
            @ResponseCode(code = 404, condition = "Not Found"),
            @ResponseCode(code = 409, condition = "Conflict"),
            @ResponseCode(code = 501, condition = "Not Implemented"),
            @ResponseCode(code = 503, condition = "No providers available") })
    public Response deleteMeteringLabel(
            @PathParam("labelUUID") String labelUUID) {
        INeutronMeteringLabelCRUD meteringLabelInterface = NeutronCRUDInterfaces.getINeutronMeteringLabelCRUD(this);
        if (meteringLabelInterface == null) {
            throw new ServiceUnavailableException("MeteringLabel CRUD Interface "
                    + RestMessages.SERVICEUNAVAILABLE.toString());
        }

        /*
         * verify that the meteringLabel exists and is not in use before removing it
         */
        if (!meteringLabelInterface.neutronMeteringLabelExists(labelUUID))
            throw new ResourceNotFoundException("MeteringLabel UUID not found");
        NeutronMeteringLabel singleton = meteringLabelInterface.getNeutronMeteringLabel(labelUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelAware.class, this);
        if (instances != null) {
            if (instances.length > 0) {
                for (Object instance : instances) {
                    INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                    int status = service.canDeleteMeteringLabel(singleton);
                    if (status < 200 || status > 299)
                        return Response.status(status).build();
                }
            } else {
                throw new ServiceUnavailableException("No providers registered.  Please try again later");
            }
        } else {
            throw new ServiceUnavailableException("Couldn't get providers list.  Please try again later");
        }
        meteringLabelInterface.removeNeutronMeteringLabel(labelUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                service.neutronMeteringLabelDeleted(singleton);
            }
        }
        return Response.status(204).build();
    }
}
