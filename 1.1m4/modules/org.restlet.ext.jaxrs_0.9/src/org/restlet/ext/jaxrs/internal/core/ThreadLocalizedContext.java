/*
 * Copyright 2005-2008 Noelios Consulting.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */
package org.restlet.ext.jaxrs.internal.core;

import java.net.URI;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyWorkers;

/**
 * This class is used for thread local injection into providers and resources.
 * 
 * @author Stephan Koops
 * @see UriInfo
 * @see Request
 * @see HttpHeaders
 * @see SecurityContext
 * @see MessageBodyWorkers
 * @see ContextResolver
 * @see CallContext
 */
public class ThreadLocalizedContext implements UriInfo, Request, HttpHeaders,
        SecurityContext {

    /**
     * The key of the {@link CallContext} in the
     * {@link org.restlet.data.Request} attributes.
     */
    private static final String CALLCONTEXT_KEY = "org.restlet.ext.jaxrs.CallContext";

    /**
     * @param lastModified
     * @return
     * @see CallContext#evaluatePreconditions(java.util.Date)
     * @see Request#evaluatePreconditions(Date)
     */
    public ResponseBuilder evaluatePreconditions(Date lastModified) {
        return get().evaluatePreconditions(lastModified);
    }

    /**
     * @param lastModified
     * @param entityTag
     * @return
     * @see CallContext#evaluatePreconditions(java.util.Date,
     *      javax.ws.rs.core.EntityTag)
     * @see Request#evaluatePreconditions(Date, EntityTag)
     */
    public ResponseBuilder evaluatePreconditions(Date lastModified,
            EntityTag entityTag) {
        return get().evaluatePreconditions(lastModified, entityTag);
    }

    /**
     * @param entityTag
     * @return
     * @see CallContext#evaluatePreconditions(javax.ws.rs.core.EntityTag)
     * @see Request#evaluatePreconditions(EntityTag)
     */
    public ResponseBuilder evaluatePreconditions(EntityTag entityTag) {
        return get().evaluatePreconditions(entityTag);
    }

    /**
     * Returns the wrapped CallContext for the current Thread.
     * 
     * @return the wrapped CallContext for the current Thread. Never returns
     *         null.
     * @throws IllegalStateException
     *                 if no {@link CallContext} was given for the current
     *                 thread. If this occurs, their is a bug in this JAX-RS
     *                 implementation.
     * @see #set(CallContext)
     */
    public CallContext get() throws IllegalStateException {
        Object callContext = getRequestAttributes().get(CALLCONTEXT_KEY);
        if (callContext == null)
            throw new IllegalStateException("No CallContext given until now");
        return (CallContext) callContext;
    }

    /**
     * @return
     * @see JaxRsUriInfo#getAbsolutePath()
     * @see UriInfo#getAbsolutePath()
     */
    public URI getAbsolutePath() {
        return get().getAbsolutePath();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getAbsolutePathBuilder()
     * @see UriInfo#getAbsolutePathBuilder()
     */
    public UriBuilder getAbsolutePathBuilder() {
        return get().getAbsolutePathBuilder();
    }

    /**
     * @see HttpHeaders#getAcceptableLanguages()
     */
    public List<String> getAcceptableLanguages() {
        return get().getAcceptableLanguages();
    }

    /**
     * @return
     * @see CallContext#getAcceptableMediaTypes()
     * @see HttpHeaders#getAcceptableMediaTypes()
     */
    @SuppressWarnings("deprecation")
    public List<MediaType> getAcceptableMediaTypes() {
        return get().getAcceptableMediaTypes();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getAncestorResources()
     * @see UriInfo#getAncestorResources()
     */
    public List<Object> getAncestorResources() {
        return get().getAncestorResources();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getAncestorResourceURIs()
     * @see UriInfo#getAncestorResourceURIs()
     */
    public List<String> getAncestorResourceURIs() {
        return get().getAncestorResourceURIs();
    }

    /**
     * @param decode
     * @return
     * @see JaxRsUriInfo#getAncestorResourceURIs(boolean)
     * @see UriInfo#getAncestorResourceURIs(boolean)
     */
    public List<String> getAncestorResourceURIs(boolean decode) {
        return get().getAncestorResourceURIs(decode);
    }

    /**
     * @return
     * @see CallContext#getAuthenticationScheme()
     * @see SecurityContext#getAuthenticationScheme()
     */
    public String getAuthenticationScheme() {
        return get().getAuthenticationScheme();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getBaseUri()
     * @see UriInfo#getBaseUri()
     */
    public URI getBaseUri() {
        return get().getBaseUri();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getBaseUriBuilder()
     * @see UriInfo#getBaseUriBuilder()
     */
    public UriBuilder getBaseUriBuilder() {
        return get().getBaseUriBuilder();
    }

    /**
     * @return
     * @see CallContext#getCookies()
     * @see HttpHeaders#getCookies()
     */
    public Map<String, Cookie> getCookies() {
        return get().getCookies();
    }

    /**
     * @return
     * @see CallContext#getLanguage()
     * @see HttpHeaders#getLanguage()
     */
    public String getLanguage() {
        return get().getLanguage();
    }

    /**
     * @return
     * @see CallContext#getMediaType()
     * @see HttpHeaders#getMediaType()
     */
    public MediaType getMediaType() {
        return get().getMediaType();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getPath()
     * @see UriInfo#getPath()
     */
    public String getPath() {
        return get().getPath();
    }

    /**
     * @param decode
     * @return
     * @see JaxRsUriInfo#getPath(boolean)
     * @see UriInfo#getPath(boolean)
     */
    public String getPath(boolean decode) {
        return get().getPath(decode);
    }

    /**
     * @return
     * @see JaxRsUriInfo#getPathExtension()
     * @see UriInfo#getPathExtension()
     */
    public String getPathExtension() {
        return get().getPathExtension();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getPathParameters()
     * @see UriInfo#getPathParameters()
     */
    public MultivaluedMap<String, String> getPathParameters() {
        return get().getPathParameters();
    }

    /**
     * @param decode
     * @return
     * @see JaxRsUriInfo#getPathParameters(boolean)
     * @see UriInfo#getPathParameters(boolean)
     */
    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        return get().getPathParameters(decode);
    }

    /**
     * @return
     * @see JaxRsUriInfo#getPathSegments()
     * @see UriInfo#getPathSegments()
     */
    public List<PathSegment> getPathSegments() {
        return get().getPathSegments();
    }

    /**
     * @param decode
     * @return
     * @see JaxRsUriInfo#getPathSegments(boolean)
     * @see UriInfo#getPathSegments(boolean)
     */
    public List<PathSegment> getPathSegments(boolean decode) {
        return get().getPathSegments(decode);
    }

    /**
     * @return
     * @see JaxRsUriInfo#getPlatonicRequestUriBuilder()
     * @see UriInfo#getPlatonicRequestUriBuilder()
     */
    public UriBuilder getPlatonicRequestUriBuilder() {
        return get().getPlatonicRequestUriBuilder();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getQueryParameters()
     * @see UriInfo#getQueryParameters()
     */
    public MultivaluedMap<String, String> getQueryParameters() {
        return get().getQueryParameters();
    }

    /**
     * @param decode
     * @return
     * @see JaxRsUriInfo#getQueryParameters(boolean)
     * @see UriInfo#getQueryParameters(boolean)
     */
    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        return get().getQueryParameters(decode);
    }

    /**
     * Returns the attributes of the current Restlet
     * {@link org.restlet.data.Request}.
     * 
     * @return the attributes of the current Restlet Request, but never null
     */
    private Map<String, Object> getRequestAttributes() {
        return org.restlet.data.Request.getCurrent().getAttributes();
    }

    /**
     * @param name
     * @return
     * @see CallContext#getRequestHeader(java.lang.String)
     * @see HttpHeaders#getRequestHeader(String)
     */
    public List<String> getRequestHeader(String name) {
        return get().getRequestHeader(name);
    }

    /**
     * @return
     * @see CallContext#getRequestHeaders()
     * @see HttpHeaders#getRequestHeaders()
     */
    public MultivaluedMap<String, String> getRequestHeaders() {
        return get().getRequestHeaders();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getRequestUri()
     * @see UriInfo#getRequestUri()
     */
    public URI getRequestUri() {
        return get().getRequestUri();
    }

    /**
     * @return
     * @see JaxRsUriInfo#getRequestUriBuilder()
     * @see UriInfo#getRequestUriBuilder()
     */
    public UriBuilder getRequestUriBuilder() {
        return get().getRequestUriBuilder();
    }

    /**
     * @return
     * @see CallContext#getUserPrincipal()
     * @see SecurityContext#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        return get().getUserPrincipal();
    }

    /**
     * @return
     * @see CallContext#isSecure()
     * @see SecurityContext#isSecure()
     */
    public boolean isSecure() {
        return get().isSecure();
    }

    /**
     * @param role
     * @return
     * @see CallContext#isUserInRole(java.lang.String)
     * @see SecurityContext#isUserInRole(String)
     */
    public boolean isUserInRole(String role) {
        return get().isUserInRole(role);
    }

    /**
     * @param variants
     * @return
     * @throws IllegalArgumentException
     * @see CallContext#selectVariant(java.util.List)
     * @see Request#selectVariant(List)
     */
    public Variant selectVariant(List<Variant> variants)
            throws IllegalArgumentException {
        return get().selectVariant(variants);
    }

    /**
     * Sets the CallContext for the current thread. You MUST set a CallContext
     * here before you can get it by {@link #get()}.
     * 
     * @param callContext
     *                The CallContext for the current request. must not be null.
     * @see #get()
     * @throws IllegalArgumentException
     *                 if null was given.
     */
    public void set(CallContext callContext) throws IllegalArgumentException {
        if (callContext == null)
            throw new IllegalArgumentException(
                    "You must give a CallContext here. null is not allowed");
        getRequestAttributes().put(CALLCONTEXT_KEY, callContext);
    }
}