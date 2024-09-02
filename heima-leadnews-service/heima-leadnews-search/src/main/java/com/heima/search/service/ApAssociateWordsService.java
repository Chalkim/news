package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;

public interface ApAssociateWordsService {

    /**
     * 联想词查询
     * @param dto
     * @return
     */
    public ResponseResult search(UserSearchDto dto);
}
