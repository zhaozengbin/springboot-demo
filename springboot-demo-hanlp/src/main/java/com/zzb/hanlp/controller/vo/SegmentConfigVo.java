package com.zzb.hanlp.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 类名称：SegmentConfigVo
 * 类描述：分词配置文件
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/27 2:48 下午
 * 修改备注：TODO
 */
@ApiModel(value = "分词配置实体", description = "分词配置实体")
public class SegmentConfigVo {
    /**
     * 设为索引模式
     */
    @ApiModelProperty(notes = "设为索引模式", value = "设为索引模式", example = "false")
    private boolean enableIndexMode;
    /**
     * 索引模式下的最小切分颗粒度（设为1可以最小切分为单字）
     */
    @ApiModelProperty(notes = "索引模式下的最小切分颗粒度", value = "索引模式下的最小切分颗粒度", example = "2")
    private int enableIndexModeNums = 2;
    /**
     * 开启词性标注
     */
    @ApiModelProperty(notes = "开启词性标注", value = "开启词性标注", example = "false")
    private boolean enablePartOfSpeechTagging;
    /**
     * 开启人名识别
     */
    @ApiModelProperty(notes = "开启人名识别", value = "开启人名识别", example = "false")
    private boolean enableNameRecognize;
    /**
     * 开启地名识别
     */
    @ApiModelProperty(notes = "开启地名识别", value = "开启地名识别", example = "false")
    private boolean enablePlaceRecognize;
    /**
     * 开启机构名识别
     */
    @ApiModelProperty(notes = "开启机构名识别", value = "开启机构名识别", example = "false")
    private boolean enableOrganizationRecognize;
    /**
     * 是否启用用户词典
     */
    @ApiModelProperty(notes = "是否启用用户词典", value = "是否启用用户词典", example = "false")
    private boolean enableCustomDictionary;
    /**
     * 是否尽可能强制使用用户词典（使用户词典的优先级尽可能高）
     * 警告：具体实现由各子类决定，可能会破坏分词器的统计特性（例如，如果用户词典
     * 含有“和服”，则“商品和服务”的分词结果可能会被用户词典的高优先级影响）。
     */
    @ApiModelProperty(
        notes = "是否尽可能强制使用用户词典（使用户词典的优先级尽可能高）警告：具体实现由各子类决定，可能会破坏分词器的统" +
        "计特性（例如，如果用户词典含有“和服”，则“商品和服务”的分词结果可能会被用户词典的高优先级影响）",
        value = "是否尽可能强制使用用户词典（使用户词典的优先级尽可能高）警告：具体实现由各子类决定，可能会破坏分词器的统" +
            "计特性（例如，如果用户词典含有“和服”，则“商品和服务”的分词结果可能会被用户词典的高优先级影响）",
        example = "false")
    private boolean enableCustomDictionaryForcing;
    /**
     * 是否启用音译人名识别
     */
    @ApiModelProperty(notes = "是否启用音译人名识别", value = "是否启用音译人名识别", example = "false")
    private boolean enableTranslatedNameRecognize;
    /**
     * 是否启用日本人名识别
     */
    @ApiModelProperty(notes = "是否启用日本人名识别", value = "是否启用日本人名识别", example = "false")
    private boolean enableJapaneseNameRecognize;
    /**
     * 是否启用偏移量计算（开启后Term.offset才会被计算）
     */
    @ApiModelProperty(notes = "是否启用偏移量计算", value = "是否启用偏移量计算", example = "false")
    private boolean enableOffset;
    /**
     * 是否启用数词和数量词识别<br>
     * 即[二, 十, 一] => [二十一]，[十, 九, 元] => [十九元]
     */
    @ApiModelProperty(
        notes = "是否启用数词和数量词识别<br>\n" +
        "     * 即[二, 十, 一] => [二十一]，[十, 九, 元] => [十九元]",
        value = "是否启用数词和数量词识别<br>\n" +
        "     * 即[二, 十, 一] => [二十一]，[十, 九, 元] => [十九元]",
        example = "false")
    private boolean enableNumberQuantifierRecognize;
    /**
     * 是否启用所有的命名实体识别
     */
    @ApiModelProperty(notes = "是否启用所有的命名实体识别", value = "是否启用所有的命名实体识别", example = "false")
    private boolean enableAllNamedEntityRecognize;

    public SegmentConfigVo() {
    }

    public SegmentConfigVo(boolean enableIndexMode, int enableIndexModeNums, boolean enablePartOfSpeechTagging, boolean enableNameRecognize, boolean enablePlaceRecognize, boolean enableOrganizationRecognize, boolean enableCustomDictionary, boolean enableCustomDictionaryForcing, boolean enableTranslatedNameRecognize, boolean enableJapaneseNameRecognize, boolean enableOffset, boolean enableNumberQuantifierRecognize, boolean enableAllNamedEntityRecognize) {
        this.enableIndexMode = enableIndexMode;
        this.enableIndexModeNums = enableIndexModeNums;
        this.enablePartOfSpeechTagging = enablePartOfSpeechTagging;
        this.enableNameRecognize = enableNameRecognize;
        this.enablePlaceRecognize = enablePlaceRecognize;
        this.enableOrganizationRecognize = enableOrganizationRecognize;
        this.enableCustomDictionary = enableCustomDictionary;
        this.enableCustomDictionaryForcing = enableCustomDictionaryForcing;
        this.enableTranslatedNameRecognize = enableTranslatedNameRecognize;
        this.enableJapaneseNameRecognize = enableJapaneseNameRecognize;
        this.enableOffset = enableOffset;
        this.enableNumberQuantifierRecognize = enableNumberQuantifierRecognize;
        this.enableAllNamedEntityRecognize = enableAllNamedEntityRecognize;
    }

    public boolean isEnableIndexMode() {
        return enableIndexMode;
    }

    public void setEnableIndexMode(boolean enableIndexMode) {
        this.enableIndexMode = enableIndexMode;
    }

    public int getEnableIndexModeNums() {
        return enableIndexModeNums;
    }

    public void setEnableIndexModeNums(int enableIndexModeNums) {
        this.enableIndexModeNums = enableIndexModeNums;
    }

    public boolean isEnablePartOfSpeechTagging() {
        return enablePartOfSpeechTagging;
    }

    public void setEnablePartOfSpeechTagging(boolean enablePartOfSpeechTagging) {
        this.enablePartOfSpeechTagging = enablePartOfSpeechTagging;
    }

    public boolean isEnableNameRecognize() {
        return enableNameRecognize;
    }

    public void setEnableNameRecognize(boolean enableNameRecognize) {
        this.enableNameRecognize = enableNameRecognize;
    }

    public boolean isEnablePlaceRecognize() {
        return enablePlaceRecognize;
    }

    public void setEnablePlaceRecognize(boolean enablePlaceRecognize) {
        this.enablePlaceRecognize = enablePlaceRecognize;
    }

    public boolean isEnableOrganizationRecognize() {
        return enableOrganizationRecognize;
    }

    public void setEnableOrganizationRecognize(boolean enableOrganizationRecognize) {
        this.enableOrganizationRecognize = enableOrganizationRecognize;
    }

    public boolean isEnableCustomDictionary() {
        return enableCustomDictionary;
    }

    public void setEnableCustomDictionary(boolean enableCustomDictionary) {
        this.enableCustomDictionary = enableCustomDictionary;
    }

    public boolean isEnableCustomDictionaryForcing() {
        return enableCustomDictionaryForcing;
    }

    public void setEnableCustomDictionaryForcing(boolean enableCustomDictionaryForcing) {
        this.enableCustomDictionaryForcing = enableCustomDictionaryForcing;
    }

    public boolean isEnableTranslatedNameRecognize() {
        return enableTranslatedNameRecognize;
    }

    public void setEnableTranslatedNameRecognize(boolean enableTranslatedNameRecognize) {
        this.enableTranslatedNameRecognize = enableTranslatedNameRecognize;
    }

    public boolean isEnableJapaneseNameRecognize() {
        return enableJapaneseNameRecognize;
    }

    public void setEnableJapaneseNameRecognize(boolean enableJapaneseNameRecognize) {
        this.enableJapaneseNameRecognize = enableJapaneseNameRecognize;
    }

    public boolean isEnableOffset() {
        return enableOffset;
    }

    public void setEnableOffset(boolean enableOffset) {
        this.enableOffset = enableOffset;
    }

    public boolean isEnableNumberQuantifierRecognize() {
        return enableNumberQuantifierRecognize;
    }

    public void setEnableNumberQuantifierRecognize(boolean enableNumberQuantifierRecognize) {
        this.enableNumberQuantifierRecognize = enableNumberQuantifierRecognize;
    }

    public boolean isEnableAllNamedEntityRecognize() {
        return enableAllNamedEntityRecognize;
    }

    public void setEnableAllNamedEntityRecognize(boolean enableAllNamedEntityRecognize) {
        this.enableAllNamedEntityRecognize = enableAllNamedEntityRecognize;
    }
}
