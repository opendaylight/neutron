/*
 * Copyright (c) 2014, 2015 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.transcriber;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.NeutronLoadBalancer;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.Loadbalancers;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.loadbalancers.Loadbalancer;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.loadbalancers.LoadbalancerBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.lbaasv2.rev150712.lbaas.attributes.loadbalancers.LoadbalancerKey;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;

@Singleton
@OsgiServiceProvider(classes = INeutronLoadBalancerCRUD.class)
public final class NeutronLoadBalancerInterface
        extends AbstractNeutronInterface<Loadbalancer, Loadbalancers, LoadbalancerKey, NeutronLoadBalancer>
        implements INeutronLoadBalancerCRUD {

    @Inject
    public NeutronLoadBalancerInterface(DataBroker db) {
        super(LoadbalancerBuilder.class, db);
    }

    @Override
    protected List<Loadbalancer> getDataObjectList(Loadbalancers lbs) {
        return lbs.getLoadbalancer();
    }

    @Override
    protected NeutronLoadBalancer fromMd(Loadbalancer loadBalancer) {
        final NeutronLoadBalancer answer = new NeutronLoadBalancer();
        fromMdAdminAttributes(loadBalancer, answer);
        if (loadBalancer.getVipAddress() != null) {
            answer.setLoadBalancerVipAddress(String.valueOf(loadBalancer.getVipAddress().getValue()));
        }
        if (loadBalancer.getVipSubnetId() != null) {
            answer.setLoadBalancerVipSubnetID(loadBalancer.getVipSubnetId().getValue());
        }
        return answer;
    }

    @Override
    protected Loadbalancer toMd(NeutronLoadBalancer loadBalancer) {
        final LoadbalancerBuilder loadBalancerBuilder = new LoadbalancerBuilder();
        toMdAdminAttributes(loadBalancer, loadBalancerBuilder);
        if (loadBalancer.getLoadBalancerVipAddress() != null) {
            loadBalancerBuilder.setVipAddress(new IpAddress(loadBalancer.getLoadBalancerVipAddress().toCharArray()));
        }
        if (loadBalancer.getLoadBalancerVipSubnetID() != null) {
            loadBalancerBuilder.setVipSubnetId(toUuid(loadBalancer.getLoadBalancerVipSubnetID()));
        }
        return loadBalancerBuilder.build();
    }
}
