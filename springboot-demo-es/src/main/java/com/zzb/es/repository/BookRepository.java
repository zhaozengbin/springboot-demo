package com.zzb.es.repository;

import com.zzb.es.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类名称：BookRepository
 * 类描述：demo1 数据仓库
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:30 上午
 * 修改备注：TODO
 */
public interface BookRepository extends ElasticsearchRepository<Book, Integer> {

    /**
     * 方法：findByPriceBetween
     * 描述：按价格区间查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param min :
     * @param max :
     * @return : java.util.List<com.zzb.esdemo.entity.Book>
     * @date: 2020年09月23日 10:31 上午
     */
    List<Book> findByPriceBetween(BigDecimal min, BigDecimal max);

    /**
     * 方法：findByTitle
     * 描述：按书名查询，因为使用了中文分词器ik，所以这里并不是精确查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param title :
     * @return : java.util.List<com.zzb.esdemo.entity.Book>
     * @date: 2020年09月23日 10:31 上午
     */
    List<Book> findByTitle(String title);

    /**
     * 方法：findByTagIn
     * 描述：按标签匹配查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param tags :
     * @return : java.util.List<com.zzb.esdemo.entity.Book>
     * @date: 2020年09月23日 10:32 上午
     */
    List<Book> findByTagIn(List<String> tags);
}
