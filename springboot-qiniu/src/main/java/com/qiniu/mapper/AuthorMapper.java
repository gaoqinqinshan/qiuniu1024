package com.qiniu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qiniu.entity.Author;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AuthorMapper extends BaseMapper<Author> {
}
