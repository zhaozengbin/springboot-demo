package com.zzb.es.repository;

import com.zzb.es.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 类名称：ItemRepository
 * 类描述：demo2 数据仓库
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:32 上午
 * 修改备注：TODO
 */
public interface ItemRepository extends ElasticsearchRepository<Item, Long> {

    /**
     * 方法：findByPriceBetween
     * 描述：根据价格区间查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param price1 :
     * @param price2 :
     * @return : java.util.List<com.zzb.esdemo.entity.Item>
     * @date: 2020年09月23日 10:33 上午
     */
    List<Item> findByPriceBetween(double price1, double price2);
}
