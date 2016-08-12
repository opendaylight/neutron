/*
 * Copyright (c) 2013, 2015 IBM Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronCRUDInterfaces {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronCRUDInterfaces.class);

    private INeutronNetworkCRUD networkInterface;
    private INeutronSubnetCRUD subnetInterface;
    private INeutronPortCRUD portInterface;
    private INeutronRouterCRUD routerInterface;
    private INeutronFloatingIPCRUD fipInterface;
    private INeutronSecurityGroupCRUD sgInterface;
    private INeutronSecurityRuleCRUD srInterface;
    private INeutronFirewallCRUD fwInterface;
    private INeutronFirewallPolicyCRUD fwpInterface;
    private INeutronFirewallRuleCRUD fwrInterface;
    private INeutronLoadBalancerCRUD lbInterface;
    private INeutronLoadBalancerPoolCRUD lbpInterface;
    private INeutronLoadBalancerListenerCRUD lblInterface;
    private INeutronLoadBalancerHealthMonitorCRUD lbhmInterface;
    private INeutronMeteringLabelCRUD mlInterface;
    private INeutronMeteringLabelRuleCRUD mlrInterface;
    private INeutronVPNIKEPolicyCRUD ikepInterface;
    private INeutronVPNIPSECPolicyCRUD ipsecpInterface;
    private INeutronVPNServiceCRUD vpnInterface;
    private INeutronVPNIPSECSiteConnectionsCRUD ipsecScInterface;
    private INeutronBgpvpnCRUD bgpvpnInterface;
    private INeutronL2gatewayCRUD l2gatewayInterface;
    private INeutronL2gatewayConnectionCRUD l2gatewayConnectionInterface;
    private INeutronSFCFlowClassifierCRUD sfcFlowClassifierInterface;
    private INeutronSFCPortPairCRUD sfcPortPairInterface;
    private INeutronSFCPortPairGroupCRUD sfcPortPairGroupInterface;
    private INeutronSFCPortChainCRUD sfcPortChainInterface;
    private INeutronQosPolicyCRUD qospInterface;

    public NeutronCRUDInterfaces() {
    }

    public INeutronNetworkCRUD getNetworkInterface() {
        return networkInterface;
    }

    public INeutronSubnetCRUD getSubnetInterface() {
        return subnetInterface;
    }

    public INeutronPortCRUD getPortInterface() {
        return portInterface;
    }

    public INeutronRouterCRUD getRouterInterface() {
        return routerInterface;
    }

    public INeutronFloatingIPCRUD getFloatingIPInterface() {
        return fipInterface;
    }

    public INeutronSecurityGroupCRUD getSecurityGroupInterface() {
        return sgInterface;
    }

    public INeutronSecurityRuleCRUD getSecurityRuleInterface() {
        return srInterface;
    }

    public INeutronFirewallCRUD getFirewallInterface() {
        return fwInterface;
    }

    public INeutronFirewallPolicyCRUD getFirewallPolicyInterface() {
        return fwpInterface;
    }

    public INeutronFirewallRuleCRUD getFirewallRuleInterface() {
        return fwrInterface;
    }

    public INeutronLoadBalancerCRUD getLoadBalancerInterface() {
        return lbInterface;
    }

    public INeutronLoadBalancerPoolCRUD getLoadBalancerPoolInterface() {
        return lbpInterface;
    }

    public INeutronLoadBalancerListenerCRUD getLoadBalancerListenerInterface() {
        return lblInterface;
    }

    public INeutronLoadBalancerHealthMonitorCRUD getLoadBalancerHealthMonitorInterface() {
        return lbhmInterface;
    }

    public INeutronMeteringLabelCRUD getMeteringLabelInterface() {
        return mlInterface;
    }

    public INeutronMeteringLabelRuleCRUD getMeteringLabelRuleInterface() {
        return mlrInterface;
    }

    public INeutronVPNIKEPolicyCRUD getVPNIKEPolicyInterface() {
        return ikepInterface;
    }

    public INeutronVPNIPSECPolicyCRUD getVPNIPSECPolicyInterface() {
        return ipsecpInterface;
    }

    public INeutronL2gatewayCRUD getL2gatewayInterface() {
        return l2gatewayInterface;
    }

    public INeutronL2gatewayConnectionCRUD getL2gatewayConnectionInterface() {
        return l2gatewayConnectionInterface;
    }

    public void setVPNServiceInterface(INeutronVPNServiceCRUD iface) {
        vpnInterface = iface;
    }

    public INeutronVPNServiceCRUD getVPNServiceInterface() {
        return vpnInterface;
    }

    public INeutronVPNIPSECSiteConnectionsCRUD getVPNIPSECSiteConnectionsInterface() {
        return ipsecScInterface;
    }

    public INeutronBgpvpnCRUD getBgpvpnInterface() {
        return bgpvpnInterface;
    }

    public INeutronSFCFlowClassifierCRUD getSFCFlowClassifierInterface() {
        return sfcFlowClassifierInterface;
    }

    public INeutronSFCPortPairCRUD getSFCPortPairInterface() {
        return sfcPortPairInterface;
    }

    public INeutronSFCPortPairGroupCRUD getSFCPortPairGroupInterface() {
        return sfcPortPairGroupInterface;
    }

    public INeutronSFCPortChainCRUD getSFCPortChainInterface() {
        return sfcPortChainInterface;
    }

    public INeutronQosPolicyCRUD getQosPolicyInterface() {
        return qospInterface;
    }

    public NeutronCRUDInterfaces fetchINeutronNetworkCRUD(Object obj) {
        networkInterface = (INeutronNetworkCRUD) getInstances(INeutronNetworkCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSubnetCRUD(Object obj) {
        subnetInterface = (INeutronSubnetCRUD) getInstances(INeutronSubnetCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronPortCRUD(Object obj) {
        portInterface = (INeutronPortCRUD) getInstances(INeutronPortCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronRouterCRUD(Object obj) {
        routerInterface = (INeutronRouterCRUD) getInstances(INeutronRouterCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronFloatingIPCRUD(Object obj) {
        fipInterface = (INeutronFloatingIPCRUD) getInstances(INeutronFloatingIPCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSecurityGroupCRUD(Object obj) {
        sgInterface = (INeutronSecurityGroupCRUD) getInstances(INeutronSecurityGroupCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSecurityRuleCRUD(Object obj) {
        srInterface = (INeutronSecurityRuleCRUD) getInstances(INeutronSecurityRuleCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronFirewallCRUD(Object obj) {
        fwInterface = (INeutronFirewallCRUD) getInstances(INeutronFirewallCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronFirewallPolicyCRUD(Object obj) {
        fwpInterface = (INeutronFirewallPolicyCRUD) getInstances(INeutronFirewallPolicyCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronFirewallRuleCRUD(Object obj) {
        fwrInterface = (INeutronFirewallRuleCRUD) getInstances(INeutronFirewallRuleCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronLoadBalancerCRUD(Object obj) {
        lbInterface = (INeutronLoadBalancerCRUD) getInstances(INeutronLoadBalancerCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronLoadBalancerPoolCRUD(Object obj) {
        lbpInterface = (INeutronLoadBalancerPoolCRUD) getInstances(INeutronLoadBalancerPoolCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronLoadBalancerListenerCRUD(Object obj) {
        lblInterface = (INeutronLoadBalancerListenerCRUD) getInstances(INeutronLoadBalancerListenerCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronLoadBalancerHealthMonitorCRUD(Object obj) {
        lbhmInterface = (INeutronLoadBalancerHealthMonitorCRUD) getInstances(
                INeutronLoadBalancerHealthMonitorCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronMeteringLabelCRUD(Object obj) {
        mlInterface = (INeutronMeteringLabelCRUD) getInstances(INeutronMeteringLabelCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronMeteringLabelRuleCRUD(Object obj) {
        mlrInterface = (INeutronMeteringLabelRuleCRUD) getInstances(INeutronMeteringLabelRuleCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronVPNIKEPolicyCRUD(Object obj) {
        ikepInterface = (INeutronVPNIKEPolicyCRUD) getInstances(INeutronVPNIKEPolicyCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronVPNIPSECPolicyCRUD(Object obj) {
        ipsecpInterface = (INeutronVPNIPSECPolicyCRUD) getInstances(INeutronVPNIPSECPolicyCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronVPNServiceCRUD(Object obj) {
        vpnInterface = (INeutronVPNServiceCRUD) getInstances(INeutronVPNServiceCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronVPNIPSECSiteConnectionsCRUD(Object obj) {
        ipsecScInterface = (INeutronVPNIPSECSiteConnectionsCRUD) getInstances(INeutronVPNIPSECSiteConnectionsCRUD.class,
                obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronBgpvpnCRUD(Object obj) {
        bgpvpnInterface = (INeutronBgpvpnCRUD) getInstances(INeutronBgpvpnCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronL2gatewayCRUD(Object obj) {
        l2gatewayInterface = (INeutronL2gatewayCRUD) getInstances(INeutronL2gatewayCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronL2gatewayConnectionCRUD(Object obj) {
        l2gatewayConnectionInterface = (INeutronL2gatewayConnectionCRUD) getInstances(
                INeutronL2gatewayConnectionCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSFCFlowClassifierCRUD(Object obj) {
        sfcFlowClassifierInterface = (INeutronSFCFlowClassifierCRUD) getInstances(INeutronSFCFlowClassifierCRUD.class,
                obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSFCPortPairCRUD(Object obj) {
        sfcPortPairInterface = (INeutronSFCPortPairCRUD) getInstances(INeutronSFCPortPairCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSFCPortPairGroupCRUD(Object obj) {
        sfcPortPairGroupInterface = (INeutronSFCPortPairGroupCRUD) getInstances(INeutronSFCPortPairGroupCRUD.class,
                obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronSFCPortChainCRUD(Object obj) {
        sfcPortChainInterface = (INeutronSFCPortChainCRUD) getInstances(INeutronSFCPortChainCRUD.class, obj);
        return this;
    }

    public NeutronCRUDInterfaces fetchINeutronQosPolicyCRUD(Object obj) {
        qospInterface = (INeutronQosPolicyCRUD) getInstances(INeutronQosPolicyCRUD.class, obj);
        return this;
    }

    public Object getInstances(Class<?> clazz, Object bundle) {
        try {
            BundleContext bCtx = FrameworkUtil.getBundle(bundle.getClass()).getBundleContext();

            ServiceReference<?>[] services = null;
            services = bCtx.getServiceReferences(clazz.getName(), null);
            if (services != null) {
                return bCtx.getService(services[0]);
            }
        } catch (Exception e) {
            LOGGER.error("Error in getInstances", e);
        }
        return null;
    }
}
