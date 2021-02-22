package com.zzb.examples.controller.es;

import com.alibaba.fastjson.JSONObject;
import com.zzb.es.controller.DemoEsTitleController;
import com.zzb.es.entity.Title;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：ExamplesDemoEsController
 * 类描述：Es 样例接口
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/13 10:22 上午
 * 修改备注：TODO
 */

@Api(value = "Elasticsearch样例", tags = "Elasticsearch样例")
@RequestMapping("/examples/es/")
@RestController
public class ExamplesDemoEsController extends DemoEsTitleController {

    @ApiOperation(value = "试题搜索样例(带相似度分数显示)", httpMethod = "POST",
        notes = "试题搜索样例,返回字段:<br />" +
            "|-highlightFields:高亮html代码可以复制到http://www.jsons.cn/htmldebug/下预览高亮（匹配到的文字）,<br />" +
            "|--des:题干<br />" +
            "|--analyze:解析<br />" +
            "|--score:匹配分数，由于默认匹配题干和解析，所以分数可能会超过100")
    @ApiIgnore
    @GetMapping(value = "/title/toSearch")
    public ModelAndView toSearch(@RequestParam(required = false) String type,
                                 @RequestParam(required = false) Integer skuId,
                                 @RequestParam(required = false) String desc,
                                 @RequestParam(required = false) String fields, @RequestParam(required = false) Integer count) {
        ModelAndView modelAndView = new ModelAndView("es/es_search");
        modelAndView.addObject("type", type == null ? "ik" : type);
        modelAndView.addObject("skuId", skuId == null ? 8 : skuId);
        modelAndView.addObject("desc", desc == null ? "" : desc);
        modelAndView.addObject("fields", fields == null ? "" : fields);
        modelAndView.addObject("count", count == null ? 10 : count);
        return modelAndView;
    }

    @ApiIgnore
    @PostMapping(value = "/title/searchByHighlightField")
    public ModelAndView searchByHighlightField(@RequestParam(required = false) String type,
                                               @RequestParam int skuId,
                                               @RequestParam String desc,
                                               @RequestParam String fields,
                                               @RequestParam int count) {
        ModelAndView modelAndView = new ModelAndView("es/es_highlight");
        SearchHits<Title> items = null;
        if ("hanlp".equals(type)) {
            items = (SearchHits<Title>) super.searchByTemplateForHanlp(skuId, desc, count, str2Map(fields)).getData();
        } else {
            items = (SearchHits<Title>) super.searchByTemplateForIk(skuId, desc, count, str2Map(fields)).getData();
        }

        List<JSONObject> resultList = new ArrayList<>();
        items.forEach(value -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", value.getId());
            jsonObject.put("score", value.getScore());
            jsonObject.put("des", value.getHighlightField("des"));
            jsonObject.put("analyze", value.getHighlightField("analyze"));
            resultList.add(jsonObject);
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceDes", desc);
        jsonObject.put("dataList", resultList);
        modelAndView.addObject("result", jsonObject);
        return modelAndView;
    }

}
