/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.StringReader;
import java.util.HashMap;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object jaxbUnmarshall(Object schemaObject, String json) throws JAXBException {
        Class cls = schemaObject.getClass();
        Class[] types = new Class[1];
        types[0] = cls;
        Map<String, String> namespacePrefixMapper = new HashMap<>(3);
        namespacePrefixMapper.put("router", "router");
        namespacePrefixMapper.put("provider", "provider");
        namespacePrefixMapper.put("binding", "binding");
        Map<String, Object> jaxbProperties = new HashMap<>(2);
        jaxbProperties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        jaxbProperties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        jaxbProperties.put(JAXBContextProperties.JSON_NAMESPACE_SEPARATOR, ':');
        jaxbProperties.put(JAXBContextProperties.NAMESPACE_PREFIX_MAPPER, namespacePrefixMapper);
        JAXBContext jc = JAXBContext.newInstance(types, jaxbProperties);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.JSON_NAMESPACE_PREFIX_MAPPER, namespacePrefixMapper);

        StringReader reader = new StringReader(json);
        StreamSource stream = new StreamSource(reader);
        return unmarshaller.unmarshal(stream, cls).getValue();
    }
}
