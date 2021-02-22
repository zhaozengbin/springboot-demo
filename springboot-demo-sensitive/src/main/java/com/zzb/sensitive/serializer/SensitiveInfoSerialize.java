package com.zzb.sensitive.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.zzb.sensitive.aop.ann.SensitiveInfoAnn;
import com.zzb.sensitive.enmu.ESensitiveType;
import com.zzb.sensitive.utils.SecurityPropertiesUtils;
import com.zzb.sensitive.utils.SensitiveInfoUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * 类名称：SensitiveInfoSerialize
 * 类描述：jackson的脱敏实现
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/17 11:23 上午
 * 修改备注：TODO
 */
@Slf4j
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {


    private ESensitiveType type;

    private SensitiveInfoAnn sensitiveInfo;

    public SensitiveInfoSerialize() {
    }

    public SensitiveInfoSerialize(final SensitiveInfoAnn sensitiveInfo) {
        this.type = sensitiveInfo.value();
        this.sensitiveInfo = sensitiveInfo;
    }

    @Override
    public void serialize(final String s, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (!SecurityPropertiesUtils.getInstance().isEnableJackFilter()) {
            jsonGenerator.writeString(s);
        }
        try {
            //有正则优先使用正则
            if (StrUtil.isNotEmpty(sensitiveInfo.regExp())) {
                jsonGenerator.writeString(s.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr()));
                return;
            }
            switch (this.type) {
                case CHINESE_NAME: {
                    jsonGenerator.writeString(SensitiveInfoUtils.chineseName(s));
                    break;
                }
                case ID_CARD:
                case MOBILE_PHONE: {
                    jsonGenerator.writeString(SensitiveInfoUtils.idCardNum(s, sensitiveInfo.idFront(), sensitiveInfo.idBack()));
                    break;
                }
                case FIXED_PHONE: {
                    jsonGenerator.writeString(SensitiveInfoUtils.fixedPhone(s));
                    break;
                }
                case PASSWORD: {
                    jsonGenerator.writeString(SensitiveInfoUtils.password(s));
                    break;
                }
                case ADDRESS: {
                    jsonGenerator.writeString(SensitiveInfoUtils.address(s, sensitiveInfo.addSize()));
                    break;
                }
                case EMAIL: {
                    jsonGenerator.writeString(SensitiveInfoUtils.email(s));
                    break;
                }
                case BANK_CARD: {
                    jsonGenerator.writeString(SensitiveInfoUtils.bankCard(s));
                    break;
                }
                case SHOPS_CODE: {
                    jsonGenerator.writeString(SensitiveInfoUtils.shopsCode(s));
                    break;
                }
                case NULL: {
                    jsonGenerator.writeString((String) null);
                    break;
                }
                default: {
                    jsonGenerator.writeString(s);
                }
            }
        } catch (Exception e) {
            jsonGenerator.writeString(s);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            if (!SecurityPropertiesUtils.getInstance().isEnableJackFilter()) {
                return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
            }
            SensitiveInfoAnn sensitiveInfo = beanProperty.getAnnotation(SensitiveInfoAnn.class);
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                if (sensitiveInfo != null) {
                    // 如果能得到注解,就将注解的 value 传入 SensitiveInfoSerialize
                    return new SensitiveInfoSerialize(sensitiveInfo);
                }
            }
            if (sensitiveInfo != null && sensitiveInfo.value() == ESensitiveType.NULL) {
                return new SensitiveInfoObjectSerialize(sensitiveInfo);
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
