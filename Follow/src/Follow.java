import java.io.IOException;  
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
public class Follow  {  
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{  
        private final static IntWritable one = new IntWritable(1);  
        private Text word1 = new Text();
        private Text word2 = new Text();
        public void map(LongWritable key, Text value, Context context)  
                throws IOException, InterruptedException {  
        		String[] arrays=value.toString().split(",");
        		word1.set(arrays[0]+","+arrays[1]);
        		word2.set(arrays[1]+","+arrays[0]);
        		context.write(word1,one);
        		context.write(word2,one);
        }  
    } 
    public static class Combine extends Reducer<Text, IntWritable, Text, IntWritable> {  
    	private IntWritable one1 = new IntWritable(1);  
    	private IntWritable zero1 = new IntWritable(0);
//    	private IntWritable result = new IntWritable();
    	private Text word1 = new Text();
    	public void reduce(Text key, Iterable<IntWritable> values, Context context)  
    			throws IOException, InterruptedException {  
    		int sum = 0;  
         for (IntWritable val : values) { 
                sum += val.get();  
            }
         if(sum==2)
            {
            	String[] arrays=key.toString().split(",");
            	word1.set(arrays[0]);
            	context.write(word1,one1);
            }
         else
            {
            	String[] arrays=key.toString().split(",");
            	word1.set(arrays[0]);
            	context.write(word1,zero1);
            }
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
        Job job = new Job(conf, "follow");
        job.setJarByClass(Follow .class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Combine.class);  
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);  
        job.setOutputKeyClass(Text.class);  
        job.setOutputValueClass(IntWritable.class);  
        FileInputFormat.addInputPath(job, new Path(args[0]));  
        FileOutputFormat.setOutputPath(job, new Path(args[1]));  
        System.exit(job.waitForCompletion(true) ? 0 : 1);  
    }  
} 