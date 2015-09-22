/*
 * Copyright (c) 2015 IBM Corporation and others.  All rights reserved.
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
public class NeutronMeteringLabelsNorthbound extends AbstractNeutronNorthbound {
    private static final String RESOURCE_NAME = "Metering Label";

    private NeutronMeteringLabel extractFields(NeutronMeteringLabel o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronMeteringLabelCRUD(this);
        if (answer.getMeteringLabelInterface() == null) {
            throw new ServiceUnavailableException("NeutronMeteringLabel CRUD Interface "
                + RestMessages.SERVICEUNAVAILABLE.toString());
        }
        return answer;
    }

    @Context
    UriInfo uriInfo;

    /**
     * Returns a list of all metering labels */

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackNetworks.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
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
        INeutronMeteringLabelCRUD labelInterface = getNeutronInterfaces().getMeteringLabelInterface();
        List<NeutronMeteringLabel> allNeutronMeteringLabels = labelInterface.getAllNeutronMeteringLabels();
        List<NeutronMeteringLabel> ans = new ArrayList<NeutronMeteringLabel>();
        Iterator<NeutronMeteringLabel> i = allNeutronMeteringLabels.iterator();
        while (i.hasNext()) {
            NeutronMeteringLabel oSS = i.next();
            if ((queryID == null || queryID.equals(oSS.getID())) &&
                    (queryName == null || queryName.equals(oSS.getMeteringLabelName())) &&
                    (queryDescription == null || queryDescription.equals(oSS.getMeteringLabelDescription())) &&
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
                new NeutronMeteringLabelRequest(ans)).build();
    }

    /**
     * Returns a specific metering label */

    @Path("{labelUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_FORBIDDEN, condition = "Forbidden"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showMeteringLabel(
            @PathParam("labelUUID") String labelUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        INeutronMeteringLabelCRUD labelInterface = getNeutronInterfaces().getMeteringLabelInterface();
        if (!labelInterface.neutronMeteringLabelExists(labelUUID)) {
            throw new ResourceNotFoundException("MeteringLabel UUID not found");
        }
        if (fields.size() > 0) {
            NeutronMeteringLabel ans = labelInterface.getNeutronMeteringLabel(labelUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronMeteringLabelRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(
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
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createMeteringLabel(final NeutronMeteringLabelRequest input) {
        INeutronMeteringLabelCRUD meteringLabelInterface = getNeutronInterfaces().getMeteringLabelInterface();
        if (input.isSingleton()) {
            NeutronMeteringLabel singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                    int status = service.canCreateMeteringLabel(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
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
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Deletes a Metering Label */

    @Path("{labelUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteMeteringLabel(
            @PathParam("labelUUID") String labelUUID) {
        final INeutronMeteringLabelCRUD meteringLabelInterface = getNeutronInterfaces().getMeteringLabelInterface();

        NeutronMeteringLabel singleton = meteringLabelInterface.getNeutronMeteringLabel(labelUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronMeteringLabelAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                int status = service.canDeleteMeteringLabel(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }
        deleteUuid(RESOURCE_NAME, labelUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return meteringLabelInterface.removeNeutronMeteringLabel(uuid);
                       }
                   });
        if (instances != null) {
            for (Object instance : instances) {
                INeutronMeteringLabelAware service = (INeutronMeteringLabelAware) instance;
                service.neutronMeteringLabelDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
