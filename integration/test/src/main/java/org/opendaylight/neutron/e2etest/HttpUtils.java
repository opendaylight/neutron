/*
 * Copyright (C) 2018 IBM, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.e2etest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;

public final class HttpUtils {

    private HttpUtils() { }

    static HttpURLConnection httpURLConnectionFactoryGet(URL url) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryDelete(URL url) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("DELETE");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryPost(URL url, String content) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
        out.write(content);
        out.close();
        return httpConn;
    }

    static HttpURLConnection httpURLConnectionFactoryPut(URL url, String content) throws IOException {
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("PUT");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
        httpConn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
        out.write(content);
        out.close();
        return httpConn;
    }

    static void test_create(String urlStr, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPost(url, content);
            Assert.assertEquals(context, 201, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_create(String urlStr, int responseCode, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPost(url, content);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_modify(String urlStr, String content, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryPut(url, content);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_fetch(String urlStr, int responseCode, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryGet(url);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_fetch(String urlStr, String context) {
        test_fetch(urlStr, 200, context);
    }

    static void test_fetch(String urlStr, boolean positiveTest, String context) {
        int responseCode = positiveTest ? 200 : 404;
        test_fetch(urlStr, responseCode, context);
    }

    static void test_delete(String urlStr, int responseCode, String context) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = httpURLConnectionFactoryDelete(url);
            Assert.assertEquals(context, responseCode, httpConn.getResponseCode());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void test_delete(String urlStr, String context) {
        test_delete(urlStr, 204, context);
    }

    static void test_delete_404(String urlStr, String context) {
        test_delete(urlStr, 404, context);
    }

    static JsonObject test_fetch_gson(String urlStr, String context) {
        String response = fetchResponse(urlStr, context);
        Gson gson = new Gson();
        return gson.fromJson(response, JsonObject.class);
    }

    static void test_fetch_collection_response(String urlStr, String collectionName, String context) {
        String response = fetchResponse(urlStr, context);

        //Collection is returned in an array. Format - {"collectionName": [{...}, {....}]}
        Gson gson = new Gson();
        JsonObject jsonObjectOutput = gson.fromJson(response, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectOutput.entrySet();
        Assert.assertTrue("E2E Tests Failed - Json Error", entrySet.size() > 0);
        JsonElement jsonElementValue = entrySet.iterator().next().getValue();
        String key = entrySet.iterator().next().getKey();
        Assert.assertEquals(context, collectionName, key);
        Assert.assertTrue("E2E Tests Failed - Collection not Array: " + jsonElementValue + "; URL: " + urlStr
                + "; full response: " + response, jsonElementValue.isJsonArray());
        JsonArray jsonArray = jsonElementValue.getAsJsonArray();
        Assert.assertNotEquals(context, jsonArray.size(), 0);
    }

    // Helper function - content is json used during create. Format - {"Name": {...}}
    static void test_fetch_with_one_query_item(String urlStr, String content, String collectionName) {
        Gson gson = new Gson();
        JsonObject jsonObjectInput = gson.fromJson(content, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObjectInput.entrySet();
        JsonObject jsonObjectOutput = entrySet.iterator().next().getValue().getAsJsonObject();
        for (Map.Entry<String, JsonElement> element : jsonObjectOutput.entrySet()) {
            String key = element.getKey();
            JsonElement jsonElementValue = element.getValue();
            // Query only values that are non null Primitives - Integer,Strings,character and boolean
            if (jsonElementValue.isJsonPrimitive() && !jsonElementValue.isJsonNull()) {
                String valueStr = jsonElementValue.getAsString();
                valueStr = valueStr.replaceAll("\\s+", "+");
                String queryUrl = urlStr + "?" + key + "=" + valueStr;
                String context = collectionName + " " + key + "=" + jsonElementValue.toString() + " Get Failed";
                test_fetch_collection_response(queryUrl, collectionName, context);
            }
        }
    }

    private static String fetchResponse(String urlStr, String context) {
        StringBuffer response = new StringBuffer();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = HttpUtils.httpURLConnectionFactoryGet(url);
            Assert.assertEquals(context, 200, httpConn.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return response.toString();
    }

}
