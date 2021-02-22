# [elasticsearch][hanlp]【原】自定义分词器

# 1、hanlp介绍
HanLP是一系列模型与算法组成的NLP工具包，目标是普及自然语言处理在生产环境中的应用。HanLP具备功能完善、性能高效、架构清晰、语料时新、可自定义的特点。内部算法经过工业界和学术界考验，配套书籍[《自然语言处理入门》](http://nlp.hankcs.com/book.php)已经出版。目前，基于深度学习的[HanLP 2.0](https://github.com/hankcs/HanLP/tree/doc-zh)正处于alpha测试阶段，未来将实现知识图谱、问答系统、自动摘要、文本语义相似度、指代消解、三元组抽取、实体链接等功能。具体可以直接移步hanlp的GitHub上详细了解。[传送门](https://github.com/zhaozengbin/HanLP)
# 2、hanlp衍生项目
因为HanLP项目的强大功能，有好多在此项目上衍生的优秀项目，其中有几个不错的elasticsearch的分词插件项目，在这里我们就以其中的一个我个人觉得自由度和兼容度比较高的插件项目《[elasticsearch-analysis-hanlp](https://github.com/AnyListen/elasticsearch-analysis-hanlp) @AnyListen》其余衍生项目大家感兴趣的可以移步[传送门](https://github.com/hankcs/HanLP/wiki/%E8%A1%8D%E7%94%9F%E9%A1%B9%E7%9B%AE#elasticsearch-analysis-hanlp-anylisten)。
# 3、[elasticsearch-analysis-hanlp](https://github.com/AnyListen/elasticsearch-analysis-hanlp)
## 3.1. 简介
基于 [HanLP](https://github.com/hankcs/HanLP) 的 Elasticsearch 中文分词插件，核心功能：

1. 兼容 ES 5.x-7.x；
1. 内置词典，无需额外配置即可使用；
1. 支持用户自定义词典；
1. 支持远程词典热更新（待开发）；
1. 内置多种分词模式，适合不同场景；
1. 拼音过滤器（待开发）；
1. 简繁体转换过滤器（待开发）。
## 3.2. 下载编译
### 3.2.1 下载
 git clone 对应版本的代码，打开 `pom.xml` 文件，修改 
```xml
<elasticsearch.version>6.5.1</elasticsearch.version>
```
 为需要的 ES 版本；
### 3.2.2 编译
使用 `mvn package` 生产打包文件，最终文件在 `target/release` 文件夹下。打包完成后，使用离线方式安装即可。
## 3.3. 安装插件

- 在线安装：
```bash
.\elasticsearch-plugin install https://github.com/AnyListen/elasticsearch-analysis-hanlp/releases/download/vA.B.C/elasticsearch-analysis-hanlp-A.B.C.zip
```

- 离线安装：
```bash
.\elasticsearch-plugin install file:///FILE_PATH/elasticsearch-analysis-hanlp-A.B.C.zip
```
离线安装请把 `FILE_PATH` 更改为 zip 文件路径；A、B、C 对应的是 ES 版本号。
## 3.4.配置词典
默认词典是精简版的词典，能够满足基本需求，但是无法使用感知机和 CRF 等基于模型的分词器。
HanLP 提供了更加[完整的词典](http://nlp.hankcs.com/download.php?file=data)，请按需下载。
词典下载后，解压到任意目录，然后修改**插件安装目录下**的 `hanlp.properties` 文件，只需修改第一行
```yaml
root=D:/JavaProjects/HanLP/
```
为 `data` 的父目录即可，比如 `data` 目录是 `/Users/hankcs/Documents/data`，那么 `root=/Users/hankcs/Documents/`。
### 
## 3.5.使用自定义配置文件
如果你在其他地方使用了 HanLP，希望能够复用 `hanlp.properties` 文件，你只需要修改**插件安装目录下**的 `plugin.properties` 文件，将 `configPath` 配置为已有的 `hanlp.properties` 文件地址即可。
## 3.6. 内置分词器
### 3.6.1 分析器(Analysis)

- hanlp_index：细粒度切分
- hanlp_smart：常规切分
- hanlp_nlp：命名实体识别
- hanlp_per：感知机分词
- hanlp_crf：CRF分词
- hanlp：自定义
### 3.6.2 分词器(Tokenizer)

- hanlp_index：细粒度切分
- hanlp_smart：常规切分
- hanlp_nlp：命名实体识别
- hanlp_per：感知机分词
- hanlp_crf：CRF分词
- hanlp：自定义
### 3.6.3 自定义分词器
插件有较为丰富的选项允许用户自定义分词器，下面是可用的配置项：

| 配置项名称 | 功能 | 默认值 |
| --- | --- | --- |
| algorithm | 可选项有：
viterbi：维特比分词 | viterbi |
| enableIndexMode | 设为索引模式（细粒度切分） | false |
| enableCustomDictionary | 是否启用用户词典 | true |
| customDictionaryPath | 用户词典路径(绝对路径,多个词典用`;`隔开) | null |
| enableCustomDictionaryForcing | [用户词典高优先级](https://github.com/hankcs/HanLP/wiki/FAQ#%E4%B8%BA%E4%BB%80%E4%B9%88%E4%BF%AE%E6%94%B9%E4%BA%86%E8%AF%8D%E5%85%B8%E8%BF%98%E6%98%AF%E6%B2%A1%E6%9C%89%E6%95%88%E6%9E%9C) | false |
| enableStopWord | 是否启用停用词过滤 | false |
| stopWordDictionaryPath | 停用词词典路径 | null |
| enableNumberQuantifierRecognize | 是否启用数词和数量词识别 | true |
| enableNameRecognize | 开启人名识别 | true |
| enableTranslatedNameRecognize | 是否启用音译人名识别 | false |
| enableJapaneseNameRecognize | 是否启用日本人名识别 | false |
| enableOrganizationRecognize | 开启机构名识别 | false |
| enablePlaceRecognize | 开启地名识别 | false |
| enableTraditionalChineseMode | 开启精准繁体中文分词 | false |



# 4、elasticsearch设置
## 4.1创建索引指定分词器
```bash
# 创建自定义分词器
# my_index：索引名称，相当于表
# my_analyzer：指定自定义分词的名称 对应 字段里面的analyzer和search_analyzer
# my_analyzer：自定义分词 hanlp_index：细粒度切分
PUT my_index
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "hanlp",
          "algorithm": "viterbi",
          "enableIndexMode": "true",
          "enableCustomDictionary": "true",
          "customDictionaryPath": "",
          "enableCustomDictionaryForcing": "false",
          "enableStopWord": "true",
          "stopWordDictionaryPath": "",
          "enableNumberQuantifierRecognize": "true",
          "enableNameRecognize": "true",
          "enableTranslatedNameRecognize": "true",
          "enableJapaneseNameRecognize": "true",
          "enableOrganizationRecognize": "true",
          "enablePlaceRecognize": "true",
          "enableTraditionalChineseMode": "true"
        }
      }
    }
  },
  "mappings": {
      "properties": {
        "des": {
          "type": "text",
          "analyzer": "my_analyzer",
          "search_analyzer": "hanlp_index"
        },
        "id" : {
          "type" : "long"
        }
    }
  }
}

# 测试分词器
POST my_index/_analyze
{
  "analyzer": "my_analyzer",
  "text": "张惠妹在上海市举办演唱会啦，我們很開心能夠在台北之後又能在上海再次聽到張惠妹的歌曲"
}
```
## 4.2 修改索引
```bash
POST my_analyzer/_mapping
{
  "properties": {
    "des": {
      "type": "text",
      "analyzer": "hanlp_index",
      "search_analyzer": "my_analyzer"
    },
    "id" : {
    	"type" : "long"
    }
  }
}

```
## 4.3 查看mapping
```bash
# 注意 analyzer search_analyzer指定的分词器一致的时候只会看到analyzer的属性，原则如下：
# analyzer：用来分词，包含索引存储阶段和搜索阶段（其中查询阶段可以被search_analyzer参数覆盖），该参数默认设置为index的analyzer设置或者standard analyzer
# index：是否可以被搜索到。默认是true
# fields：Multi-fields允许同一个字符串值同时被不同的方式索引，例如用不同的analyzer使一个field用来排序和聚类，另一个同样的string用来分析和全文检索。下面会做详细的说明
# search_analyzer：这个字段用来指定搜索阶段时使用的分词器，默认使用analyzer的设置
# search_quote_analyzer：搜索遇到短语时使用的分词器，默认使用search_analyzer的设置
GET title_hanlp/_mapping
{
  "title_hanlp" : {
    "mappings" : {
      "properties" : {
        "des" : {
          "type" : "text",
          "analyzer": "hanlp_index",
       		"search_analyzer": "my_analyzer"
        },
        "id" : {
          "type" : "long"
        }
      }
    }
  }
}
```
## 4.4 映射JAVA实体里面指定分词器
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "my_index", type = "docs", shards = 1, replicas = 0)
public class MyIndex {
    @Id
    private long id;

    @Field(type = FieldType.Text, analyzer = "my_analyzer", searchAnalyzer = "my_analyzer")
    private String des;

}
```
## 4.5 分词搜索JAVA代码
```java
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
        boolQueryBuilder.must(QueryBuilders.termQuery("id", 1));
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery("测试分词搜索是否生效",
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

    private String[] fields = new String[]{"des"};

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
```


