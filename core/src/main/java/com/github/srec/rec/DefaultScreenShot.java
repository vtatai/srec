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

package com.github.srec.rec;

import com.github.srec.util.AWTTreeScanner;
import com.github.srec.util.PropertiesReader;
import com.github.srec.util.ScannerMatcher;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Takes a screenshot
 *
 * @author vivek
 * @author Victor Tatai
 */
public class DefaultScreenShot implements ScreenShot {
    private static final Logger log = Logger.getLogger(DefaultScreenShot.class);
    private static int counter = 1;

    public String captureDesktop(String parent, Robot robot) {
        log.info("Capturing desktop screenshot");
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return capture(parent, screenRect, robot);
    }

    public String captureFrame(String parent, Robot robot) {
        log.info("Capturing frame screenshot");
        Rectangle screenRect = findActiveWindow().getBounds();
        if (screenRect == null) return null;
        return capture(parent, screenRect, robot);
    }

    public String captureInternalFrame(String parent, Robot robot) {
        log.info("Capturing internal frame screenshot (under mouse pointer)");
        Window w = findActiveWindow();
        if (!(w instanceof JFrame)) return null;
        JInternalFrame iframe = findInternalFrame((JFrame) w);
        if (iframe == null) return null;
        return capture(parent, getScreenSize(iframe), robot);
    }

    private JInternalFrame findInternalFrame(JFrame frame) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Component component = frame.findComponentAt(mouseLocation);
        return findInternalFrameParent(component);
    }

    private JInternalFrame findInternalFrameParent(Component component) {
        while (component != null && !(component instanceof JInternalFrame)) {
            component = component.getParent();
        }
        return component != null ? (JInternalFrame) component : null;
    }

    private Window findActiveWindow() {
        Window[] ws = Window.getOwnerlessWindows();
        if (ws.length == 0) return null;
        for (Window w : ws) {
            if (w.isActive()) {
                return w;
            }
        }
        return null;
    }

    private Rectangle getScreenSize(JInternalFrame iframe) {
        Point p = iframe.getLocationOnScreen();
        Rectangle r = iframe.getBounds();
        r.setLocation(p);
        return r;
    }

    @Override
    public String captureInternalFrame(String title, String parentFolder, Robot robot) {
        log.info("Capturing internal frame screenshot with title " + title);
        Window w = findActiveWindow();
        if (!(w instanceof JFrame)) return null;
        JInternalFrame iframe = findInternalFrame(title, (JFrame) w);
        if (iframe == null) {
            log.warn("Could not locate internal frame"); 
            return null;
        }
        return capture(parentFolder, getScreenSize(iframe), robot);
    }

    private JInternalFrame findInternalFrame(final String title, JFrame frame) {
        if (title == null) return null;
        Component c = AWTTreeScanner.scan(frame, new ScannerMatcher() {
            @Override
            public boolean matches(Component component) {
                return component != null && component instanceof JInternalFrame
                        && title.equals(((JInternalFrame) component).getTitle());
            }
        });
        if (c != null && c instanceof JInternalFrame) return (JInternalFrame) c;
        return null;
    }

    public String capture(String parent, Rectangle screenRect, Robot robot) {
        BufferedImage image = robot.createScreenCapture(screenRect);
        String captureFileName = "screenshot-" + counter++ + ".png";
        String pathname = PropertiesReader.getProperties().getProperty(PropertiesReader.SCREENSHOTS_DIR);
        if (isBlank(pathname)) pathname = "screenshots";
        if (!isBlank(parent)) pathname += File.separator + parent;
        createScreenshotDirectory(pathname);
        String finalFileName = pathname + File.separator + captureFileName;
        try {
            ImageIO.write(image, "png", new File(finalFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalFileName;
    }

    private void createScreenshotDirectory(String pathname) {
        File screenshotDirectory = new File(pathname);
        if (!screenshotDirectory.exists()) screenshotDirectory.mkdir();
    }
}
