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

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Antonio Gomes
 */
public class ClasspathScanner {
    private static final Logger log = Logger.getLogger(ClasspathScanner.class);
    private static final String JAR_FILE_PATTERN = ".jar!";

    private ClassSelector selector;
    private Set<Class<?>> classes;

    // PUBLIC METHODS ------------------------------------------------------------------------------

    public synchronized Set<Class<?>> scanPackage(String basePackage, ClassSelector selector) throws Exception {
        if (selector == null) {
            throw new NullPointerException("Selector cannot be null");
        }
        this.selector = selector;
        this.classes = new HashSet<Class<?>>();
        Set<Class<?>> aux;
        try {
            scanClasses0(basePackage);
            aux = this.classes;
        }
        finally {
            this.selector = null;
            this.classes = null;
        }

        return aux;
    }

    // HELPER CLASSES ------------------------------------------------------------------------------

    private void scanClasses0(String basePackage)
            throws IOException, ClassNotFoundException, FileNotFoundException {
        File packageDirectory = null;
        ClassLoader cld = getLoader();
        String basePackagePath = basePackage.replace('.', '/');
        Enumeration<URL> basePackageUrls = cld.getResources(basePackagePath);
        if (basePackageUrls == null || !basePackageUrls.hasMoreElements()) {
            throw new ClassNotFoundException("Base package path not found: [" + basePackagePath
                    + "]");
        }
        while (basePackageUrls.hasMoreElements()) {
            String packagePath = basePackageUrls.nextElement().getFile();
            if (packagePath.contains(JAR_FILE_PATTERN)) {
                scanJarFile(basePackagePath, packagePath);
            } else {
                packageDirectory = new File(packagePath);
                scanDirectory(basePackage, packageDirectory);
            }
        }
    }

    private void scanDirectory(String packageName, File packagePath)
            throws ClassNotFoundException, FileNotFoundException {
        if (packagePath.exists()) {
            File[] packageFiles = packagePath.listFiles();
            for (File file : packageFiles) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String fullFileName = packageName + '.' + file.getName();
                    checkClass(fullFileName);
                } else if (file.isDirectory()) {
                    scanDirectory(packageName + "." + file.getName(), file);
                }
            }
        } else {
            throw new FileNotFoundException(packagePath.getPath());
        }
    }

    private void scanJarFile(String basePackagePath, String jarFileUrl)
            throws IOException, ClassNotFoundException {
        String jarFilePath = jarFileUrl.substring("file:".length(), jarFileUrl
                .indexOf(JAR_FILE_PATTERN)
                + JAR_FILE_PATTERN.length() - 1);
        log.debug("URL JAR file path: [" + jarFilePath + "]");
        jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
        log.debug("Decoded JAR file path: [" + jarFilePath + "]");
        JarFile jar = new JarFile(new File(jarFilePath));
        for (Enumeration<JarEntry> jarFiles = jar.entries(); jarFiles.hasMoreElements();) {
            JarEntry file = jarFiles.nextElement();
            String fileName = file.getName();
            if (!file.isDirectory() && fileName.endsWith(".class")
                    && fileName.startsWith(basePackagePath)) {
                String className = fileName.replace('/', '.');
                checkClass(className);
            }
        }
    }

    private void checkClass(String fullFilePath) throws ClassNotFoundException {
        String className = fullFilePath.substring(0, fullFilePath.length() - 6);
        Class<?> c = getLoader().loadClass(className);
        if (selector.select(c)) {
            classes.add(c);
        }
    }

    private ClassLoader getLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = getClass().getClassLoader();
        }
        return loader;
    }

    // INNER CLASSES -------------------------------------------------------------------------------

    public interface ClassSelector {
        boolean select(Class<?> clazz);
    }

    public static class AnnotatedClassSelector implements ClassSelector {
        private final Class<? extends Annotation>[] annotations;

        public AnnotatedClassSelector(Class<? extends Annotation>... annotations) {
            this.annotations = annotations;
        }

        public boolean select(Class<?> clazz) {
            for (Class<? extends Annotation> ac : annotations) {
                if (clazz.isAnnotationPresent(ac)) {
                    return true;
                }
            }
            return false;
        }
    }
}
