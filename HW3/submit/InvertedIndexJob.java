import java.io.IOException;
import java.util.*;
import org.apache.hadoop.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexJob {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, Text>{

    private Text word = new Text();
    private Text document = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String line = value.toString();
      String[] lineArr = line.split("\t");

      StringTokenizer itr = new StringTokenizer(lineArr[1]);
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        document.set(lineArr[0]);
        context.write(word, document);
      }
    }
  }

  public static class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {
    private Text invertedIndex = new Text();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      Map<String, Integer> invertedIndexMap = new HashMap<String, Integer>();

      for (Text val: values) {
        String documentID = val.toString();

        // if (invertedIndexMap == null) {
        //   invertedIndexMap.put(documentID, 1);
        //   continue;
        // }
        if (invertedIndexMap.containsKey(documentID)) {
          invertedIndexMap.put(documentID, invertedIndexMap.get(documentID) + 1);
        } else {
          invertedIndexMap.put(documentID, 1);
        }
      }

      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, Integer> entry: invertedIndexMap.entrySet()) {
        sb.append(entry.getKey());
        sb.append(":");
        sb.append(entry.getValue());
        sb.append("\t");
      }
      String str = sb.substring(0, sb.length() - 1);
      invertedIndex.set(str);

      context.write(key, invertedIndex);
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: Inverted Index <input path> <output path>");
      System.exit(-1);
    }

    Job job = new Job();
    job.setJarByClass(InvertedIndexJob.class);
    job.setJobName("Inverted Index");

    job.setMapperClass(TokenizerMapper.class);
    job.setReducerClass(InvertedIndexReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}