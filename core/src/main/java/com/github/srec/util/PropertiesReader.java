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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Reads global application settings from srec.properties files.
 * 
 * @author Victor Tatai
 */
public class PropertiesReader {
    public static final String PACKAGES_TO_SCAN_PROPERTY_NAME = "packages_to_scan";
    public static final String PLAYER_COMMAND_INTERVAL= "player_command_interval";

    private static Properties properties;

    public static Properties getProperties() throws IOException {
        if (properties != null) return properties;
        properties = new Properties();
        Enumeration<URL> urls = ClassLoader.getSystemResources("srec.properties");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            Properties prop = new Properties();
            prop.load(url.openStream());
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                if (properties.containsKey(key)) {
                    properties.put(key, properties.get(key) + "," + value);
                } else {
                    properties.put(key, value);
                }
            }
        }
        return properties;
    }
}
