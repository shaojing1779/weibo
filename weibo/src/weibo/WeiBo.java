package weibo;
import java.io.IOException;  
//import java.util.StringTokenizer;  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.IntWritable;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
public class WeiBo {  
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{  
        private final static IntWritable one = new IntWritable(1);  
        private final static IntWritable zero = new IntWritable(0);
        private Text word1 = new Text();
        private Text word2 = new Text();
        public void map(LongWritable key, Text value, Context context)  
                throws IOException, InterruptedException {  
        		String[] arrays=value.toString().split(",");
        		word1.set(arrays[0]);
        		word2.set(arrays[1]);
        		context.write(word1, one);
            context.write(word2, zero);
             
        }  
    } 
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {  
        private IntWritable result = new IntWritable();  
        public void reduce(Text key, Iterable<IntWritable> values, Context context)  
                throws IOException, InterruptedException {  
            int sum = 0;  
            for (IntWritable val : values) { 
                sum += val.get();  
            }  
            result.set(sum);  
            context.write(key, result);  
        
    }
    }
    public static void main(String[] args) throws Exception {  
        Configuration conf = new Configuration();  
        if (args.length != 2) {  
            System.err.println("Usage: arg ERROR!  ");  
            System.exit(2);  
        }
        Job job = new Job(conf, "weibo");
        job.setJarByClass(WeiBo.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);  
        job.setMapOutputKeyClass(Text.class);  
        job.setMapOutputValueClass(IntWritable.class);  
        job.setOutputKeyClass(Text.class);  
        job.setOutputValueClass(IntWritable.class);  
        FileInputFormat.addInputPath(job, new Path(args[0]));  
        FileOutputFormat.setOutputPath(job, new Path(args[1]));  
        System.exit(job.waitForCompletion(true) ? 0 : 1);  
    }  
}  