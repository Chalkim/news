package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {

    /**
     * 保存搜索记录
     * @param keyword
     * @param userId
     */
    public void insert(String keyword, Integer userId);

    /**
     * 查询搜索历史记录
     * @return
     */
    public ResponseResult findUserSearch();

    /**
     * 删除搜索历史记录
     * @return
     */
    public ResponseResult delUserSearch(HistorySearchDto dto);
}
