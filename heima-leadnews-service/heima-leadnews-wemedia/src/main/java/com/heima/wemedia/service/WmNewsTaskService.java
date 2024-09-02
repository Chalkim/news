package com.heima.wemedia.service;


import java.util.Date;

public interface WmNewsTaskService {

    /**
     * 添加文章自动发布任务
     * @param id 文章id
     * @param publishTime 发布时间, 任务执行时间
     */
    public void addNewsToTask(Integer id, Date publishTime);

    /**
     * 消费任务审核文章
     */
    public void scanNewsByTask();
}
