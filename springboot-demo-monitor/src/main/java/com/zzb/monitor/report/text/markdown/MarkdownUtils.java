package com.zzb.monitor.report.text.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.system.SystemUtil;
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
        simpleMarkdownBuilder.title("异常方法：", 2);
        exceptionInfoEntitySet.forEach(exceptionInfoEntity -> {
            simpleMarkdownBuilder
                    .title("方法名：" + SimpleMarkdownBuilder.bold(exceptionInfoEntity.getMethodName()), 3)
                    .title("路径：", 3).text(exceptionInfoEntity.getClassName(), true)
                    .title("参数信息：", 3).orderPoint(exceptionInfoEntity.getParams())
                    .title("追踪信息：", 3).orderPoint(exceptionInfoEntity.getStackTraceElementSet())
                    .title("出现时间：", 3).text(DateUtil.format(DateUtil.date(exceptionInfoEntity.getTime()), "yyyy-MM-dd HH:mm:ss"), true);
            if (CollUtil.isNotEmpty(exceptionInfoEntity.getMethodNodeList())) {
                simpleMarkdownBuilder.title("链路： ", 3).orderPoint(exceptionInfoEntity.getMethodNodeList());
            }
        });
        String markdown = simpleMarkdownBuilder.build();
        LOGGER.info(markdown);
        return markdown;
    }
}
