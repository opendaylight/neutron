/*
 * Copyright (c) 2017 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "trunk")
@XmlAccessorType(XmlAccessType.NONE)
public final class NeutronTrunk extends NeutronAdminAttributes<NeutronTrunk> implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(NeutronTrunk.class);
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "port_id")
    String portId;

    @XmlElement(name = "sub_ports")
    List<NeutronTrunkSubPort> subPorts;

    public NeutronTrunk() {
    }

    @Override
    public void initDefaults() {
        // In order to override super.initDefaults()
        // status needs to be checked before calling super.initDefaults()
        if (status == null) {
            status = "DOWN";
        }
        super.initDefaults();
        if (subPorts == null) {
            subPorts = new ArrayList<>();
        }
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public List<NeutronTrunkSubPort> getSubPorts() {
        return subPorts;
    }

    public void setSubPorts(List<NeutronTrunkSubPort> subPorts) {
        this.subPorts = subPorts;
    }



    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStackTrunk object with only the selected fields
     *             populated
     */

    @Override
    public NeutronTrunk extractFields(List<String> fields) {
        NeutronTrunk ans = new NeutronTrunk();
        for (String s : fields) {
            if (extractField(s, ans)) {
                continue;
            }
            switch (s) {
                case "port_id":
                    ans.setPortId(this.getPortId());
                    break;
                case "sub_ports":
                    List<NeutronTrunkSubPort> subPortList = new ArrayList<>();
                    subPortList.addAll(this.getSubPorts());
                    ans.setSubPorts(subPortList);
                    break;
                default:
                    LOG.warn("{} is not an NeutronTrunk suitable field.", s);
                    break;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "NeutronTrunk{" + "trunkUUID='" + uuid + '\'' + ", trunkName='" + name + '\''
                + ", tenantID='" + tenantID + '\'' + ", adminStateUp='" + adminStateUp + '\''
                + ", status='" + status + '\'' + ", portId='" + portId + '\''
                + ", subPorts='" + subPorts + '\'' + '}';
    }
}
