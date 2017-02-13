package org.opendaylight.neutron.hostconfig.vpp;

import org.opendaylight.controller.md.sal.binding.test.AbstractDataBrokerTest;
import org.opendaylight.yang.gen.v1.urn.opendaylight.neutron.rev150712.Neutron;
import org.opendaylight.yangtools.yang.binding.YangModuleInfo;
import org.opendaylight.yangtools.yang.binding.util.BindingReflections;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class HostconfigsDataBrokerTest extends AbstractDataBrokerTest {

    @Override
    protected Iterable<YangModuleInfo> getModuleInfos() throws Exception {
        Builder<YangModuleInfo> moduleInfoSet = ImmutableSet.<YangModuleInfo>builder();
        for(Class<?> moduleClass : ImmutableList.<Class<?>>of(Neutron.class)) {
            YangModuleInfo moduleInfo = BindingReflections.getModuleInfo(moduleClass);
            Preconditions.checkNotNull(moduleInfo, "Module Info for %s is not available.", moduleClass);
            collectYangModuleInfo(moduleInfo, moduleInfoSet);
        }
        return moduleInfoSet.build();
    }

    private static void collectYangModuleInfo(final YangModuleInfo moduleInfo,
            final Builder<YangModuleInfo> moduleInfoSet) {
        moduleInfoSet.add(moduleInfo);
        for (YangModuleInfo dependency : moduleInfo.getImportedModules()) {
            collectYangModuleInfo(dependency, moduleInfoSet);
        }
    }
}
