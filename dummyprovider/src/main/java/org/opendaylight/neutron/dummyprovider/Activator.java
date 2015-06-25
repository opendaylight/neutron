/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.dummyprovider;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.opendaylight.neutron.spi.INeutronFirewallAware;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyAware;
import org.opendaylight.neutron.spi.INeutronFirewallRuleAware;
import org.opendaylight.neutron.spi.INeutronFloatingIPAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolAware;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolMemberAware;
import org.opendaylight.neutron.spi.INeutronMeteringLabelAware;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleAware;
import org.opendaylight.neutron.spi.INeutronNetworkAware;
import org.opendaylight.neutron.spi.INeutronPortAware;
import org.opendaylight.neutron.spi.INeutronRouterAware;
import org.opendaylight.neutron.spi.INeutronSecurityGroupAware;
import org.opendaylight.neutron.spi.INeutronSecurityRuleAware;
import org.opendaylight.neutron.spi.INeutronSubnetAware;
import org.opendaylight.neutron.spi.INeutronVPNIKEPolicyAware;
import org.opendaylight.neutron.spi.INeutronVPNIPSECPolicyAware;
import org.opendaylight.neutron.spi.INeutronVPNIPSECSiteConnectionAware;
import org.opendaylight.neutron.spi.INeutronVPNServiceAware;

import org.osgi.framework.BundleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends DependencyActivatorBase {

    @Override
    public void init(BundleContext context, DependencyManager manager) throws Exception {
       manager.add(createComponent().setInterface(new String[] {
           INeutronFirewallAware.class.getName()}, null)
           .setImplementation(NeutronFirewallDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronFirewallPolicyAware.class.getName()}, null)
           .setImplementation(NeutronFirewallPolicyDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronFirewallRuleAware.class.getName()}, null)
           .setImplementation(NeutronFirewallRuleDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronFloatingIPAware.class.getName()}, null)
           .setImplementation(NeutronFloatingIPDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronLoadBalancerAware.class.getName()}, null)
           .setImplementation(NeutronLoadBalancerDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronLoadBalancerHealthMonitorAware.class.getName()}, null)
           .setImplementation(NeutronLoadBalancerHealthMonitorDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronLoadBalancerListenerAware.class.getName()}, null)
           .setImplementation(NeutronLoadBalancerListenerDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronLoadBalancerPoolAware.class.getName()}, null)
           .setImplementation(NeutronLoadBalancerPoolDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronLoadBalancerPoolMemberAware.class.getName()}, null)
           .setImplementation(NeutronLoadBalancerPoolMemberDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronMeteringLabelAware.class.getName()}, null)
           .setImplementation(NeutronMeteringLabelDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronMeteringLabelRuleAware.class.getName()}, null)
           .setImplementation(NeutronMeteringLabelRuleDummyProvider.class));

       manager.add(createComponent().setInterface(new String[] {
           INeutronNetworkAware.class.getName()}, null)
           .setImplementation(NeutronNetworkDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronPortAware.class.getName()}, null)
           .setImplementation(NeutronPortDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronRouterAware.class.getName()}, null)
           .setImplementation(NeutronRouterDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronSecurityGroupAware.class.getName()}, null)
           .setImplementation(NeutronSecurityGroupDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronSecurityRuleAware.class.getName()}, null)
           .setImplementation(NeutronSecurityRuleDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronSubnetAware.class.getName()}, null)
           .setImplementation(NeutronSubnetDummyProvider.class));

       manager.add(createComponent().setInterface(new String[] {
           INeutronVPNIKEPolicyAware.class.getName()}, null)
           .setImplementation(NeutronVPNIKEPolicyDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronVPNIPSECPolicyAware.class.getName()}, null)
           .setImplementation(NeutronVPNIPSECPolicyDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronVPNIPSECSiteConnectionAware.class.getName()}, null)
           .setImplementation(NeutronVPNIPSECSiteConnectionDummyProvider.class));
       manager.add(createComponent().setInterface(new String[] {
           INeutronVPNServiceAware.class.getName()}, null)
           .setImplementation(NeutronVPNServiceDummyProvider.class));
    }

    /**
     * Function called when the activator stops just before the
     * cleanup done by ComponentActivatorAbstractBase
     *
     */
    @Override
    public void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }

}
