/*
 * Copyright (C) 2015 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.io.StringReader;

import javax.xml.bind.JAXBException;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONUnmarshaller;

public class JaxbTestHelper {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object jaxbUnmarshall(Object schemaObject, String json) throws JAXBException {
        Class c = schemaObject.getClass();
        Class[] types = new Class[1];
        types[0] = c;
        JSONJAXBContext context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
        JSONUnmarshaller unmarshaller = context.createJSONUnmarshaller();
        StringReader reader = new StringReader(json);
        return unmarshaller.unmarshalFromJSON(reader, c);
    }
}
