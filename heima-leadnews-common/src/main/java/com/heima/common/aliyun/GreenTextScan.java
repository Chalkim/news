package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")
public class GreenTextScan {

    private String accessKeyId;
    private String secret;

    public Map greenTextScan(String content) throws Exception {
        System.out.println(accessKeyId);
        IClientProfile profile = DefaultProfile
                .getProfile("cn-shanghai", accessKeyId, secret);
        DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景，文本垃圾检测传递：antispam
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        System.out.println(JSON.toJSONString(data, true));
        textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);

        Map<String, String> resultMap = new HashMap<>();

        // randomly generated result (1. pass, 2. block, 3. review)
        // label:
        //    normal：正常文本
        //    spam：含垃圾信息
        //    ad：广告
        //    politics：涉政
        //    terrorism：暴恐
        //    abuse：辱骂
        //    porn：色情
        //    flood：灌水
        //    contraband：违禁
        //    meaningless：无意义
        //    harmful：不良场景（支持拜金炫富、追星应援、负面情绪、负面诱导等检测场景）
        //    customized：自定义（例如命中自定义关键词）

        List<String> labelList = Arrays.asList("normal", "spam", "ad", "politics", "terrorism", "abuse", "porn",
                "flood", "contraband", "meaningless", "harmful", "customized");

        Random random = new Random();
        int randomResult = random.nextInt(3);
        // randomly select label
        String label = labelList.get(random.nextInt(labelList.size()));

        // delay to simulate
        Thread.sleep(100);

        randomResult = 0;

        if (randomResult == 0) {
            resultMap.put("suggestion", "pass");
            return resultMap;
        } else if (randomResult == 1) {
            resultMap.put("suggestion", "block");
            resultMap.put("label", label);
            return resultMap;
        } else {
            resultMap.put("suggestion", "review");
            resultMap.put("label", label);
            return resultMap;
        }

        // try {
        //     HttpResponse httpResponse = client.doAction(textScanRequest);
        //     if (httpResponse.isSuccess()) {
        //         JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
        //         System.out.println(JSON.toJSONString(scrResponse, true));
        //         if (200 == scrResponse.getInteger("code")) {
        //             JSONArray taskResults = scrResponse.getJSONArray("data");
        //             for (Object taskResult : taskResults) {
        //                 if (200 == ((JSONObject) taskResult).getInteger("code")) {
        //                     JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
        //                     for (Object sceneResult : sceneResults) {
        //                         String scene = ((JSONObject) sceneResult).getString("scene");
        //                         String label = ((JSONObject) sceneResult).getString("label");
        //                         String suggestion = ((JSONObject) sceneResult).getString("suggestion");
        //                         System.out.println("suggestion = [" + label + "]");
        //                         if (!suggestion.equals("pass")) {
        //                             resultMap.put("suggestion", suggestion);
        //                             resultMap.put("label", label);
        //                             return resultMap;
        //                         }

        //                     }
        //                 } else {
        //                     return null;
        //                 }
        //             }
        //             resultMap.put("suggestion", "pass");
        //             return resultMap;
        //         } else {
        //             return null;
        //         }
        //     } else {
        //         return null;
        //     }
        // } catch (ServerException e) {
        //     e.printStackTrace();
        // } catch (ClientException e) {
        //     e.printStackTrace();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // return null;
    }

}