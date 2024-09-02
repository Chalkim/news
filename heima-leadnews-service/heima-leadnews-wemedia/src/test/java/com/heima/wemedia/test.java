package com.heima.wemedia;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.file.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 测试文本内容审核
     */
    @Test
    public void testTextScan() throws Exception {
        Map map = greenTextScan.greenTextScan("你好");
        System.out.println(map);
    }

    /**
     * 测试图片内容审核
     */
    @Test
    public void testImageScan() throws Exception {
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.200.130:9000/leadnews/2024/08/14/92da756b60eb4a939d4d1f68033d7fec.png");

        List<byte []> list = new ArrayList<>();
        list.add(bytes);

        Map map = greenImageScan.greenImageScan(list);
        System.out.println(map);
    }
}
