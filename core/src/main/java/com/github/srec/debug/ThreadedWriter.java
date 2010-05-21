package com.github.srec.debug;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Victor Tatai
 */
class ThreadedWriter extends Thread {
    private PrintWriter writer;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    ThreadedWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = queue.take();
                writer.println(line);
                if ("quit".equals(line) || "cont".equals(line)) {
                    return;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String line) {
        try {
            queue.put(line);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
