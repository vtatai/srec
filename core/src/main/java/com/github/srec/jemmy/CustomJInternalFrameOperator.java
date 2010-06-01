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

package com.github.srec.jemmy;

import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JInternalFrameOperator;

import javax.swing.*;
import java.awt.*;

/**
 * @author Victor Tatai
 */
public class CustomJInternalFrameOperator extends JInternalFrameOperator {
    public CustomJInternalFrameOperator(JInternalFrame b) {
        super(b);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont, ComponentChooser chooser, int index) {
        super(cont, chooser, index);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont, ComponentChooser chooser) {
        super(cont, chooser);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont, String text, int index) {
        super(cont, text, index);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont, String text) {
        super(cont, text);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont, int index) {
        super(cont, index);
    }

    public CustomJInternalFrameOperator(ContainerOperator cont) {
        super(cont);
    }

    @Override
    protected void initOperators() {
        super.initOperators();
        Container titlePane = findTitlePane();
        if (!isIcon() && titlePane != null) {
            if (titleOperator == null) {
                titleOperator = new ContainerOperator(titlePane);
            }
            if (findDesktopPane() != null) {
                if (((JInternalFrame) getSource()).isIconifiable()) {
                    minOper = new JButtonOperator(titleOperator, 1);
                } else {
                    minOper = null;
                }
                if (((JInternalFrame) getSource()).isMaximizable()) {
                    maxOper = new JButtonOperator(titleOperator, 0);
                } else {
                    maxOper = null;
                }
            } else {
                minOper = null;
                maxOper = null;
            }
            if (isClosable()) {
                closeOper = new JButtonOperator(titleOperator, 2);
            } else {
                closeOper = null;
            }
        } else {
            titleOperator = null;
            minOper = null;
            maxOper = null;
            closeOper = null;
        }
    }

    private Container findDesktopPane() {
        return getContainer(new ComponentChooser() {
            public boolean checkComponent(Component comp) {
                return (comp instanceof JDesktopPane);
            }

            public String getDescription() {
                return ("Desctop pane");
            }
        });
    }
}
