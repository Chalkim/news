package com.heima.admin.service;

import com.heima.model.admin.dtos.AdLoginDto;
import com.heima.model.common.dtos.ResponseResult;

public interface AdUserService {

    /**
     * 管理员登录
     * @param dto
     * @return
     */
    public ResponseResult login(AdLoginDto dto);
}
