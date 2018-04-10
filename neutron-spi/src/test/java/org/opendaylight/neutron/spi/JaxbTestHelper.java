/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

public final class JaxbTestHelper {

    private JaxbTestHelper() {
    }

    public static <T> T jaxbUnmarshall(Class<T> schemaClass, String json) throws JAXBException {
        Map<String, String> namespacePrefixMapper = new HashMap<>(3);
        namespacePrefixMapper.put("router", "router");
        namespacePrefixMapper.put("provider", "provider");
        namespacePrefixMapper.put("binding", "binding");

        Map<String, Object> jaxbProperties = new HashMap<>(2);
        jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        jaxbProperties.put(JAXBContextProperties.JSON_NAMESPACE_SEPARATOR, ':');
        jaxbProperties.put(JAXBContextProperties.NAMESPACE_PREFIX_MAPPER, namespacePrefixMapper);

        List<Class<T>> classesToBeBound = Collections.singletonList(schemaClass);
        JAXBContext jc = JAXBContext.newInstance(classesToBeBound.toArray(new Class[0]), jaxbProperties);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.JSON_NAMESPACE_PREFIX_MAPPER, namespacePrefixMapper);

        try (StringReader reader = new StringReader(json)) {
            StreamSource stream = new StreamSource(reader);
            T object = unmarshaller.unmarshal(stream, schemaClass).getValue();
            if (object == null) {
                throw new IllegalStateException(
                        "unmarshal() returned null for arguments schemaClass=" + schemaClass + ", json: " + json);
            } else {
                return object;
            }
        }
    }
}
