package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // kafka配置信息
        Properties prop = new Properties();
        // 连接地址
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        // key value 序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //acks=0: 生产者不等待任何确认
        //acks=1: 生产者至少等待leader已成功将数据写入到本地日志中
        //acks=all: 生产者等待leader已成功将数据写入到本地日志和所有follower节点
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        //重试次数
        prop.put(ProducerConfig.RETRIES_CONFIG, 10);
        //消息压缩
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

        // 创建生产者
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);

        // 发送消息
        for(int i = 0; i < 5; ++i) {
            ProducerRecord<String, String> kvProducerRecord = new ProducerRecord<>(
                    "itcast-topic-input",
                    "hello kafka"
            );
            producer.send(kvProducerRecord);
            // RecordMetadata recordMetadata = producer.send(kvProducerRecord).get();

            // // 异步
            // producer.send(kvProducerRecord, new Callback() {
            //             @Override
            //             public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            //                 if(e != null) {
            //                     System.out.println("消息发送失败: " + e.getMessage());
            //                 }
            //                 System.out.println("消息发送成功: " + recordMetadata);
            //             }
            //         }
            // );

        }

        // 关闭消息通道
        producer.close();
    }
}
