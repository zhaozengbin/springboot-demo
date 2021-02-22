package com.zzb.es.repository;

import com.zzb.es.entity.Title;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 类名称：TitleRepository
 * 类描述：业务demo 数据仓库
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:33 上午
 * 修改备注：TODO
 */
public interface TitleRepository extends ElasticsearchRepository<Title, Long> {
    /**
     * 方法：findByDes
     * 描述：根据des查询 des必须为title属性
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param des :
     * @return : java.util.List<com.zzb.esdemo.entity.Title>
     * @date: 2020年09月23日 10:34 上午
     */
    List<Title> findByDes(String des);
}
