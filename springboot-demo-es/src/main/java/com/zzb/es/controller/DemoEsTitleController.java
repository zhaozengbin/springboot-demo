package com.zzb.es.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzb.core.controller.BaseDemoController;
import com.zzb.core.controller.vo.BaseDemoVo;
import com.zzb.core.utils.HttpUtils;
import com.zzb.es.entity.Title;
import com.zzb.es.entity.TitleForHanlp;
import com.zzb.es.repository.TitleRepository;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Api(value = "Elasticsearch接口样例", tags = "Elasticsearch样例")
@RestController
public class DemoEsTitleController extends BaseDemoController {
    private static final Log LOG = LogFactory.get(DemoEsTitleController.class);
    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private ElasticsearchRestTemplate esRestTemplate;

    @ApiOperation(value = "试题搜索样例(不带相似度分数显示)", notes = "试题搜索样例", httpMethod = "POST")
    @PostMapping("/title/searchByRepositor")
    @ResponseBody
    public BaseSwaggerVo<Page<Title>> titleSearchByRepository(
        @ApiParam(name = "skuId", value = "指定搜索SkuId,目前Demo开发服务器只支持8Sku下的搜索", defaultValue = "8", required = true) int skuId,
        @ApiParam(name = "desc", value = "指定搜索描述", defaultValue = "", required = true) String desc,
        @ApiParam(name = "fields", value = "指定搜索字段及权重{\"des\":1.0,\"analyze\":0.5}", defaultValue = "", required = true) String fields,
        @ApiParam(name = "count", value = "指定返回条数", defaultValue = "1", required = true) int count) {
        NativeSearchQuery nativeSearchQuery = getQuery(skuId, desc, count, str2Map(fields));
        // 搜索，获取结果
        Page<Title> items = titleRepository.search(nativeSearchQuery);
        return BaseSwaggerVo.success(desc, items);
    }

    @ApiOperation(value = "试题搜索样例(带相似度分数显示)", httpMethod = "POST",
        notes = "试题搜索样例,返回字段:<br />" +
            "|-highlightFields:高亮html代码可以复制到http://www.jsons.cn/htmldebug/下预览高亮（匹配到的文字）,<br />" +
            "|--des:题干<br />" +
            "|--analyze:解析<br />" +
            "|--score:匹配分数，由于默认匹配题干和解析，所以分数可能会超过100")
    @PostMapping("/title/searchByTemplate")
    @ResponseBody
    public BaseSwaggerVo titleSearchByTemplate(
        @ApiParam(name = "type", value = "搜索类型[ik/hanlp]", defaultValue = "ik", required = true) String type,
        @ApiParam(name = "skuId", value = "指定搜索SkuId,目前Demo开发服务器只支持8Sku下的搜索", defaultValue = "8", required = true) int skuId,
        @ApiParam(name = "desc", value = "指定搜索描述,模拟扫描到的试题文本", defaultValue = "", required = true) String desc,
        @ApiParam(name = "fields", value = "指定搜索字段及权重{\"des\":1.0,\"analyze\":0.5}", defaultValue = "", required = true) String fields,
        @ApiParam(name = "count", value = "指定返回条数", defaultValue = "1", required = true) int count, HttpServletRequest request) {
        BaseSwaggerVo baseSwaggerVo = null;
        if ("hanlp".equals(type)) {
            baseSwaggerVo = searchByTemplateForHanlp(skuId, desc, count, str2Map(fields)).toObject(BaseSwaggerVo.class);
        } else {
            baseSwaggerVo = searchByTemplateForIk(skuId, desc, count, str2Map(fields)).toObject(BaseSwaggerVo.class);
        }
        String pathParam = "/examples/es/title/toSearch?type=%s&skuId=%d&count=%d&fields=%s&desc=%s";
        baseSwaggerVo.setMsg("复制链接查看匹配结果高亮:" + HttpUtils.getServerUrl(request) + String.format(pathParam, type, skuId, count, URLEncoder.encode(fields), desc));
        return baseSwaggerVo;
    }


    protected BaseDemoVo searchByTemplateForIk(int skuId, String desc, int count, Map<String, Float> fieldMap) {
        NativeSearchQuery nativeSearchQuery = getQuery(skuId, desc, count, fieldMap);
        // 搜索，获取结果
        SearchHits<Title> searchHit = esRestTemplate.search(nativeSearchQuery, Title.class, IndexCoordinates.of("title"));
        return BaseDemoVo.success(desc, searchHit);
    }

    protected BaseDemoVo searchByTemplateForHanlp(int skuId, String desc, int count, Map<String, Float> fieldMap) {
        NativeSearchQuery nativeSearchQuery = getQuery(skuId, desc, count, fieldMap);
        // 搜索，获取结果
        SearchHits<TitleForHanlp> searchHit = esRestTemplate.search(nativeSearchQuery, TitleForHanlp.class, IndexCoordinates.of("title_hanlp"));
        return BaseDemoVo.success(desc, searchHit);
    }


    private NativeSearchQuery getQuery(int skuId, String desc, int count, Map<String, Float> fieldMap) {
        /**
         *  方法：getQuery
         *  描述：组装查询语句
         *  作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
         *  @date: 2020年10月16日 2:37 下午
         *  @param skuId : sku
         *  @param desc : 描述
         *  @param count : 条数
         *  @param fieldMap : 匹配字段 key 字段  value 权重 0~1
         *  @return : org.springframework.data.elasticsearch.core.query.NativeSearchQuery
         */
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 组合多种查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("skuId", skuId));
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(desc,
            fieldMap.keySet().toArray(new String[0])).fields(fieldMap));
        // 设置相似度分数阈值
        // queryBuilder.withMinScore(100.0f);
        // 设置相似度分数倒序排序
        queryBuilder.withSort(SortBuilders.scoreSort().order(SortOrder.DESC));
        // 设置分页
        queryBuilder.withPageable(PageRequest.of(0, count));

        // 添加基本分词查询
        queryBuilder.withQuery(boolQueryBuilder);

        // 设置高亮
        setHighlightBuilder(queryBuilder);

        NativeSearchQuery nativeSearchQuery = queryBuilder.build();
        return nativeSearchQuery;
    }


    private void setHighlightBuilder(NativeSearchQueryBuilder queryBuilder) {
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        HighlightBuilder.Field highlightDes =
            new HighlightBuilder.Field("des");
        HighlightBuilder.Field highlightAnalyze =
            new HighlightBuilder.Field("analyze");

        highlightBuilder.field(highlightDes).field(highlightAnalyze);

        highlightDes.highlighterType("unified");
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
        LOG.debug("sql = {}", nativeSearchQuery.getQuery().toString());
        items.forEach(item -> LOG.debug("item = {}", item));
    }


    protected Map<String, Float> str2Map(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        Map<String, Float> result = new HashMap<>();
        jsonObject.forEach((key, value) -> result.put(key, Float.valueOf(value.toString())));
        return result;
    }
}
