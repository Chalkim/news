package com.heima.article.stream;

import com.alibaba.fastjson.JSON;
import com.heima.common.constants.HotArticleConstants;
import com.heima.model.mess.ArticleVisitStreamMess;
import com.heima.model.mess.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class HotArticleStreamHandler {

    @Bean
    public KStream<String, String> hotArticleHandle(StreamsBuilder streamsBuilder) {
        // 接收消息
        KStream<String, String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);

        // 聚合处理
        stream.map((k, v) -> {
                    UpdateArticleMess mess = JSON.parseObject(v, UpdateArticleMess.class);
                    // 重置k和v
                    return new KeyValue<>(mess.getArticleId().toString(), mess.getType().name() + ":" + mess.getAdd());
                    // 聚合
                })
                .groupBy((k, v) -> k)
                // 时间窗口
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                /**
                 * 自行实现聚合逻辑
                 */
                .aggregate(new Initializer<String>() {
                    /**
                     * 初始化值，返回值是消息的value
                     * @return
                     */
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                }, new Aggregator<String, String, String>() {
                    /**
                     * 聚合逻辑，返回值是消息的value
                     * @param key
                     * @param value
                     * @param aggValue
                     * @return
                     */
                    @Override
                    public String apply(String key, String value, String aggValue) {
                        if(StringUtils.isBlank(value)) {
                            return aggValue;
                        }

                        String[] aggAry = aggValue.split(",");
                        int col = 0, com = 0, lik = 0, vie = 0;
                        for (String s : aggAry) {
                            String[] split = s.split(":");
                            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])) {
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    lik = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }

                        String[] valAry = value.split(":");
                        switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])) {
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                lik += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }

                        String format = "COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d";
                        String formatString = String.format(format, col, com, lik, vie);
                        log.info("聚合处理中的数据：{}", formatString);
                        return formatString;
                    }
                }, Materialized.as("hot-article-stream-count-001"))
                .toStream()
                .map((k, v) -> {
                    return new KeyValue<>(k.key().toString(), formatObj(k.key().toString(), v));
                })
                // 发送消息
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);

        return stream;
    }

    /**
     * 格式化数据
     * @param articleId
     * @param value
     * @return
     */
    public String formatObj(String articleId, String value) {
        ArticleVisitStreamMess mess = new ArticleVisitStreamMess();
        mess.setArticleId(Long.valueOf(articleId));
        String[] split = value.split(",");
        for (String s : split) {
            String[] valAry = s.split(":");
            switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])) {
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(valAry[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(valAry[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(valAry[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(valAry[1]));
                    break;
            }
        }
        log.info("聚合后的数据：{}", mess.toString());
        return JSON.toJSONString(mess);
    }
}
