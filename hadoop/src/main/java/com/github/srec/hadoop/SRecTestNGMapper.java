package com.github.srec.hadoop;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Mapper class which wraps the test script execution inside a TestNG test. 
 *
 * @author Victor Tatai
 */
public abstract class SRecTestNGMapper extends Mapper<Object, Text, Text, LongWritable> {
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        runApplication();
        TestNGRunner runner = new TestNGRunner();
        runner.run(value.toString());
        context.write(new Text("success"), new LongWritable(runner.getSuccessCount()));
        context.write(new Text("failure"), new LongWritable(runner.getFailureCount()));
        context.write(new Text("skipped"), new LongWritable(runner.getSkippedCount()));
        context.write(new Text("failureWithSuccessPercentageCount"), new LongWritable(runner.getFailureWithSuccessPercentageCount()));

        if (runner.getFailureCount() > 0) {
            FileSplit split = (FileSplit) context.getInputSplit();
            System.out.println("Path " + split.getPath());
            context.write(new Text(split.getPath().getName()), new LongWritable(runner.getFailureCount()));
        }
    }

    public abstract void runApplication();
}
