package com.zzb.monitor.core.entity;

import com.zzb.monitor.chain.db.entity.MethodNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionInfoEntity {
    private String guid;

    private String exceptionName;

    private LinkedHashSet<StackTraceElement> stackTraceElementSet;

    private long time;

    private String className;

    private String methodName;

    private Object[] params;

    private List<MethodNode> methodNodeList;

}
