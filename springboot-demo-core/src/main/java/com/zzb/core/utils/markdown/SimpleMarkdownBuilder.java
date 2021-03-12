package com.zzb.core.utils.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * 类名称：SimpleMarkdownBuilder
 * 类描述：组装markdown文档
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/6 3:02 下午
 * 修改备注：TODO
 *
 */
public class SimpleMarkdownBuilder {

    private StringBuilder stringBuilder = new StringBuilder();

    public static SimpleMarkdownBuilder create() {
        SimpleMarkdownBuilder markdownBuilder = new SimpleMarkdownBuilder();
        return markdownBuilder;
    }

    public SimpleMarkdownBuilder title(String content, int level) {
        if (level > 0 && level < 6) {
            for (int i = 0; i < level; i++) {
                stringBuilder.append("#");
            }
            stringBuilder.append(" ").append(content).append("\n\n");
        }
        return this;
    }

    public SimpleMarkdownBuilder text(String content, boolean lineFeed) {
        stringBuilder.append(content);
        if (lineFeed) {
            stringBuilder.append("\n\n");
        }
        return this;
    }

    public SimpleMarkdownBuilder point(List<?> contentList) {
        return orderPoint(contentList, false);
    }

    public SimpleMarkdownBuilder point(Object... contentList) {
        return orderPoint(contentList, false);
    }

    public SimpleMarkdownBuilder orderPoint(List<?> list) {
        return orderPoint(list, true);
    }

    public SimpleMarkdownBuilder orderPoint(LinkedHashSet<?> set) {
        return orderPoint(set, true);
    }

    public SimpleMarkdownBuilder orderPoint(Object... list) {
        return orderPoint(ArrayUtil.isNotEmpty(list) ? CollUtil.newArrayList(list) : null, true);
    }

    private SimpleMarkdownBuilder orderPoint(Collection<?> coll, boolean isOrder) {
        if (CollUtil.isNotEmpty(coll)) {
            int i = 0;
            coll.forEach(value -> {
                stringBuilder.append(isOrder ? (i + 1) + ". " : "- ").append(value).append("\n");
            });
            stringBuilder.append("\n");
        }
        return this;
    }

    public SimpleMarkdownBuilder code(String content, int level) {
        if (level > 0 && level < 4) {
            String str = "`````````".substring(0, level);
            if (level != 3) {
                stringBuilder.append(String.format("%s%s%s", str, content, str));
            } else {
                stringBuilder.append(String.format("%s\n%s\n%s\n", str, content, str));
            }
        }
        return this;
    }

    public SimpleMarkdownBuilder linked(String explain, String url) {
        stringBuilder.append("![").append(explain).append("](").append(url).append(")");
        return this;
    }

    public SimpleMarkdownBuilder keyValue(Map<?, ?> map, String keyName, String valueName, TableAlignment alignment) {
        if (map != null && map.size() > 0) {
            stringBuilder.append("|").append(keyName).append("|").append(valueName).append("|").append("\n");
            String value = alignment.getValue();
            stringBuilder.append(value).append("|").append(value).append("|").append("\n");
            map.forEach((x, y) -> stringBuilder.append("|").append(x).append("|").append(y).append("|").append("\n"));
            stringBuilder.append("\n\n");
        }
        return this;
    }

    public SimpleMarkdownBuilder nextLine() {
        stringBuilder.append("\n");
        return this;
    }

    public SimpleMarkdownBuilder horizon() {
        stringBuilder.append("***");
        return this;
    }

    public SimpleMarkdownBuilder at(List<String> ats) {
        if (CollUtil.isNotEmpty(ats)) {
            ats.forEach(value -> {
                stringBuilder.append("@").append(value);
            });
            stringBuilder.append("\n");
        }
        return this;
    }

    public String build() {
        return stringBuilder.toString();
    }

    public static String bold(String text) {
        return String.format("**%s**", text);
    }
}
