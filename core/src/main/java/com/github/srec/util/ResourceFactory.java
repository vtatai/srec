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

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service responsible for creating resources.
 */
public abstract class ResourceFactory {

    private static final Logger log = Logger.getLogger(ResourceFactory.class);

    private ResourceFactory() {
    }

    public static Resource getResource(URL url) {
        ResourceImpl r = new ResourceImpl(url);
        return r.exists() ? r : null;
    }

    public static Resource getResource(File file) {
        return getResource("file:" + file.getAbsolutePath());
    }

    /**
     * Retrieves a resource handle by locator and name.
     * <p/>
     * Resources can be either files or directories. They must be specified by an expression formed of two
     * parts, the type of locator, and resource name. Resource names are always specified with forward
     * slashes. Usage:
     * <ul>
     * <li><code>classpath:com/company/Resource</code>: Creates a resource from the classpath. Do not start
     * the resource name with a slash.
     * a last
     * <li><code>file:path/to/file</code>: Creates a resource from the filesystem. The path can be either
     * relative or absolute.
     * </ul>
     *
     * @param resourceSpec Resource name including the locator specification (file: or classpath:)
     * @return resource or null if the resource could not be located
     */
    public static Resource getResource(String resourceSpec) {
        log.trace("retrieving resource -> " + resourceSpec);
        try {

            if (resourceSpec.startsWith("file:")) {
                return new ResourceImpl(normFile(resourceSpec.substring(5)).toURI().toURL());
            }

            if (resourceSpec.startsWith("classpath:")) {
                String name = resourceSpec.substring(10);
                if (name.startsWith("/")) {
                    throw new IllegalArgumentException("Classpath resource cannot start with '/': " + name);
                }
                if (name.indexOf('\\') != -1) {
                    throw new IllegalArgumentException("Classpath resource cannot contain '\\': " + name);
                }
                URL url = getLoader().getResource(name);
                if (url == null) {
                    return null;
                }
                return new ResourceImpl(url);
            }
            log.error("Invalid resource specification: " + resourceSpec);
            return null;
        } catch (Exception e) {
            log.error("Could not retrieve resource: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets a generic resource, searching first from the file system then in the classpath if not found.
     *
     * @param resource The resource name
     * @return The resource
     */
    public static Resource getGenericResource(String resource) {
        log.trace("retrieving resource -> " + resource);
        try {

            final File file = normFile(resource);
            if (file.exists()) {
                return new ResourceImpl(file.toURI().toURL());
            }

            if (resource.startsWith("/")) {
                throw new IllegalArgumentException("Classpath resource cannot start with '/': " + resource);
            }
            if (resource.indexOf('\\') != -1) {
                throw new IllegalArgumentException("Classpath resource cannot contain '\\': " + resource);
            }
            URL url = getLoader().getResource(resource);
            if (url == null) {
                return null;
            }
            return new ResourceImpl(url);
        } catch (Exception e) {
            log.error("Could not retrieve resource: " + e.getMessage(), e);
            return null;
        }
    }

    // HELPER METHODS ----------------------------------------------------------------------------------------

    private static ClassLoader getLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static File normFile(String s) {
        return new File(s.replace('/', File.separatorChar).replace('\\', File.separatorChar)).getAbsoluteFile();
    }

    public static String digestResource(URL url) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        InputStream is = url.openStream();
        byte[] buffer = new byte[8192];
        int read = 0;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] shaSum = digest.digest();
            return new BigInteger(1, shaSum).toString(16);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
            }
        }
    }

    // INNER CLASSES -----------------------------------------------------------------------------------------

    private static class ResourceImpl implements Resource {

        private final URL url;

        protected ResourceImpl(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }

        private boolean exists() {
            InputStream is = null;
            try {
                is = url.openStream();
                return true;
            }
            catch (IOException ioe) {
                return false;
            }
            finally {
                IOUtils.closeQuietly(is);
            }
        }

        @Override
        public Resource getParent() {
            String u = url.toString();
            int p = u.lastIndexOf('/');
            if (p == -1) {
                throw new IllegalArgumentException("Classpath resource has no parent: " + u);
            }
            u = u.substring(0, p);
            try {
                ResourceImpl resource = new ResourceImpl(new URL(u));
                return resource.exists() ? resource : null;
            }
            catch (MalformedURLException e) {
                throw new IllegalArgumentException("Cannot retrieve parent resource of " + url);
            }
        }

        @Override
        public Resource getRelative(String relativeName, boolean sibling) {
            try {
                ResourceImpl r = sibling ? (ResourceImpl) getParent() : this;
                for (String term : relativeName.split("/")) {
                    if (term.equals(".")) {
                        continue;
                    }
                    if (term.equals("..")) {
                        r = (ResourceImpl) r.getParent();
                    } else {
                        r = new ResourceImpl(new URL(r.getUrl() + "/" + term));
                    }
                }
                return r.exists() ? r : null;
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Cannot retrieve parent resource of " + url);
            }
        }

        @Override
        public String computeDigest() {
            try {
                return digestResource(url);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not compute digest of resource: " + url, e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ResourceImpl)) {
                return false;
            }
            ResourceImpl resource = (ResourceImpl) o;
            return !(url != null ? !url.equals(resource.url) : resource.url != null);
        }

        @Override
        public int hashCode() {
            return url != null ? url.hashCode() : 0;
        }

        @Override
        public String toString() {
            return url.toString();
        }
    }
}