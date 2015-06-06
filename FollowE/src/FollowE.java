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
public class FollowE {  
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>{  
        private Text word1 = new Text();
        private IntWritable result = new IntWritable();
        public void map(LongWritable key, Text value, Context context)  
                throws IOException, InterruptedException {  
        		String[] arrays=value.toString().split(",");
        		word1.set(arrays[0]);
        		char a=(char)arrays[1].getBytes()[0];
        		int a1=a-48;
        		result.set(a1);
            context.write(word1,result);
             
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
        job.setJarByClass(FollowE.class);
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