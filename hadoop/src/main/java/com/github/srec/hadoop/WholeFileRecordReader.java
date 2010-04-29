package com.github.srec.hadoop;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class WholeFileRecordReader extends RecordReader<Object, Text> {
    private FileSplit fileSplit;
    private TaskAttemptContext taskAttemptContext;
    private boolean processed = false;
    private Text value;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        this.fileSplit = (FileSplit) inputSplit;
        this.taskAttemptContext = taskAttemptContext;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (processed) return false;
        byte[] contents = new byte[(int) fileSplit.getLength()];
        Path file = fileSplit.getPath();
        FileSystem fs = file.getFileSystem(taskAttemptContext.getConfiguration());
        FSDataInputStream in = null;
        try {
            in = fs.open(file);
            IOUtils.readFully(in, contents, 0, contents.length);
            value = new Text(contents);
        } finally {
            IOUtils.closeStream(in);
        }
        processed = true;
        return true;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public float getProgress() throws IOException {
        return processed ? fileSplit.getLength() : 0;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
