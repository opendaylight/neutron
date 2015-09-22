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
import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.NeutronCRUDInterfaces;
import org.opendaylight.neutron.spi.NeutronFirewallPolicy;

/**
 * Neutron Northbound REST APIs for Firewall Policies.<br>
 * This class provides REST APIs for managing neutron Firewall Policies
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
@Path("/fw/firewall_policies")
public class NeutronFirewallPolicyNorthbound extends AbstractNeutronNorthbound {

    private static final String RESOURCE_NAME = "Firewall Policy";

    private NeutronFirewallPolicy extractFields(NeutronFirewallPolicy o, List<String> fields) {
        return o.extractFields(fields);
    }

    private NeutronCRUDInterfaces getNeutronInterfaces() {
        NeutronCRUDInterfaces answer = new NeutronCRUDInterfaces().fetchINeutronFirewallPolicyCRUD(this);
        if (answer.getFirewallPolicyInterface() == null) {
            throw new ServiceUnavailableException(serviceUnavailable(RESOURCE_NAME));
        }
        return answer;
    }

    /**
     * Returns a list of all Firewall Policies */
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
            // OpenStack Firewall Policy attributes
            @QueryParam("id") String queryFirewallPolicyUUID,
            @QueryParam("tenant_id") String queryFirewallPolicyTenantID,
            @QueryParam("name") String queryFirewallPolicyName,
            @QueryParam("description") String querySecurityPolicyDescription,
            @QueryParam("shared") String querySecurityPolicyIsShared,
            @QueryParam("firewall_rules") List<String> querySecurityPolicyFirewallRules,
            @QueryParam("audited") Boolean querySecurityPolicyIsAudited,
            // pagination
            @QueryParam("limit") String limit,
            @QueryParam("marker") String marker,
            @QueryParam("page_reverse") String pageReverse
            // sorting not supported
    ) {
        INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronInterfaces().getFirewallPolicyInterface();
        List<NeutronFirewallPolicy> ans = new ArrayList<NeutronFirewallPolicy>();
        for (NeutronFirewallPolicy nsg : firewallPolicyInterface.getAllNeutronFirewallPolicies()) {
            if ((queryFirewallPolicyUUID == null ||
                queryFirewallPolicyUUID.equals(nsg.getID())) &&
                (queryFirewallPolicyTenantID == null ||
                    queryFirewallPolicyTenantID.equals(nsg.getTenantID())) &&
                (queryFirewallPolicyName == null ||
                    queryFirewallPolicyName.equals(nsg.getFirewallPolicyName())) &&
                (querySecurityPolicyDescription == null ||
                    querySecurityPolicyDescription.equals(nsg.getFirewallPolicyDescription())) &&
                (querySecurityPolicyIsShared == null ||
                    querySecurityPolicyIsShared.equals(nsg.getFirewallPolicyIsShared())) &&
                (querySecurityPolicyFirewallRules.size() == 0 ||
                    querySecurityPolicyFirewallRules.equals(nsg.getFirewallPolicyRules())) &&
                (querySecurityPolicyIsAudited == null ||
                    querySecurityPolicyIsAudited.equals(nsg.getFirewallPolicyIsAudited()))) {
                if (fields.size() > 0) {
                    ans.add(extractFields(nsg,fields));
                } else {
                    ans.add(nsg);
                }
            }
        }
        //TODO: apply pagination to results
        return Response.status(HttpURLConnection.HTTP_OK).entity(
                new NeutronFirewallPolicyRequest(ans)).build();
    }

    /**
     * Returns a specific Firewall Policy */

    @Path("{firewallPolicyUUID}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAUTHORIZED, condition = "Unauthorized"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_FOUND, condition = "Not Found"),
            @ResponseCode(code = HttpURLConnection.HTTP_NOT_IMPLEMENTED, condition = "Not Implemented"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response showFirewallPolicy(@PathParam("firewallPolicyUUID") String firewallPolicyUUID,
                                      // return fields
                                      @QueryParam("fields") List<String> fields) {
        INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronInterfaces().getFirewallPolicyInterface();
        if (!firewallPolicyInterface.neutronFirewallPolicyExists(firewallPolicyUUID)) {
            throw new ResourceNotFoundException(uuidNoExist(RESOURCE_NAME));
        }
        if (fields.size() > 0) {
            NeutronFirewallPolicy ans = firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID);
            return Response.status(HttpURLConnection.HTTP_OK).entity(
                    new NeutronFirewallPolicyRequest(extractFields(ans, fields))).build();
        } else {
            return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFirewallPolicyRequest(firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID))).build();
        }
    }

    /**
     * Creates new Firewall Policy
     * */
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_CREATED, condition = "Created"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response createFirewallPolicies(final NeutronFirewallPolicyRequest input) {
        INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronInterfaces().getFirewallPolicyInterface();
        if (input.isSingleton()) {
            NeutronFirewallPolicy singleton = input.getSingleton();

            Object[] instances = NeutronUtil.getInstances(INeutronFirewallPolicyAware.class, this);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                    int status = service.canCreateNeutronFirewallPolicy(singleton);
                    if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                        return Response.status(status).build();
                    }
                }
            }
            firewallPolicyInterface.addNeutronFirewallPolicy(singleton);
            if (instances != null) {
                for (Object instance : instances) {
                    INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                    service.neutronFirewallPolicyCreated(singleton);
                }
            }
        } else {
            Object[] instances = NeutronUtil.getInstances(INeutronFirewallPolicyAware.class, this);
            if (instances != null) {
                for (NeutronFirewallPolicy test : input.getBulk()) {
                    for (Object instance : instances) {
                        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                        int status = service.canCreateNeutronFirewallPolicy(test);
                        if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                            return Response.status(status).build();
                        }
                    }
                }
            }
            /*
             * now, each element of the bulk request can be added to the cache
             */
            for (NeutronFirewallPolicy test : input.getBulk()) {
                firewallPolicyInterface.addNeutronFirewallPolicy(test);
                if (instances != null) {
                    for (Object instance : instances) {
                        INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                        service.neutronFirewallPolicyCreated(test);
                    }
                }
            }
        }
        return Response.status(HttpURLConnection.HTTP_CREATED).entity(input).build();
    }

    /**
     * Updates a Firewall Policy
     */
    @Path("{firewallPolicyUUID}")
    @PUT
    @Produces({ MediaType.APPLICATION_JSON })
    @Consumes({ MediaType.APPLICATION_JSON })
    //@TypeHint(OpenStackSubnets.class)
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_OK, condition = "Operation successful"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response updateFirewallPolicy(
            @PathParam("firewallPolicyUUID") String firewallPolicyUUID, final NeutronFirewallPolicyRequest input) {
        INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronInterfaces().getFirewallPolicyInterface();

        NeutronFirewallPolicy delta = input.getSingleton();
        NeutronFirewallPolicy original = firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID);

        Object[] instances = NeutronUtil.getInstances(INeutronFirewallPolicyAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                int status = service.canUpdateNeutronFirewallPolicy(delta, original);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        /*
         * update the object and return it
         */
        firewallPolicyInterface.updateNeutronFirewallPolicy(firewallPolicyUUID, delta);
        NeutronFirewallPolicy updatedFirewallPolicy = firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                service.neutronFirewallPolicyUpdated(updatedFirewallPolicy);
            }
        }
        return Response.status(HttpURLConnection.HTTP_OK).entity(new NeutronFirewallPolicyRequest(firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID))).build();
    }

    /**
     * Deletes a Firewall Policy */

    @Path("{firewallPolicyUUID}")
    @DELETE
    @StatusCodes({
            @ResponseCode(code = HttpURLConnection.HTTP_NO_CONTENT, condition = "No Content"),
            @ResponseCode(code = HttpURLConnection.HTTP_UNAVAILABLE, condition = "No providers available") })
    public Response deleteFirewallPolicy(
            @PathParam("firewallPolicyUUID") String firewallPolicyUUID) {
        final INeutronFirewallPolicyCRUD firewallPolicyInterface = getNeutronInterfaces().getFirewallPolicyInterface();

        NeutronFirewallPolicy singleton = firewallPolicyInterface.getNeutronFirewallPolicy(firewallPolicyUUID);
        Object[] instances = NeutronUtil.getInstances(INeutronFirewallPolicyAware.class, this);
        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                int status = service.canDeleteNeutronFirewallPolicy(singleton);
                if (status < HTTP_OK_BOTTOM || status > HTTP_OK_TOP) {
                    return Response.status(status).build();
                }
            }
        }

        deleteUuid(RESOURCE_NAME, firewallPolicyUUID,
                   new Remover() {
                       public boolean remove(String uuid) {
                           return firewallPolicyInterface.removeNeutronFirewallPolicy(uuid);
                       }
                   });

        if (instances != null) {
            for (Object instance : instances) {
                INeutronFirewallPolicyAware service = (INeutronFirewallPolicyAware) instance;
                service.neutronFirewallPolicyDeleted(singleton);
            }
        }
        return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
    }
}
