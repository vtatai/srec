package com.github.srec.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Victor Tatai
 */
class ThreadedReader extends Thread {
    private BufferedReader reader;
    private BlockingQueue<String> lines = new LinkedBlockingQueue<String>();

    ThreadedReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        String line;
        try {
            while((line = reader.readLine()) != null) {
                System.out.println("Reader: " + line);
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BlockingQueue<String> getLines() {
        return lines;
    }

    public String take() throws InterruptedException {
        return lines.take();
    }

    public void drainTo(Collection<String> to) {
        lines.drainTo(to);
    }
}
