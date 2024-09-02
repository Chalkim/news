package com.heima.schedule.service.impl;

import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void addTask() {
        for(int i = 0; i < 5; ++i) {
            Task task = new Task();
            task.setTaskType(20+i);
            task.setPriority(50);
            task.setParameters("task test".getBytes());
            task.setExecuteTime(new Date().getTime() + 5000*i);

            Long taskId = taskService.addTask(task);
        }
    }

    @Test
    public void cancelTask() {
        boolean result = taskService.cancelTask(1824485351751151617L);
        System.out.println(result);
    }

    @Test
    public void poll() {
        Task task = taskService.poll(20, 50);
        System.out.println(task);
    }

    @Test
    public void testKeys() {
        Set<String> scan = cacheService.scan("future_*");
        System.out.println(scan);
    }
}
