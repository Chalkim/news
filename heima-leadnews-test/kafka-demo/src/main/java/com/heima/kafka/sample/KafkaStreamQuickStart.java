package com.heima.kafka.sample;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 流式处理
 */
public class KafkaStreamQuickStart {

    public static void main(String[] args) {
        // 配置
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-quickstart");

        // streamsBuilder
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // 处理数据
        streamProcessor(streamsBuilder);

        // 创建KafkaStream对象
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), prop);

        // 启动
        kafkaStreams.start();
    }

    /**
     * 处理数据
     * 消息内容：hello kafka
     * @param streamsBuilder
     */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        stream.flatMapValues(
                new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String s) {
                        String[] split = s.split(" ");
                        return Arrays.asList(split);
                    }
                }).groupBy((key, value) -> value)
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10000)))
                .count()
                .toStream()
                .map((key, value) -> {
                    System.out.println("key: " + key.key() + " value: " + value);
                    return new KeyValue<>(key.key().toString(), value.toString());
                })
                .to("itcast-topic-output");
    }
}
