package com.zzb.monitor.report.text.markdown;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.system.SystemUtil;
import com.zzb.core.utils.SpringContextUtils;
import com.zzb.monitor.chain.db.entity.MethodNode;
import com.zzb.monitor.core.entity.ExceptionInfoEntity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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
            simpleMarkdownBuilder.horizon().nextLine();
            simpleMarkdownBuilder
                    .title("方法名：" + SimpleMarkdownBuilder.bold(exceptionInfoEntity.getMethodName()), 3)
                    .title("异常次数：" + exceptionInfoEntity.getExceptionCount(), 3)
                    .title("出现时间：" + DateUtil.format(DateUtil.date(exceptionInfoEntity.getTime()), "yyyy-MM-dd HH:mm:ss"), 3)
                    .title("方法类路径：", 3).text(exceptionInfoEntity.getClassName(), true)
                    .title("参数信息：", 3).orderPoint(exceptionInfoEntity.getParams())
                    .title("追踪信息：", 3).orderPoint(exceptionInfoEntity.getStackTraceElementSet());
            if (CollUtil.isNotEmpty(exceptionInfoEntity.getMethodNodeList())) {
                simpleMarkdownBuilder.title("链路： ", 3).
                        text(getNodeList(CollUtil.newArrayList(exceptionInfoEntity.getMethodNodeList().iterator())), true);
            }
            simpleMarkdownBuilder.horizon().nextLine();
        });
        String markdown = simpleMarkdownBuilder.build();
        LOG.info(markdown);
        return markdown;
    }

    private static String getNodeList(List<MethodNode> methodNodes, String... tag) {
        StringBuilder stringBuilder = new StringBuilder();
        String tempTag = ">" + (ArrayUtil.isEmpty(tag) ? "" : tag[0] + "\t");
        stringBuilder.append(tempTag);
        int i = 0;
        methodNodes.forEach(value -> {
            stringBuilder.append(value.getFullName());
            stringBuilder.append("[");
            stringBuilder.append(value.getEndTime().getTime() - value.getStartTime().getTime());
            stringBuilder.append("ms]");
            stringBuilder.append("\n");
            if (CollUtil.isNotEmpty(value.getMethodNodes())) {
                stringBuilder.append(getNodeList(value.getMethodNodes(), tempTag));
            }
            stringBuilder.append("\n\t");
        });
        return stringBuilder.toString();
    }
}
