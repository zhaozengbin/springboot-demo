# ES实现全文检索

elasticsearch一个准实时的搜索引擎，基于lucene构建，它的主要强项还是在全文检索方面。工作中还是使用到了这部分功能，这里做一个简单的总结，可以使初次使用的人很快的配置和使用。<br />**一、全文检索的概念**<br />首先介绍全文检索的概念，就是对一篇文章进行索引，可以根据关键字搜索，类似于mysql里的like语句。<br />全文索引就是把内容根据词的意义进行分词，然后分别创建索引，例如”你们的激情是因为什么事情来的” 可能会被分词成：“你们“，”激情“，“什么事情“，”来“ 等token<br />这样当你搜索“你们” 或者 “激情” 都会把这句搜出来。<br />**二、内置分词器**<br />elasticsearch实现全文索引，首先要确定分词器，elasticsearch默认有很多分词器，你可以参考elasticsearch的官方文档。了解分词器主要是怎么实现的。<br />你可以使用
```bash
curl -XGET ‘http://192.168.1.101:9200/_analyze?analyzer=standard’ -d ‘你们有什么事情’
```
命令来了解各种分词器的分词效果。<br />**三、中文分词器**<br />一般中文分词器一般使用第三方的ik分词器、mmsegf分词器和paoding分词器，他们最初可能构建于lucene，后来移植于elasticsearch。 在最新版的elasticsearch，我们主要使用了ik分词器。<br />安装ik分词器到elasticsearch很简单，它有个插件目录analysis-ik，和一个配置目录ik, 分别拷贝到plugins和conf目录就可以了。当然你可以使用elasticsearch的plugin命令去安装，这个过程可能会有些麻烦。<br />然后在elasticsearch.yml文件中配置
```yaml
index:
  analysis:
    analyzer:
      ik:
          alias: [ik_analyzer]
          type: org.elasticsearch.index.analysis.IkAnalyzerProvider
      ik_max_word:
          type: ik
          use_smart: false
      ik_smart:
          type: ik
          use_smart: true
```
意思就是ik分词器，也可以使用别名ik_analyzer，使用IkAnalyzerProvider类分词。<br />ik_max_word、ik_smart也是ik分词器，只不过一个打开了use_smart开关，一个没打开use_smart。这个本文不关心。<br />**四、curl命令测试分词器**<br />第三方的分词器，你是没法使用
```bash
curl -XGET ‘http://192.168.1.101:9200/_analyze?analyzer=standard’ -d ‘你们有什么事情’ 来查看分词效果的。
```
你必须创建一个指定该分词器的索引才行。<br />1、创建索引
```bash
curl -XPUT http://192.168.1.101:9200/index
```
2、创建mapping，这里就一个字段content
```bash
curl -XPOST http://192.168.1.101:9200/index/fulltext/_mapping -d'
```
```json
{
    "fulltext": {
             "_all": {
            "indexAnalyzer": "ik",
            "searchAnalyzer": "ik",
            "store": "false"
        },
        "properties": {
            "content": {
                "type": "string",
                "store": "no",
                "indexAnalyzer": "ik",
                "searchAnalyzer": "ik"
            }
        }
    }
}
```
3、查看分词效果
```bash
curl -XGET ‘http://192.168.1.101:9200/index/_analyze?analyzer=ik’ -d ‘你们有什么事情’
```
4、索引数据
```bash
curl -XPOST http://192.168.1.101:9200/index/fulltext/1 -d'{content:”美国留给伊拉克的是个烂摊子吗”}’
curl -XPOST http://192.168.1.101:9200/index/fulltext/2 -d'{content:”公安部：各地校车将享最高路权”}’
curl -XPOST http://192.168.1.101:9200/index/fulltext/3 -d'{content:”中韩渔警冲突调查：韩警平均每天扣1艘中国渔船”}’
curl -XPOST http://192.168.1.101:9200/index/fulltext/4 -d'{content:”中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首”}’
```
5、全文检索<br />term检索，如果content分词后含有中国这个token，就会检索到
```bash
curl -XPOST http://192.168.1.101:9200/index/fulltext/_search  -d'
```
```json
{"query" : { "term" : { "content" : "中国" }}}
```
querystring检索，它会先把”中国美国“分词成中国、美国分别去检索，然后最后默认是OR的关系
```bash
curl -XPOST http://192.168.22.161:9200/index/fulltext/_search  -d'
```
```json
{
    "query" : {
          "query_string" : {
               "default_field" : "content",
               "query" : "中国美国"
          }
       }
}
```
```
你也可以明显的写成
“query” : “中国 AND 美国”
或者
“query” : “中国 OR 美国”
如果你把查询条件加上双引号
“query” : “\”中国美国\””
便类似mysql里的like的效果
```
**五、java客户端**<br />java程序都有对应的类和方法。创建索引和设置mapping,这里就不赘述了，这里有总结：<br />主要是检索：<br />term搜索主要是用：
```java
QueryBuilders.termQuery(“content”, “中国”);
```
querystring搜索使用：
```java
QueryStringQueryBuilder queryString = new QueryStringQueryBuilder(“中国 OR 美国”);
queryString.field(“content”);
```
