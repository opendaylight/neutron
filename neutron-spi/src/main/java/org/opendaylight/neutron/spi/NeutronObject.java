/*
 * Copyright (c) 2015 Intel Corporation  All rights reserved.
 * Copyright (c) 2015 Isaku Yamahata  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class NeutronObject<T extends NeutronObject<T>> extends NeutronID implements INeutronObject<T> {
    // T extends NeutronObject as 0th type argument. Used by extractFields()
    private static final int NEUTRON_OBJECT_CLASS_TYPE_INDEX = 0;

    private static final Logger LOG = LoggerFactory.getLogger(NeutronFirewallRule.class);

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "tenant_id")
    String tenantID;

    @XmlElement(name = "project_id")
    String projectID;

    @XmlElement(name = "revision_number")
    Long revisionNumber;

    public NeutronObject() {
    }

    @Override
    public String getTenantID() {
        if (tenantID != null && tenantID.isEmpty()) {
            // Bug 4775 - Treat empty string tenantId as null, so no attempt is made
            //            to turn it into a uuid.
            return null;
        }
        return tenantID;
    }

    @Override
    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    @Override
    @XmlTransient
    public void setTenantID(Uuid tenantID) {
        this.tenantID = tenantID.getValue().replace("-", "");
    }

    @Override
    public String toString() {
        return "NeutronObject [id=" + uuid + ", tenantID=" + tenantID + "]";
    }

    @Override
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    @Override
    public String getProjectID() {
        return this.projectID;
    }

    @Override
    public Long getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @Override
    public void initDefaults() {
        if (projectID != null && tenantID == null) {
            tenantID = projectID;
        }
        if (projectID == null && tenantID != null) {
            projectID = tenantID;
        }
    }

    /**
     * This method copies selected fields from the object and returns them
     * as a new object, suitable for marshaling.
     *
     * @param fields
     *            List of attributes to be extracted
     * @return an OpenStack Neutron object with only the selected fields
     *             populated
     */

    @Override
    public T extractFields(List<String> fields) {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        @SuppressWarnings("unchecked")
        Class<T> cls = (Class<T>) types[NEUTRON_OBJECT_CLASS_TYPE_INDEX];
        T ans;
        try {
            ans = cls.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            // should not happen.
            throw new IllegalStateException(e);
        }
        for (String s : fields) {
            if (!extractField(s, ans)) {
                LOG.warn("Unknown {} {}.", cls.getSimpleName(), s);
            }
        }
        return ans;
    }

    protected boolean extractField(String field, T ans) {
        switch (field) {
            case "id":
                ans.setID(this.getID());
                return true;
            case "tenant_id":
                ans.setTenantID(this.getTenantID());
                return true;
            case "project_id":
                ans.setProjectID(this.getProjectID());
                return true;
            case "revision_number":
                ans.setRevisionNumber(this.getRevisionNumber());
                return true;
            default:
                return false;
        }
    }
}
