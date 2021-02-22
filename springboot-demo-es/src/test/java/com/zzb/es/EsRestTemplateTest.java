package com.zzb.es;

import com.zzb.es.entity.Book;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.math.BigDecimal;

/**
 * 类名称：EsRestTemplateTest
 * 类描述：ElasticsearchRestTemplate的简单使用
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 11:00 上午
 * 修改备注：TODO
 */
//@SpringBootTest
public class EsRestTemplateTest {
    //自动注入即可使用
    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    /**
     * 方法：testQueryBookById
     * 描述：按id查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 11:00 上午
     */
    @Test
    void testQueryBookById() {
        Book book = esRestTemplate.get("1", Book.class);
        Assertions.assertNotNull(book);
        System.out.println(book.toString());
    }

    /**
     * 方法：testQueryBookByTitle
     * 描述：按书名查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 11:00 上午
     */
    @Test
    void testQueryBookByTitle() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "Java"))
                .build();
        SearchHits<Book> searchHits = esRestTemplate.search(searchQuery, Book.class);
        //SearchHits就是查询的结果集
        searchHits.get().forEach(hit -> {
            System.out.println(hit.getContent());
        });
    }

    /**
     * 方法：testQueryBookByPriceInternal
     * 描述：按价格区间查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 11:00 上午
     */
    @Test
    void testQueryBookByPriceInternal() {

        BigDecimal min = new BigDecimal("15.00");
        BigDecimal max = new BigDecimal("30.00");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.rangeQuery("price")
                        .gte(min)
                        .lte(max))
                .build();
        SearchHits<Book> searchHits = esRestTemplate.search(searchQuery, Book.class);
        searchHits.get().forEach(hit -> {
            System.out.println(hit.getContent());
        });
    }

    /**
     * 方法：testQueryBookByTag
     * 描述：按标签匹配查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 11:01 上午
     */
    @Test
    void testQueryBookByTag() {
        //查询标签中含有Java和数据库的书籍
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termsQuery("tag", "Java", "数据库"))
                .build();
        SearchHits<Book> searchHits = esRestTemplate.search(searchQuery, Book.class);
        searchHits.get().forEach(hit -> {
            System.out.println(hit.getContent());
        });
    }

    /**
     * 方法：testAggregationBookAvgPrice
     * 描述：聚合操作-计算所有书籍的平均价格
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 11:01 上午
     */
    @Test
    void testAggregationBookAvgPrice() {

        //聚合名为avg_price，对price字段进行聚合，计算平均值
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.avg("avg_price").field("price"))
                .build();
        SearchHits<Book> searchHits = esRestTemplate.search(searchQuery, Book.class);
        searchHits.get().forEach(hit -> {
            System.out.println(hit.getContent());
        });
        //获取聚合结果
        if (searchHits.hasAggregations()) {
            ParsedAvg parsedAvg = searchHits.getAggregations().get("avg_price");
            Assertions.assertNotNull(parsedAvg, "无聚合结果");
            System.out.println(parsedAvg.getValue());
        }
    }
}
