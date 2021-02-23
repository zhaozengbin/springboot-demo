package com.zzb.monitor.report.text.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.system.SystemUtil;
import com.zzb.core.utils.SpringContextUtils;
import com.zzb.monitor.core.entity.ExceptionInfoEntity;

import java.util.List;

public class MarkdownUtils {
    private static final Log LOG = Log.get(MarkdownUtils.class);

    public static String exceptionMarkdown(String exceptionName, int threshold, List<ExceptionInfoEntity> exceptionInfoEntityList) {
        String title = String.format("%s", exceptionName);
        SimpleMarkdownBuilder simpleMarkdownBuilder = SimpleMarkdownBuilder.create().title(title, 1);
        simpleMarkdownBuilder.title("项目名：" + SpringContextUtils.getApplicationContext().getId(), 2);
        simpleMarkdownBuilder.title("机器IP：" + SystemUtil.getHostInfo().getAddress(), 2);
        simpleMarkdownBuilder.title("报警阈值：" + threshold, 2);
        simpleMarkdownBuilder.title("异常方法：", 2);
        exceptionInfoEntityList.forEach(exceptionInfoEntity -> {
            simpleMarkdownBuilder
                    .title("方法名：" + SimpleMarkdownBuilder.bold(exceptionInfoEntity.getMethodName()), 3)
                    .title("异常次数：" + exceptionInfoEntity.getExceptionCount(), 3)
                    .title("出现时间：" + DateUtil.format(DateUtil.date(exceptionInfoEntity.getTime()), "yyyy-MM-dd HH:mm:ss"), 3)
                    .title("方法类路径：", 3).text(exceptionInfoEntity.getClassName(), true)
                    .title("参数信息：", 3).orderPoint(exceptionInfoEntity.getParams())
                    .title("追踪信息：", 3).orderPoint(exceptionInfoEntity.getStackTraceElementSet());
            if (CollUtil.isNotEmpty(exceptionInfoEntity.getMethodNodeList())) {
                simpleMarkdownBuilder.title("链路： ", 3).orderPoint(exceptionInfoEntity.getMethodNodeList());
            }
        });
        String markdown = simpleMarkdownBuilder.build();
        LOG.info(markdown);
        return markdown;
    }
}
