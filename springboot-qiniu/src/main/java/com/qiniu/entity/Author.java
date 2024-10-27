package com.qiniu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gao
 * @version 1.0
 * @description: 开发者
 * @date 2024/10/28 0:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("author_information")
public class Author {

    @TableId(type = IdType.AUTO)
    private String author;

    private int  lines;
    private int number;
    private int importance;
}
