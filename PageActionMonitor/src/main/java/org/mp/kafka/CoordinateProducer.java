package org.mp.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.mp.bean.Coordinate;

import java.util.Properties;

/**
 * Created by MP on 23/3/16.
 */
public class CoordinateProducer {
    private static CoordinateProducer instance = null;

    private CoordinateProducer() {}

    public static CoordinateProducer getInstance() {
        if (instance == null) {
            instance = new CoordinateProducer();
        }
        return instance;
    }

    public void produce(Coordinate coordinate) {
        Properties props = new Properties();
        props.put("zookeeper.connect", "127.0.0.1:2181");
        props.put("metadata.broker.list", "127.0.0.1:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        String message = System.currentTimeMillis() + "," + coordinate.getX() + "," + coordinate.getY();
        KeyedMessage<String, String> data = new KeyedMessage<String, String>("move_action", message);
        producer.send(data);
        producer.close();
    }
}
