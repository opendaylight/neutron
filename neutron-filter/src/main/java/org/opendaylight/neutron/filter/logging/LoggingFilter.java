/*
 * Copyright (c) 2020 Ericsson India Global Services Pvt Ltd. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.neutron.filter.logging;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opendaylight.neutron.filter.logging.entity.LoggingRequest;
import org.opendaylight.neutron.filter.logging.entity.LoggingResponse;
import org.opendaylight.neutron.filter.logging.wrapper.LoggingHttpServletRequestWrapper;
import org.opendaylight.neutron.filter.logging.wrapper.LoggingHttpServletResponseWrapper;
import org.slf4j.Logger;

public class LoggingFilter implements Filter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Logger log = getLogger(getClass());
    private static final String REQUEST_PREFIX = "REQUEST:";
    private static final String RESPONSE_PREFIX = "RESPONSE:";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_DELETE = "DELETE";

    static {
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("LoggingFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!log.isDebugEnabled()) {
            // If debug logging is not enabled, neutron logger filter needs to be bypassed
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        if (!isMethodCUD(httpRequest)) {
            // if the request is not POST/PUT/DELETE, neutron logger filter needs to be bypassed
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            // if request has parameters, then neutron logger filter needs to be bypassed
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }
        // We come here only for POST, PUT and DELETE invocations for non-parametrized requests
        LoggingHttpServletRequestWrapper requestWrapper = new LoggingHttpServletRequestWrapper(httpRequest);
        LoggingHttpServletResponseWrapper responseWrapper = new LoggingHttpServletResponseWrapper(httpResponse);
        log.debug("request prefix {}  and request description {}", REQUEST_PREFIX,
                getRequestDescription(requestWrapper));
        filterChain.doFilter(requestWrapper, responseWrapper);
        log.debug("response prefix {} and response description{}", RESPONSE_PREFIX,
                getResponseDescription(responseWrapper));
        responseWrapper.getByteArrayOutputStream().writeTo(httpResponse.getOutputStream());
    }

    @Override
    public void destroy() {
    }

    public boolean isMethodCUD(HttpServletRequest request) {
        String contentType = request.getContentType();
        return (contentType != null && ((METHOD_POST.equalsIgnoreCase(request.getMethod()))
                || (METHOD_PUT.equalsIgnoreCase(request.getMethod()))
                || (METHOD_DELETE.equalsIgnoreCase(request.getMethod()))));
    }

    protected String getRequestDescription(LoggingHttpServletRequestWrapper requestWrapper) throws IOException {
        String request = "";
        LoggingRequest loggingRequest = new LoggingRequest();
        loggingRequest.setMethod(requestWrapper.getMethod());
        loggingRequest.setPath(requestWrapper.getRequestURI());
        loggingRequest.setContentLength(requestWrapper.getContentLength());
        loggingRequest.setBody(requestWrapper.getContent());

        try {
            request = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(loggingRequest);
            return request;
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Request to JSON", e);
            return null;
        }
    }

    protected String getResponseDescription(LoggingHttpServletResponseWrapper responseWrapper) {
        String response = "";
        LoggingResponse loggingResponse = new LoggingResponse();
        loggingResponse.setStatus(responseWrapper.getStatus());
        char[] content = responseWrapper.getContentAsChar();
        // TODO(Dimple): This area is expensive in string management, need to rework
        if (content != null && content.length > 0) {
            if (responseWrapper.getStatus() < 200 || responseWrapper.getStatus() > 299) {
                loggingResponse.setBody(content);
            }
        }
        try {
            response = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(loggingResponse);
            return response;
        } catch (JsonProcessingException e) {
            log.warn("Cannot serialize Response to JSON", e);
            return null;
        }
    }
}