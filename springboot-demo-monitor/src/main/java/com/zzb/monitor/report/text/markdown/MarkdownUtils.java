package com.zzb.monitor.report.text.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.zzb.core.utils.SpringContextUtils;
import com.zzb.monitor.core.entity.ExceptionInfoEntity;

import java.util.Set;

public class MarkdownUtils {
    private static final Log LOGGER = Log.get(MarkdownUtils.class);

    public static String exceptionMarkdown(String exceptionName, Set<ExceptionInfoEntity> exceptionInfoEntitySet) {
        String title = String.format("%s", exceptionName);
        SimpleMarkdownBuilder simpleMarkdownBuilder = SimpleMarkdownBuilder.create().title(title, 1);
        simpleMarkdownBuilder.title("项目名：", 2).text(SpringContextUtils.getApplicationContext().getId(), true);
        simpleMarkdownBuilder.title("机器IP：", 2).text(SystemUtil.getHostInfo().getAddress(), true);
        exceptionInfoEntitySet.forEach(exceptionInfoEntity -> {
            simpleMarkdownBuilder.title("路径：", 2).text(exceptionInfoEntity.getClassName(), true)
                    .title("方法名：" + SimpleMarkdownBuilder.bold(exceptionInfoEntity.getMethodName()), 2)
                    .title("参数信息：", 2).orderPoint(exceptionInfoEntity.getParams())
                    .title("异常信息：", 2).point(exceptionInfoEntity.getExceptionName())
                    .title("追踪信息：", 2).orderPoint(exceptionInfoEntity.getStackTraceElementList())
                    .title("出现时间：", 2).text(DateUtil.format(DateUtil.date(exceptionInfoEntity.getTime()), "yyyy-MM-dd HH:mm:ss"), true);
            if (CollUtil.isNotEmpty(exceptionInfoEntity.getMethodNodeList())) {
                simpleMarkdownBuilder.title("链路： ", 2).orderPoint(exceptionInfoEntity.getMethodNodeList());
            }
        });
        String markdown = simpleMarkdownBuilder.build();
        LOGGER.info(markdown);
        return markdown;
    }
}
