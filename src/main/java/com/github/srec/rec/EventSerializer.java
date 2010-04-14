package com.github.srec.rec;

import com.github.srec.RecorderEvent;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.github.srec.Utils.quote;
import static com.github.srec.Utils.unquote;

public class EventSerializer {
    public static void write(File file, List<RecorderEvent> events) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        try {
            for (RecorderEvent event : events) {
                writer.println(print(event));
            }
        } finally {
            try { writer.flush(); }
            finally { writer.close(); }
        }
    }

    private static String print(RecorderEvent event) {
        if (StringUtils.isBlank(event.getCommand())) return "";
        if (StringUtils.isBlank(event.getComponentLocator())) return "";
        StringBuilder strb = new StringBuilder(event.getCommand()).append(" ").append(quote(event.getComponentLocator()));
        for (String arg : event.getArgs()) {
            strb.append(", ").append(quote(arg));
        }
        return strb.toString();
    }

    public static List<RecorderEvent> read(File file) throws IOException {
        List<RecorderEvent> events = new ArrayList<RecorderEvent>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                events.add(read(line));
            }
        } finally {
            reader.close();
        }
        return events;
    }

    private static RecorderEvent read(String line) {
        line = line.trim();
        int i = line.indexOf(" ");
        if (i == -1) throw new EventReaderException("Could not read '" + line + "'");
        String event = line.substring(0, i);
        String[] originalParams = line.substring(i + 1).split(",");
        if (originalParams.length < 1) throw new EventReaderException("Could not read '" + line + "' because no target was provided");
        for (int j = 0; j < originalParams.length; j++) {
            String finalParam = originalParams[j];
            originalParams[j] = unquote(finalParam.trim());
        }
        String[] finalParams = new String[originalParams.length - 1];
        System.arraycopy(originalParams, 1, finalParams, 0, finalParams.length);
        return new RecorderEvent(event, originalParams[0], null, finalParams);
    }
}
