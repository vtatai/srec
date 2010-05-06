package com.github.srec.ui;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitarian class used to read / write to a recent files list.
 *
 * @author Victor Tatai
 */
public class RecentFilesSerializer {
    private static final Logger logger = Logger.getLogger(RecentFilesSerializer.class);

    public static List<String> read() throws IOException {
        File file = getRecentFilesFile();
        List<String> list = new ArrayList<String>();
        if (file == null || !file.exists()) return list;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug("Adding file to recent file list: " + line);
                list.add(line);
            }
        } finally {
            if (reader != null) reader.close();            
        }
        return list;
    }

    private static File getRecentFilesFile() throws IOException {
        String path = System.getProperty("user.home") + File.separator + ".srec_history";
        return new File(path);
    }

    public static void save(List<String> files) throws IOException {
        File file = getRecentFilesFile();
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream(file));
            for (String fileName : files) {
                out.println(fileName);
            }
        } finally {
            if (out != null) out.close();
        }
    }
}
