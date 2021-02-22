> 基于springboot的数据脱敏，实现了"模型类"和"AOP注解"两种方法,选择其中一种即可
* 选择任意一种即可,若同时使用，先执行controller层的脱敏，再执行模型类里面的脱敏(返回视图默认Jackson)
* 基于AOP实现的方法，也可用于其它spring方法上，如无效，记得引入spring的aop包
* 脱敏了的数据,前端传回来后，可进行数据回填，参考下面的"数据回写"部分，基于fastJson实现
## 如何使用
```
 <dependency>
   <groupId>com.zzb</groupId>
   <name>springboot-demo-sensitive</name>
   <description>spring boot 脱敏加密Demo</description>
   <version>0.0.1-SNAPSHOT</version>
 </dependency>
```

## 使用fastJson脱敏
```
/**
 * 基于fastJson的数据脱敏
 */
@DesensitizationParamsAnn({
        @DesensitizationParamAnn(type = ESensitiveType.NULL, fields = {"id","address"}),
        @DesensitizationParamAnn(type = ESensitiveType.MOBILE_PHONE, fields = {"phone", "idCard"}),
        @DesensitizationParamAnn(type = ESensitiveType.BANK_CARD, fields = "$..bankCard", mode = HandleType.RGE_EXP),
        @DesensitizationParamAnn(regExp = "(?<=\\w{2})\\w(?=\\w{1})", fields = "$[0].idCard2", mode = HandleType.RGE_EXP)
})
@GetMapping("fast")
public List<UserDesensitization> sensitive(){
    return Arrays.asList(new UserDesensitization(), new UserDesensitization());
}  
```

## 使用jackson脱敏,基于jackson的JsonSerialize实现
```java
@Data
public class UserSensitive {

	@SensitiveInfoAnn(value = ESensitiveType.CHINESE_NAME)
	String name = "张三";

	@SensitiveInfoAnn(value = ESensitiveType.ID_CARD)
	String idCard = "430524202012120832";

	@SensitiveInfoAnn(regExp = "(?<=\\w{3})\\w(?=\\w{4})")
	String idCard2 = "430524202012120832";

	@SensitiveInfoAnn(value = ESensitiveType.MOBILE_PHONE)
	String phone = "1234567890";

	@SensitiveInfoAnn(value = ESensitiveType.FIXED_PHONE)
	String ext = "0739-8888888";

	@SensitiveInfoAnn(value = ESensitiveType.ADDRESS)
	String address = "湖南省长沙市高新区岳麓大道芯城科技园";

	@SensitiveInfoAnn(value = ESensitiveType.NULL)
	String address2 = "湖南省";

	@SensitiveInfoAnn(value = ESensitiveType.BANK_CARD)
	String bankCard = "622260000027736298837";
	
	@SensitiveInfoAnn(value = ESensitiveType.NULL)
	Integer id = 654321;
}
```

### 方法调用输出
```
@SpringBootTest
public class ApplicationTests {

    /**
     * jackson脱敏测试
     * @throws JsonProcessingException
     */
    @Test
    void testSensitive() throws JsonProcessingException {
        UserSensitive user = new UserSensitive();
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);
    }
    
}
```

## 数据回写
有些数据脱敏给前端后,传回给后台时,需要回填到入参里面去,如一些用户ID,手机号等信息
```
/**
 * IndexController.java
 * 数据回填,不给argName默认取第一个参数
 * @param pt1
 * @param pt2
 */
@HyposensitizationParamsAnn({
        @HyposensitizationParamAnn(type = "card", fields = "bankCard"),
        @HyposensitizationParamAnn(argName = "a", type = "string"),
        @HyposensitizationParamAnn(argName = "pt1", type = "phone", fields = {"idCard","phone"}),
        @HyposensitizationParamAnn(argName = "pt2", type = "reg", fields = {"$..address", "$.bankCard"}, mode = HandleType.RGE_EXP)
})
@GetMapping("undo")
public String Hyposensitization(UserDesensitization pt1, UserSensitive pt2, String a){
    return JSON.toJSONString(Arrays.asList(pt1, pt2, a));
}

//PtoUndoObserver.java
import com.github.doobo.undo.UndoObserver;
import com.github.doobo.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndoObserver extends UndoObserver {

    /**
     * 继承观察者,可填充到方法的入参里面
     * @param vo
     */
    @Override
    public void undoValue(UndoVO vo) {
        synchronized (this) {
            if (vo.getType().equals("card")) {
                vo.undo("...1");
            }
            if (vo.getType().equals("phone")) {
                vo.undo("......2");
            }
            if (vo.getType().equals("reg")) {
                vo.undo(".........3");
            }
            if(vo.getType().equals("string")){
                vo.undo(4);
            }
        }
    }
}
```

## 全局配置是否启动相关功能
```yaml
server:
    port: 80
sensitive:
    filter:
        fast:
            enable: true # 开启fast脱敏
        jack:
            enable: true # 开启jack脱敏
        undo:
            enable: true # 开启undo数据回写
        encryption:
            enable: true # 开启加密
            encryption-key:
                aes: "7ebd2b224cea49398509b188" # 24位编码
                des: "7ebd2b224cea49398509b188" # 24位编码
                sm4: "7ebd2b224cea4939" # 16为编码
```

## 脱敏结果
```json
[
  {
    "name": "张三",
    "idCard": "430************832",
    "idCard2": "43***************2",
    "phone": "123****890",
    "ext": "0739-8888888",
    "address": null,
    "address2": "湖南省",
    "bankCard": "622260***********8837",
    "id": null
  },
  {
    "name": "张三",
    "idCard": "430************832",
    "idCard2": "430524202012120832",
    "phone": "123****890",
    "ext": "0739-8888888",
    "address": null,
    "address2": "湖南省",
    "bankCard": "622260***********8837",
    "id": null
  }
]
```

## 数据回写结果
```json
[
  {
    "address": "湖南省长沙市高新区岳麓大道芯城科技园",
    "address2": "湖南省",
    "bankCard": "...1",
    "ext": "0739-8888888",
    "id": 123456,
    "idCard": "......2",
    "idCard2": "430524202012120832",
    "name": "张三",
    "phone": "......2"
  },
  {
    "address": "湖南省长沙市高新区岳麓大道芯城科技园",
    "address2": "湖南省",
    "bankCard": "622260000027736298837",
    "ext": "0739-8888888",
    "id": "a",
    "idCard": "430524202012120832",
    "idCard2": "430524202012120832",
    "name": "张三",
    "phone": "1234567890"
  },
  null,
  {
    "address": "www.5fu8.com",
    "author": "............5"
  }
]
```


## 开启加密,基于内置算法加密实现
### UserEncryption.java
```java
@Data
@EncryptionEntityAnn
public class UserEncryption {

    String name = "张三";

    String idCard = "430524202012120832";

    String idCard2 = "430524202012120832";

    String phone = "1234567890";

    String ext = "0739-8888888";

    String address = "湖南省长沙市高新区岳麓大道芯城科技园";

    String address2 = "湖南省";

    @SensitiveInfoAnn(value = ESensitiveType.BANK_CARD)
    @EncryptionFieldAnn(isNeedEencryption = true, name = "bankCard", type = String.class, encodeFieldAnn = {
        @EncryptionEncodeFieldAnn(value = EEncryptionType.AES, name = "bankCardAES"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.DES_EDE, name = "bankCardDes"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.SM4, name = "bankCardSM4")
    })
    String bankCard = "622260000027736298837";

    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.AES))
    String bankCardAES = "";

    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.DES_EDE))
    String bankCardDes = "";

    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.SM4))
    String bankCardSM4 = "";

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    char id = 'c';
}
```

### UndoService.java
```java
@Service
public class UndoService {
    @EncryptionMethodAnn(type = EMethodType.READ, isPrintLog = true, isDecode = false)
    public UserEncryption testEncryption(UserEncryption userEncryption) {
        return userEncryption;
    }
}
```


## 加密结果
```json
{
  "address": "湖南省长沙市高新区岳麓大道芯城科技园",
  "address2": "湖南省",
  "bankCardAES": "0e7117ac86a61717ec32dedee99ded569f86f180ca342d5f4c190dfb73fdcf47",
  "bankCardDes": "7bc1bda5d3ccb323ce2da7d65eac846526fae65692cb0218",
  "bankCardSM4": "227ca8e357eb4c44291dc83aee7d3ffc55ceffc0213974669a8cb728125f171e",
  "ext": "0739-8888888",
  "id": "c",
  "idCard": "430524202012120832",
  "idCard2": "430524202012120832",
  "name": "张三",
  "phone": "1234567890"
}
```

# 特别说明

参考及使用源码：
* **doobo@spring-sensitive**项目传送门：[gitee](https://gitee.com/doobo/spring-sensitive) | [github](https://github.com/doobo/spring-sensitive)
* **loolly@hutool**项目传送门： [gitee](https://gitee.com/loolly/hutool) | [github](https://github.com/looly/hutool)

**如果有冒犯，请加VX：4415599沟通。**