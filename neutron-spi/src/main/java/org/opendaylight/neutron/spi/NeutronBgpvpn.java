/*
 * Copyright (c) 2015 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bgpvpn")
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronBgpvpn extends NeutronAdminAttributes<NeutronBgpvpn> implements Serializable {
    // See OpenStack Network API v2.0 Reference for description of
    // annotated attributes

    private static final long serialVersionUID = 1L;

    @XmlElement(defaultValue = "l3", name = "type")
    String type;

    @XmlElement(name = "technique")
    String technique;

    @XmlElement(name = "route_targets")
    List<String> routeTargets;

    @XmlElement(name = "import_targets")
    List<String> importTargets;

    @XmlElement(name = "export_targets")
    List<String> exportTargets;

    @XmlElement(name = "route_distinguishers")
    List<String> routeDistinguishers;

    @XmlElement(name = "vnid")
    Long vnid;

    @XmlElement(defaultValue = "false", name = "auto_aggregate")
    Boolean autoAggregate;

    @XmlElement(name = "networks")
    List<String> networks;

    @XmlElement(name = "routers")
    List<String> routers;

    /* This attribute lists the ports associated with an instance
     * which is needed for determining if that instance can be deleted
     */

    public NeutronBgpvpn() {
    }

    public void initDefaults() {
        if (type == null) {
            type = "l3";
        }
        if (status == null) {
            status = "ACTIVE";
        }
        if (adminStateUp == null) {
            adminStateUp = true;
        }
        if (autoAggregate == null) {
            autoAggregate = false;
        }
    }

    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    public boolean isAutoAggregate() {
        return autoAggregate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public List<String> getRouteTargets() {
        return routeTargets;
    }

    public void setRouteTargets(List<String> routeTargets) {
        this.routeTargets = routeTargets;
    }

    public void addRouteTarget(String uuid) {
        routeTargets.add(uuid);
    }

    public void removeRouteTarget(String uuid) {
        routeTargets.remove(uuid);
    }

    public List<String> getImportTargets() {
        return importTargets;
    }

    public void setImportTargets(List<String> importTargets) {
        this.importTargets = importTargets;
    }

    public void addImportTarget(String uuid) {
        importTargets.add(uuid);
    }

    public void removeImportTarget(String uuid) {
        importTargets.remove(uuid);
    }

    public List<String> getExportTargets() {
        return exportTargets;
    }

    public void setExportTargets(List<String> exportTargets) {
        this.exportTargets = exportTargets;
    }

    public void addExportTarget(String uuid) {
        exportTargets.add(uuid);
    }

    public void removeExportTarget(String uuid) {
        exportTargets.remove(uuid);
    }

    public List<String> getRouteDistinguishers() {
        return routeDistinguishers;
    }

    public void setRouteDistinguishers(List<String> routeDistinguishers) {
        this.routeDistinguishers = routeDistinguishers;
    }

    public void addRouteDistinguisher(String uuid) {
        routeDistinguishers.add(uuid);
    }

    public void removeRouteDistinguisher(String uuid) {
        routeDistinguishers.remove(uuid);
    }

    public Long getVnid() {
        return vnid;
    }

    public void setVnid(Long input) {
        vnid = input;
    }

    public Boolean getAutoAggregate() {
        return autoAggregate;
    }

    public void setAutoAggregate(boolean newValue) {
        autoAggregate = newValue;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }

    public void addNetwork(String uuid) {
        networks.add(uuid);
    }

    public void removeNetwork(String uuid) {
        networks.remove(uuid);
    }

    public List<String> getRouters() {
        return routers;
    }

    public void setRouters(List<String> routers) {
        this.routers = routers;
    }

    public void addRouter(String uuid) {
        routers.add(uuid);
    }

    public void removeRouter(String uuid) {
        routers.remove(uuid);
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackNetworks object with only the selected fields
     *             populated
     */

    public NeutronBgpvpn extractFields(List<String> fields) {
        NeutronBgpvpn ans = new NeutronBgpvpn();
        for (String s : fields) {
            extractField(s, ans);
            if (s.equals("type")) {
                ans.setType(this.getType());
            }
            if (s.equals("technique")) {
                ans.setTechnique(this.getTechnique());
            }
            if (s.equals("route_targets")) {
                ans.setRouteTargets(this.getRouteTargets());
            }
            if (s.equals("import_targets")) {
                ans.setImportTargets(this.getImportTargets());
            }
            if (s.equals("export_targets")) {
                ans.setExportTargets(this.getExportTargets());
            }
            if (s.equals("route_distinguishers")) {
                ans.setRouteDistinguishers(this.getRouteDistinguishers());
            }
            if (s.equals("routers")) {
                ans.setRouters(this.getRouters());
            }
            if (s.equals("networks")) {
                ans.setNetworks(this.getNetworks());
            }
            if (s.equals("vnid")) {
                ans.setVnid(this.getVnid());
            }
            if (s.equals("auto_aggregate")) {
                ans.setAutoAggregate(this.getAutoAggregate());
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronBgpvpn [bgpvpnUUID=" + uuid + ", bgpvpnName=" + name + ", adminStateUp=" + adminStateUp
                + ", status=" + status + ", tenantID=" + tenantID + ", type=" + type + ", technique=" + technique
                + ", routeTargets=" + routeTargets + ", importTargets=" + importTargets + ", exportTargets="
                + exportTargets + ", routeDistinguishers=" + routeDistinguishers + ", vnid = " + vnid
                + ", autoAggregate = " + autoAggregate + ", networks = " + networks + ", routers = " + routers + "]";
    }
}
