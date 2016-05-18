/*
 * Copyright (c) 2016 Intel Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class NeutronPort_VIFAdapter extends XmlAdapter<Object, Map<String, String>> {
    @Override
    public Map<String, String> unmarshal(Object domTree) {
        Map<String, String> map = new HashMap<>();
        Element content = (Element) domTree;
        NodeList childNodes = content.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            String key = child.getNodeName();
            // Skip text nodes
            if (key.startsWith("#"))
                continue;
            String value = ((Text) child.getChildNodes().item(0)).getWholeText();
            map.put(key, value);
        }
        return map;
    }

    @Override
    public Object marshal(Map<String, String> map) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element customXml = doc.createElement("Map");
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Element keyValuePair = doc.createElement(entry.getKey());
                    keyValuePair.appendChild(doc.createTextNode(entry.getValue()));
                    customXml.appendChild(keyValuePair);
                }
            }
            return customXml;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
