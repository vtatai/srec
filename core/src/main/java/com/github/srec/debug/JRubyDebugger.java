package com.github.srec.debug;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Tatai
 */
public class JRubyDebugger {
    private ThreadedWriter threadedOut;
    private ThreadedReader threadedReader;
    private ThreadedReader threadedErr;
    private BufferedReader in;
    private volatile boolean running;

    public void start(String script) throws IOException, InterruptedException {
        running = true;
        final Process p = new ProcessBuilder("jruby", "--debug", "-S", "rdebug", script).start();
        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(p.getOutputStream()), true);
        final BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        ignoreLines(2);

        threadedReader = new ThreadedReader(in);
        threadedErr = new ThreadedReader(err);
        threadedOut = new ThreadedWriter(out);
        threadedReader.start();
        threadedErr.start();
        threadedOut.start();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    p.waitFor();
                    threadedReader.interrupt();
                    threadedOut.interrupt();
                    threadedErr.interrupt();
                    in.close();
                    out.close();
                    err.close();
                    running = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        t.start();
    }

    private void ignoreLines(int lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            in.readLine();            
        }
    }

    public void cont() {
        threadedOut.put("cont");
    }

    public void step() {
        threadedOut.put("step");
    }

    public void quit() {
        threadedOut.put("quit");
    }

    public void breakpoint(String file, int line) {
        threadedOut.put("break " + file + ":" + line);
    }

    public List<String> getOutputLines() {
        List<String> ret = new ArrayList<String>();
        threadedReader.drainTo(ret);
        return ret;
    }

    public String read() throws InterruptedException {
        return threadedReader.take();
    }

    public void stop() {
        threadedOut.interrupt();
        threadedReader.interrupt();
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JRubyDebugger d = new JRubyDebugger();
        d.start("test.rb");
        d.cont();
    }
}
