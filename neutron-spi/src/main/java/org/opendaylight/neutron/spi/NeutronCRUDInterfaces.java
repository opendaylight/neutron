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

public final class NeutronCRUDInterfaces {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeutronCRUDInterfaces.class);

    public NeutronCRUDInterfaces() {
    }

    public static <T extends INeutronObject<T>, I extends INeutronCRUD<T>> I fetchINeutronCRUD(
        Class<I> cls, Object obj) {
        return (I) getInstances(cls, obj);
    }

    public static Object getInstances(Class<?> clazz, Object bundle) {
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
