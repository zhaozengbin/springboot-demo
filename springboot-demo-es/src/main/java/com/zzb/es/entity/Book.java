package com.zzb.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类名称：Book
 * 类描述：demo1 实体 如果不使用@Field注解指定数据类型的话  Spring Data Elasticsearch框架会自动映射数据类型，如果es服务器中不存在索引的话会自动创建
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:20 上午
 * 修改备注：TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "book")
public class Book {

    // @Id注解表明该字段是文档id
    @Id
    private Integer id;

    // 标题
    private String title;

    // 价格
    private BigDecimal price;

    // 标签
    @Field(type = FieldType.Keyword)
    private List<String> tag;

}
