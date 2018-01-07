/*
 * Copyright (c) 2017 Mellanox.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.spi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class NeutronDictJsonAdapter extends XmlAdapter<String, Map<String, JsonElement>> {

    @Override
    public String marshal(Map<String, JsonElement> val) {
        if (null == val) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : val.entrySet()) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }
        return jsonObject.toString();
    }

    @Override
    public Map<String, JsonElement> unmarshal(String val) {
        if (null == val) {
            return null;
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(val, JsonObject.class);
        Map<String, JsonElement> map = new HashMap();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
