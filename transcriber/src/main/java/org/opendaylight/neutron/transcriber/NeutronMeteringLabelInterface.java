/*
 * Copyright IBM Corporation, 2015.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.transcriber;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.NeutronMeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.MeteringLabels;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.metering.labels.MeteringLabel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.metering.rev141002.metering.labels.attributes.metering.labels.MeteringLabelBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150325.Neutron;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeutronMeteringLabelInterface extends AbstractNeutronInterface<MeteringLabel, NeutronMeteringLabel>  implements INeutronMeteringLabelCRUD {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronMeteringLabelInterface.class);
    private ConcurrentMap<String, NeutronMeteringLabel> meteringLabelDB = new ConcurrentHashMap<String, NeutronMeteringLabel>();

    NeutronMeteringLabelInterface(ProviderContext providerContext) {
        super(providerContext);
    }

    // this method uses reflection to update an object from it's delta.

    private boolean overwrite(Object target, Object delta) {
        Method[] methods = target.getClass().getMethods();

        for(Method toMethod: methods){
            if(toMethod.getDeclaringClass().equals(target.getClass())
                    && toMethod.getName().startsWith("set")){

                String toName = toMethod.getName();
                String fromName = toName.replace("set", "get");

                try {
                    Method fromMethod = delta.getClass().getMethod(fromName);
                    Object value = fromMethod.invoke(delta, (Object[])null);
                    if(value != null){
                        toMethod.invoke(target, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }

    // IfNBMeteringLabelCRUD methods

    @Override
    public boolean neutronMeteringLabelExists(String uuid) {
        return meteringLabelDB.containsKey(uuid);
    }

    @Override
    public NeutronMeteringLabel getNeutronMeteringLabel(String uuid) {
        if (!neutronMeteringLabelExists(uuid)) {
            return null;
        }
        return meteringLabelDB.get(uuid);
    }

    @Override
    public List<NeutronMeteringLabel> getAllNeutronMeteringLabels() {
        Set<NeutronMeteringLabel> allMeteringLabels = new HashSet<NeutronMeteringLabel>();
        for (Entry<String, NeutronMeteringLabel> entry : meteringLabelDB.entrySet()) {
            NeutronMeteringLabel meteringLabel = entry.getValue();
            allMeteringLabels.add(meteringLabel);
        }
        LOGGER.debug("Exiting getAllMeteringLabels, Found {} OpenStackMeteringLabels", allMeteringLabels.size());
        List<NeutronMeteringLabel> ans = new ArrayList<NeutronMeteringLabel>();
        ans.addAll(allMeteringLabels);
        return ans;
    }

    @Override
    public boolean addNeutronMeteringLabel(NeutronMeteringLabel input) {
        if (neutronMeteringLabelExists(input.getMeteringLabelUUID())) {
            return false;
        }
        meteringLabelDB.putIfAbsent(input.getMeteringLabelUUID(), input);
      //TODO: add code to find INeutronMeteringLabelAware services and call newtorkCreated on them
        return true;
    }

    @Override
    public boolean removeNeutronMeteringLabel(String uuid) {
        if (!neutronMeteringLabelExists(uuid)) {
            return false;
        }
        meteringLabelDB.remove(uuid);
      //TODO: add code to find INeutronMeteringLabelAware services and call newtorkDeleted on them
        return true;
    }

    @Override
    public boolean updateNeutronMeteringLabel(String uuid, NeutronMeteringLabel delta) {
        if (!neutronMeteringLabelExists(uuid)) {
            return false;
        }
        NeutronMeteringLabel target = meteringLabelDB.get(uuid);
        return overwrite(target, delta);
    }

    @Override
    public boolean neutronMeteringLabelInUse(String netUUID) {
        if (!neutronMeteringLabelExists(netUUID)) {
            return true;
        }
        return false;
    }

    @Override
        protected InstanceIdentifier<MeteringLabel> createInstanceIdentifier(
                        MeteringLabel item) {
                return InstanceIdentifier.create(Neutron.class).child(MeteringLabels.class).child(MeteringLabel.class);

        }

        @Override
        protected MeteringLabel toMd(NeutronMeteringLabel meteringLabel) {
      MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
      if (meteringLabel.getMeteringLabelName()!=null) {
                        meteringLabelBuilder.setName(meteringLabel.getMeteringLabelName());
                }
      if (meteringLabel.getMeteringLabelDescription()!=null) {
                        meteringLabelBuilder.setDescription(meteringLabel.getMeteringLabelDescription());
                }
      if (meteringLabel.getMeteringLabelTenantID()!=null && !meteringLabel.getMeteringLabelTenantID().isEmpty()) {
                        meteringLabelBuilder.setTenantId(toUuid(meteringLabel.getMeteringLabelTenantID()));
                }
      if (meteringLabel.getMeteringLabelUUID()!=null) {
                        meteringLabelBuilder.setUuid(toUuid(meteringLabel.getMeteringLabelUUID()));
                }
                return meteringLabelBuilder.build();
        }

        @Override
        protected MeteringLabel toMd(String uuid) {
            MeteringLabelBuilder meteringLabelBuilder = new MeteringLabelBuilder();
      meteringLabelBuilder.setUuid(toUuid(uuid));
      return meteringLabelBuilder.build();
        }
}
