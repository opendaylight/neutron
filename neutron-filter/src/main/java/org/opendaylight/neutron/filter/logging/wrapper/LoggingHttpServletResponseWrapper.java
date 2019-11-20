/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.filter.logging.wrapper;

import static java.nio.charset.StandardCharsets.UTF_8;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class LoggingHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final LoggingServletOutputStream loggingServletOutputStream = new LoggingServletOutputStream();

    public LoggingHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return loggingServletOutputStream;
    }

    @SuppressFBWarnings("DM_DEFAULT_ENCODING")
    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(loggingServletOutputStream.baos);
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return loggingServletOutputStream.baos;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>(0);
        for (String headerName : getHeaderNames()) {
            headers.put(headerName, getHeader(headerName));
        }
        return headers;
    }

    public String getContent() {
        try {
            String responseEncoding = getCharacterEncoding();
            return loggingServletOutputStream.baos.toString(
                    responseEncoding != null ? responseEncoding : UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]";
        }
    }

    public char[] getContentAsChar() {
        try {
            char[] content = null;
            String responseEncoding = getCharacterEncoding();
            String response = loggingServletOutputStream.baos.toString(
                    responseEncoding != null ? responseEncoding : UTF_8.name());
            if (response != null && !response.isEmpty()) {
                content = response.toCharArray();
            }
            return content;
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]".toCharArray();
        }
    }

    private static class LoggingServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        @Override
        public void write(int bint) throws IOException {
            baos.write(bint);
        }

        @Override
        public void write(byte[] barr) throws IOException {
            baos.write(barr);
        }

        @Override
        public void write(byte[] barr, int off, int len) throws IOException {
            baos.write(barr, off, len);
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return baos;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}