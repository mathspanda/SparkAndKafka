package org.mp.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.mp.es.util.ESUtils;
import org.mp.model.Region;
import scala.Tuple2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by MP on 23/3/16.
 */
public class ActionStreamProcessor implements Serializable {
    private static ActionStreamProcessor streamProcessor = null;

    private ActionStreamProcessor() {}

    public static ActionStreamProcessor getInstance() {
        if (streamProcessor == null) {
            streamProcessor = new ActionStreamProcessor();
        }
        return streamProcessor;
    }

    public void processActionStream(String topicName) {
        SparkConf conf = new SparkConf().setAppName("ActionStreamProcessor").setMaster("local[2]");
        JavaStreamingContext ctx = new JavaStreamingContext(conf, Durations.seconds(5));

        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topicName, 1);
        JavaPairReceiverInputDStream<String, String> messages =
                KafkaUtils.createStream(ctx, "localhost", "group1", topicMap);
        JavaDStream<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
            public String call(Tuple2<String, String> stringStringTuple2) throws Exception {
                return stringStringTuple2._2();
            }
        });
        JavaDStream<Region> regions = lines.map(new Function<String, Region>() {
            private final Pattern COMMA = Pattern.compile(",");

            public Region call(String s) throws Exception {
                String[] ss = COMMA.split(s);
                long timestamp = Long.parseLong(ss[0]);
                int coordinateX = Integer.parseInt(ss[1]);
                int coordinateY = Integer.parseInt(ss[2]);
                return new Region(getId(coordinateX, coordinateY),
                        coordinateX, coordinateY, timestamp);
            }

            private int getId(int coordinateX, int coordinateY) {
                if (coordinateX >= 0 && coordinateX <= 500) {
                    return coordinateY <= 500 ? 1 : 3;
                } else {
                    return coordinateY <= 500 ? 2 : 4;
                }
            }
        });
        regions.foreachRDD(new Function<JavaRDD<Region>, Void>() {
            public Void call(JavaRDD<Region> regionJavaRDD) throws Exception {
                List<Region> regions = regionJavaRDD.collect();

                ESUtils utils = new ESUtils("127.0.0.1");
                ObjectMapper objectMapper = new ObjectMapper();
                for (Region region : regions) {
                    utils.index("moves", "region", objectMapper.writeValueAsString(region));
                }
                utils.close();
                return null;
            }
        });

        ctx.start();
        ctx.awaitTermination();
    }

    public static void main(String[] args) {
        getInstance().processActionStream("move_action");
    }
}
