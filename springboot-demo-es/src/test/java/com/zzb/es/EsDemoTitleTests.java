package com.zzb.es;

import com.zzb.es.entity.Title;
import com.zzb.es.repository.TitleRepository;
import com.zzb.es.utils.HtmlUtils;
import com.zzb.mysql.utils.JDBCUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：EsDemoTitleTests
 * 类描述：业务demo单元测试
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:58 上午
 * 修改备注：TODO
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
public class EsDemoTitleTests {

    @Autowired
    private TitleRepository titleRepository;

    //自动注入即可使用
    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    @Test
    public void delTitle() {
        titleRepository.deleteAll();
    }

    /**
     * 方法：readTitle
     * 描述：数据库数据导入es
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:59 上午
     */
    @Test
    public void initTitle() {

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<Title> list = null;
        try {
            conn = JDBCUtils.getConnection();
            //定义sql
            String sql = "SELECT * FROM ti_title where sku = 8";
            //获取执行sql的对象
            st = conn.createStatement();
            //执行sql
            rs = st.executeQuery(sql);
            Title bean = null;
            list = new ArrayList<Title>();
            while (rs.next()) {
                long sku = rs.getLong("sku");
                long titleId = rs.getLong("id");
                long parentId = rs.getLong("parent_id");
                String des = rs.getString("des");
                String analyze_text = rs.getString("analyze_text");
                if (HtmlUtils.getTextFromHtml(des) == null && HtmlUtils.getTextFromHtml(analyze_text) == null) {
                    continue;
                } else {
                    bean = new Title(titleId, sku, parentId, HtmlUtils.getTextFromHtml(des), HtmlUtils.getTextFromHtml(analyze_text));
                }
                list.add(bean);
            }
            titleRepository.saveAll(list);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, st, conn);
        }
    }

    @Test
    public void readTitle() {
        System.out.println(titleRepository.count());
    }

    /**
     * 方法：search
     * 描述：搜索
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:59 上午
     */
    @Test
    public void search() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery("A矿业公司开采销售应税矿产品，资源税实行从量计证，则该公司计征资源税的课税数量是()。",
            fields);
        queryBuilder.withQuery(matchQueryBuilder);
        queryBuilder.withMinScore(0.9f);
        queryBuilder.withSort(SortBuilders.scoreSort());
        queryBuilder.withPageable(PageRequest.of(0, 10));
        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        // 搜索，获取结果
        Page<Title> items = titleRepository.search(nativeSearchQuery);
        printResult(nativeSearchQuery, items);
    }

    /**
     * 方法：search
     * 描述：搜索
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月23日 10:59 上午
     */
    @Test
    public void searchRepositoryBySku() {
        NativeSearchQuery nativeSearchQuery = getQuery();
        // 搜索，获取结果
        Page<Title> items = titleRepository.search(nativeSearchQuery);
        printResult(nativeSearchQuery, items);
    }

    /**
     * 方法：searchRestTemplateBySku
     * 描述：根据template查询
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年09月28日 3:30 下午
     */
    @Test
    public void searchRestTemplateBySku() {
        NativeSearchQuery nativeSearchQuery = getQuery();
        // 搜索，获取结果
        SearchHits<Title> searchHit = esRestTemplate.search(nativeSearchQuery, Title.class, IndexCoordinates.of("title"));
        printResult(nativeSearchQuery, searchHit);
    }

    /**
     * 方法：getQuery
     * 描述：组装查询语句
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.data.elasticsearch.core.query.NativeSearchQuery
     * @date: 2020年09月28日 5:55 下午
     */
    private NativeSearchQuery getQuery() {
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 组合多种查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("skuId", 8));
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery("A矿业公司开采销售应税矿产品，共伴生矿与主矿产品销售额分开核算的",
            fields).fields(getFields()));
        // 设置相似度分数阈值
        // queryBuilder.withMinScore(100.0f);
        // 设置相似度分数倒序排序
        queryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        // 设置分页
        queryBuilder.withPageable(PageRequest.of(0, 10));

        // 添加基本分词查询
        queryBuilder.withQuery(boolQueryBuilder);

        // 设置高亮
        setHighlightBuilder(queryBuilder);

        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        return nativeSearchQuery;
    }

    private String[] fields = new String[]{"des", "analyze"};

    /**
     * 方法：getFields
     * 描述：设置字段权重
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.util.Map<java.lang.String,java.lang.Float>
     * @date: 2020年09月28日 5:54 下午
     */
    private Map<String, Float> getFields() {
        Map<String, Float> result = new HashMap<>();
        result.put("des", 1.0f);
        result.put("analyze", 1.0f);
        return result;
    }

    private void setHighlightBuilder(NativeSearchQueryBuilder queryBuilder) {
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
            new HighlightBuilder.Field("des");
        highlightTitle.highlighterType("unified");
        highlightBuilder.field(highlightTitle);
        highlightBuilder.preTags("<b class='key' style='color:red'>");
        highlightBuilder.postTags("</b>");
        queryBuilder.withHighlightBuilder(highlightBuilder);
    }

    /**
     * 方法：printResult
     * 描述：打印结果
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param nativeSearchQuery :
     * @param items             :
     * @return : void
     * @date: 2020年09月28日 5:55 下午
     */

    private <T extends Streamable> void printResult(NativeSearchQuery nativeSearchQuery, T items) {
        // 总条数
        System.out.println("sql = " + nativeSearchQuery.getQuery().toString());
        items.forEach(item -> System.out.println("item = " + item));
    }
}
