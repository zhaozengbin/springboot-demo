package com.zzb.es.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 类名称：Title
 * 类描述：业务demo 因涉及业务不做详细注释
 * - @Document 作用在类，标记实体类为文档对象，一般有两个属性
 * |- - indexName：对应索引库名称
 * |- - type：对应在索引库中的类型
 * |- - shards：分片数量，默认5
 * |- - replicas：副本数量，默认1
 * - @Id 作用在成员变量，标记一个字段作为id主键
 * - @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：
 * |- - type：字段类型，是是枚举：FieldType
 * |- - index：是否索引，布尔类型，默认是true
 * |- - store：是否存储，布尔类型，默认是false
 * |- - analyzer：分词器名称
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:26 上午
 * 修改备注：TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "title_hanlp", shards = 1, replicas = 0)
public class TitleForHanlp {
    @Id
    private long id;

    @Field(type = FieldType.Long)
    private long skuId;

    @Field(type = FieldType.Long)
    private long parentId;

    @Field(type = FieldType.Text, analyzer = "hanlp_index", searchAnalyzer = "hanlp_index")
    private String des;

    @Field(type = FieldType.Text, analyzer = "hanlp_analyzer", searchAnalyzer = "hanlp_analyzer")
    private String analyze;

}
