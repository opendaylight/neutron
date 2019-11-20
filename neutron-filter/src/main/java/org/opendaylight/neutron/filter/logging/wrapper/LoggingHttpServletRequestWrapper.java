/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.filter.logging.wrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingHttpServletRequestWrapper.class);
    private byte[] requestBody;

    public LoggingHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            requestBody = IOUtils.toByteArray(request.getInputStream());
        } catch (IOException e) {
            LOG.error("Unable to audit Neutron request, error when decoding body of request, method {} URI {}",
                    request.getMethod(), request.getRequestURI());
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new LoggingServletInputStream(requestBody);
    }

    public String getContent() {
        try {
            String requestEncoding = getCharacterEncoding();
            String normalizedContent = new String(requestBody,
                    requestEncoding != null ? requestEncoding : StandardCharsets.UTF_8.name());
            return normalizedContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class LoggingServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream bais;

        LoggingServletInputStream(byte[] content) {
            this.bais = new ByteArrayInputStream(content);
        }

        @Override
        public int read() throws IOException {
            return this.bais.read();
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.bais.close();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}