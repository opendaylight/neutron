/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.northbound.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ws.rs.core.UriInfo;
import org.opendaylight.neutron.spi.INeutronObject;
import org.opendaylight.neutron.spi.NeutronNetwork;
import org.opendaylight.neutron.spi.NeutronObject;
import org.opendaylight.neutron.spi.NeutronPort;
import org.opendaylight.neutron.spi.NeutronSubnet;

public final class PaginatedRequestFactory {

    private static final Comparator<INeutronObject> NEUTRON_OBJECT_COMPARATOR = new Comparator<INeutronObject>() {
        @Override
        public int compare(INeutronObject o1, INeutronObject o2) {
            return o1.getID().compareTo(o2.getID());
        }
    };

    public static class PaginationResults<T extends INeutronObject> {
        List<T> collection;
        List<NeutronPageLink> links;

        public PaginationResults(List<T> collection, List<NeutronPageLink> links) {
            this.collection = collection;
            this.links = links;
        }
    }

    private static final class MarkerObject extends NeutronObject implements INeutronObject {
        private final String id;

        MarkerObject(String id) {
            super();
            this.id = id;
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public void setID(String id) {
            throw new UnsupportedOperationException("Marker has constant ID");
        }
    }

    private PaginatedRequestFactory() {
    }

    /*
     * SuppressWarnings is needed because the compiler does not understand that we
     * are actually safe here.
     *
     * FIXME: the only caller performs a cast back, so this is not actually necessary.
     */
    @SuppressWarnings("unchecked")
    public static <T extends INeutronObject> INeutronRequest<T> createRequest(Integer limit, String marker,
                                                                           Boolean pageReverse,
                                                                           UriInfo uriInfo,
                                                                           List<T> collection,
                                                                           Class<T> clazz) {
        PaginationResults<T> results = paginate(limit, marker, pageReverse, uriInfo, collection);

        if (clazz.equals(NeutronNetwork.class)){
            return (INeutronRequest<T>) new NeutronNetworkRequest((List<NeutronNetwork>) results.collection, results.links);
        }
        if (clazz.equals(NeutronSubnet.class)){
            return (INeutronRequest<T>) new NeutronSubnetRequest((List<NeutronSubnet>) results.collection, results.links);
        }
        if (clazz.equals(NeutronPort.class)){
            return (INeutronRequest<T>) new NeutronPortRequest((List<NeutronPort>) results.collection, results.links);
        }
        return null;
    }

    private static <T extends INeutronObject> PaginationResults<T> paginate(Integer limit, String marker, Boolean pageReverse, UriInfo uriInfo, List<T> collection) {
        List<NeutronPageLink> links = new ArrayList<>();
        final int startPos;
        String startMarker;
        String endMarker;
        Boolean firstPage = false;
        Boolean lastPage = false;
        List<T> lCollection = collection;

        Collections.sort(lCollection, NEUTRON_OBJECT_COMPARATOR);

        if (marker != null) {
            int offset = Collections.binarySearch(lCollection, new MarkerObject(marker), NEUTRON_OBJECT_COMPARATOR);
            if (offset < 0) {
                throw new ResourceNotFoundException("UUID for marker: " + marker + " could not be found");
            }

            if (!pageReverse) {
                startPos = offset + 1;
            }
            else {
                startPos = offset - limit;
            }
        }
        else {
            startPos = 0;
        }

        if (startPos == 0){
            firstPage = true;
        }

        if (startPos + limit >= lCollection.size()) {
            lCollection = lCollection.subList(startPos, lCollection.size());
            startMarker = lCollection.get(0).getID();
            endMarker = lCollection.get(lCollection.size() - 1).getID();
            lastPage = true;
        }
        else if (startPos < 0) {
            if (startPos + limit > 0) {
                lCollection = lCollection.subList(0, startPos + limit);
                startMarker = lCollection.get(0).getID();
                endMarker = lCollection.get(lCollection.size() - 1).getID();
                firstPage = true;
            }
            else {
                throw new BadRequestException("Requested page is out of bounds. Please check the supplied limit and marker");
            }
        }
        else {
            lCollection = lCollection.subList(startPos, startPos + limit);
            startMarker = lCollection.get(0).getID();
            endMarker = lCollection.get(limit-1).getID();
        }

        if (!lastPage) {
            NeutronPageLink next = new NeutronPageLink();
            next.setRef("next");
            next.setHref(uriInfo.getAbsolutePath().toString() + "?limit=" + limit.toString() + "&marker=" + endMarker);
            links.add(next);
        }

        if (!firstPage) {
            NeutronPageLink previous = new NeutronPageLink();
            previous.setRef("previous");
            previous.setHref(uriInfo.getAbsolutePath().toString() + "?limit=" + limit.toString() + "&marker=" + startMarker + "&page_reverse=True");
            links.add(previous);
        }

        return new PaginationResults<T>(lCollection, links);
    }
}
