package com.heima.model.behavior.dtos;

import com.heima.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class FollowBehaviorDto {
    //文章id
    @IdEncrypt
    Long articleId;
    //关注的id
    @IdEncrypt
    Integer followId;
    //用户id
    @IdEncrypt
    Integer userId;
}