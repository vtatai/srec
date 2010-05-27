/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.util;

import java.net.URL;

/**
 * Interface that represents a resource, such as a file or resource in classpath.
 * <p/>
 * The only way to create valid instances is by calling any of the factory method available in the class
 * {@link ResourceFactory}. Notice that instances of this class are immutable.
 *
 * @author Antonio Gomes
 */
public interface Resource {

    /**
     * Gets the URL that represents this resource.
     *
     * @return url
     */
    URL getUrl();

    /**
     * Gets the parent of this resource or <code>null</code> if none is found.
     *
     * @return resource (or <code>null</code> if none is available)
     */
    Resource getParent();

    /**
     * Gets a resource relative to this one.
     * <p/>
     * This allows the use of symbols like <code>'.'</code>,<code>'..'</code>, and <code>'/'</code> to
     * navigate to locations related to this one.
     *
     * @param relativeName Relative resource name (no prefix <code>file:</code> or <code>classpath:</code> is
     *                     allowed)
     * @param sibling      If true, assumes the resource name is based on the parent of current resource
     * @return resource (or null no given resource is available)
     */
    Resource getRelative(String relativeName, boolean sibling);

    /**
     * Computes the SHA1 digest with the contents of this resource.
     *
     * @return digest formatted as a hex string
     */
    String computeDigest();
}