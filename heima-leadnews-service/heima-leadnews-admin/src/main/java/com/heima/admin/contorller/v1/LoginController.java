package com.heima.admin.contorller.v1;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdLoginDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdUserService adUserService;

    /**
     * 管理员登录
     * @param dto
     * @return
     */
    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdLoginDto dto){
        return adUserService.login(dto);
    }
}
