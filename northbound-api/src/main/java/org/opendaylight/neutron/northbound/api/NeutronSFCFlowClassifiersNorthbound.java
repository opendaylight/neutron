/*
 * Copyright (c) 2016 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.northbound.api;

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
import org.opendaylight.neutron.spi.INeutronSFCFlowClassifierCRUD;
import org.opendaylight.neutron.spi.NeutronSFCFlowClassifier;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Neutron Northbound REST APIs for OpenStack SFC Flow Classifier.
 */
@Singleton
@Path("/sfc/flowclassifiers")
public final class NeutronSFCFlowClassifiersNorthbound extends AbstractNeutronNorthbound<NeutronSFCFlowClassifier,
        NeutronSFCFlowClassifierRequest, INeutronSFCFlowClassifierCRUD> {

    private static final String RESOURCE_NAME = "Sfc Flow Classifier";

    @Inject
    public NeutronSFCFlowClassifiersNorthbound(@OsgiService INeutronSFCFlowClassifierCRUD neutronCRUD) {
        super(neutronCRUD);
    }

    @Override
    protected String getResourceName() {
        return RESOURCE_NAME;
    }

    /**
     * Returns a list of all SFC Flow Classifiers.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response listSFCFlowClassifiers(
            // return fields
            @QueryParam("fields") List<String> fields,
            // note: openstack isn't clear about filtering on lists, so we aren't handling them
            @QueryParam("id") String queryID,
            @QueryParam("name") String queryName,
            @QueryParam("ethertype") String queryEthertype,
            @QueryParam("protocol") String queryProtocol,
            @QueryParam("source_port_range_min") Integer querySourcePortRangeMin,
            @QueryParam("source_port_range_max") Integer querySourcePortRangeMax,
            @QueryParam("tenant_id") String queryTenantID,
            @QueryParam("destination_port_range_min") Integer queryDestinationPortRangeMin,
            @QueryParam("destination_port_range_max") Integer queryDestinationPortRangeMax,
            @QueryParam("source_ip_prefix") String querySourceIpPrefix,
            @QueryParam("destination_ip_prefix") String queryDestinationIpPrefix,
            @QueryParam("logical_source_port") String queryLogicalSourcePort,
            @QueryParam("logical_destination_port") String queryLogicalDestinationPort) {
        INeutronSFCFlowClassifierCRUD sfcFlowClassifierInterface = getNeutronCRUD();
        List<NeutronSFCFlowClassifier> allSFCFlowClassifier = sfcFlowClassifierInterface.getAll();
        List<NeutronSFCFlowClassifier> ans = new ArrayList<>();
        for (NeutronSFCFlowClassifier classifier : allSFCFlowClassifier) {
            if ((queryID == null || queryID.equals(classifier.getID()))
                    && (queryName == null || queryName.equals(classifier.getName()))
                    && (queryEthertype == null || queryEthertype.equals(classifier.getEthertype()))
                    && (queryProtocol == null || queryProtocol.equals(classifier.getProtocol()))
                    && (querySourcePortRangeMin == null
                        || querySourcePortRangeMin.equals(classifier.getSourcePortRangeMin()))
                    && (querySourcePortRangeMax == null
                        || querySourcePortRangeMax.equals(classifier.getSourcePortRangeMax()))
                    && (queryDestinationPortRangeMin == null
                            || queryDestinationPortRangeMin.equals(classifier.getDestinationPortRangeMin()))
                    && (queryDestinationPortRangeMax == null
                            || queryDestinationPortRangeMax.equals(classifier.getDestinationPortRangeMax()))
                    && (querySourceIpPrefix == null || querySourceIpPrefix.equals(classifier.getSourceIpPrefix()))
                    && (queryDestinationIpPrefix == null
                            || queryDestinationIpPrefix.equals(classifier.getDestinationIpPrefix()))
                    && (queryLogicalSourcePort == null
                            || queryLogicalSourcePort.equals(classifier.getLogicalSourcePortUUID()))
                    && (queryLogicalDestinationPort == null
                            || queryLogicalDestinationPort.equals(classifier.getLogicalDestinationPortUUID()))
                    && (queryTenantID == null || queryTenantID.equals(classifier.getTenantID()))) {
                if (fields.size() > 0) {
                    ans.add(classifier.extractFields(fields));
                } else {
                    ans.add(classifier);
                }
            }
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronSFCFlowClassifierRequest(ans)).build();

    }

    /**
     * Returns a specific SFC Flow Classifier.
     */
    @Path("{flowClassifierUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showSFCFlowClassifier(@PathParam("flowClassifierUUID") String sfcFlowClassifierUUID,
            // return fields
            @QueryParam("fields") List<String> fields) {
        return show(sfcFlowClassifierUUID, fields);
    }

    /**
     * Creates new SFC Flow Classifier.
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createSFCFlowClassifier(final NeutronSFCFlowClassifierRequest input) {
        return create(input);
    }

    @Override
    protected void updateDelta(String uuid, NeutronSFCFlowClassifier delta, NeutronSFCFlowClassifier original) {
        /*
         * note: what we get appears to not be a delta but
         * rather an incomplete updated object.  So we need to set
         * the ID to complete the object and then send that down
         * for folks to check
         */
        delta.setID(uuid);
        delta.setTenantID(original.getTenantID());
    }

    /**
     * Updates an existing SFC Flow Classifier.
     */
    @Path("{flowClassifierUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateSFCFlowClassifier(@PathParam("flowClassifierUUID") String sfcFlowClassifierUUID,
            final NeutronSFCFlowClassifierRequest input) {
        return update(sfcFlowClassifierUUID, input);
    }

    /**
     * Deletes the SFC Flow Classifier.
     */
    @Path("{flowClassifierUUID}")
    @DELETE
    @StatusCodes({ @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteSFCFlowClassifier(@PathParam("flowClassifierUUID") String sfcFlowClassifierUUID) {
        return delete(sfcFlowClassifierUUID);
    }
}
