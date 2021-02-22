package com.zzb.sensitive.serializer;

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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

import static com.zzb.sensitive.utils.ClassUtils.isBaseType;


/**
 * 类名称：SensitiveInfoObjectSerialize
 * 类描述：jackson的脱敏实现
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:59 上午
 * 修改备注：TODO
 */
@Slf4j
public class SensitiveInfoObjectSerialize extends JsonSerializer<Object> implements ContextualSerializer {

    private ESensitiveType type;

    public SensitiveInfoObjectSerialize() {
    }

    public SensitiveInfoObjectSerialize(final SensitiveInfoAnn sensitiveInfo) {
        this.type = sensitiveInfo.value();
    }

    @Override
    public void serialize(final Object s, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (!SecurityPropertiesUtils.getInstance().isEnableJackFilter()) {
            jsonGenerator.writeObject(s);
        }
        try {
            if (this.type == ESensitiveType.NULL && !isBaseType(s.getClass())) {
                jsonGenerator.writeObject(null);
                return;
            }
            jsonGenerator.writeObject(s);
        } catch (Exception e) {
            log.error("脱敏数据处理异常", e);
            jsonGenerator.writeObject(s);
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
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), Object.class)) {
                SensitiveInfoAnn sensitiveInfo = beanProperty.getAnnotation(SensitiveInfoAnn.class);
                if (sensitiveInfo == null) {
                    sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfoAnn.class);
                }
                if (sensitiveInfo != null && sensitiveInfo.value() == ESensitiveType.NULL) {
                    return new SensitiveInfoObjectSerialize(sensitiveInfo);
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
