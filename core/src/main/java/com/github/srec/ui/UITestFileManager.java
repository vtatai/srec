package com.github.srec.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the file set.
 * 
 * @author Victor Tatai
 */
public class UITestFileManager {
    public static List<FileNode> scanFiles(File currentDirectory) {
        if (!currentDirectory.isDirectory()) return null;
        File[] files = currentDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rb");
            }
        });
        List<FileNode> ret = new ArrayList<FileNode>();
        for (File file : files) {
            ret.add(new FileNode(file));
        }
        return ret;
    }

    public static class FileNode extends DefaultMutableTreeNode {
        public FileNode(File file) {
            super(file, true);
            if (!file.getName().endsWith(".rb")) {
                throw new IllegalArgumentException("Can only be used with .rb files");
            }
        }

        public File getFile() {
            return (File) getUserObject();
        }

        @Override
        public String toString() {
            return ((File) getUserObject()).getName();
        }
    }
}
