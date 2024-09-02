package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 加载文章列表
     * @param dto
     * @param type
     * @return
     */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);

    /**
     * 查询5天内的文章
     * @return
     */
    public List<ApArticle> findArticleListByLast5days(@Param("dayParam") Date dayParam);
}
