package com.zzb.monitor.core.entity;

import com.zzb.monitor.chain.db.entity.MethodNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

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

    private int exceptionCount;

    private List<MethodNode> methodNodeList;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExceptionInfoEntity that = (ExceptionInfoEntity) o;
        return Objects.equals(exceptionName, that.exceptionName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionName, className, methodName);
    }
}
