package com.zzb.monitor.chain.core.point;

import cn.hutool.log.Log;
import com.zzb.monitor.chain.core.around.util.AroundMethodUtil;
import com.zzb.monitor.chain.db.entity.MethodNode;
import com.zzb.monitor.chain.db.service.MethodNodeService;
import com.zzb.monitor.chain.util.ApplicationContextHelper;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类名称：BuriedPoint
 * 类描述：埋点处调用的前后方法
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:15 下午
 * 修改备注：TODO
 */
public class BuriedPoint {

    private final static Log LOG = Log.get(BuriedPoint.class);
    //自定义线程栈：ThreadId+Stack
    private static final Map<String, Stack<MethodNode>> map = new ConcurrentHashMap<>();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 方法：before
     * 描述：调用前置方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param pjp      :
     * @param identify :
     * @return : void
     * @date: 2021年02月18日 5:15 下午
     */
    public static void before(ProceedingJoinPoint pjp, int identify) {

        long threadId = Thread.currentThread().getId();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                MethodNode methodNode = AroundMethodUtil.getMethodNode(pjp, identify, threadId);
                pushMap(methodNode);
            }
        });
    }

    /**
     * 方法：after
     * 描述：调用后置方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param identify   :
     * @param returnType :
     * @return : void
     * @date: 2021年02月18日 5:15 下午
     */
    public static void after(int identify, String returnType) {
        long threadId = Thread.currentThread().getId();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                popMap(identify, threadId, returnType);
            }
        });
    }

    /**
     * 方法：pushMap
     * 描述：push进（线程）栈
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param methodNode :
     * @return : void
     * @date: 2021年02月18日 5:15 下午
     */
    private static void pushMap(MethodNode methodNode) {
        Stack<MethodNode> stack = map.get(String.valueOf(methodNode.getThreadId()));
        if (stack == null) {
            stack = new Stack<>();
        }
        stack.push(methodNode);
        map.put(String.valueOf(methodNode.getThreadId()), stack);
    }

    /**
     * 方法：popMap
     * 描述： pop出（线程）栈
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param identify   :
     * @param threadId   :
     * @param returnType :
     * @return : void
     * @date: 2021年02月18日 5:16 下午
     */
    private static void popMap(int identify, long threadId, String returnType) {

        Stack<MethodNode> stack = map.get(String.valueOf(threadId));
        if (stack == null || stack.isEmpty()) {
            return;
        }
        MethodNode methodNode = stack.pop();
        while (methodNode.getIdentify() != identify) {
            if (stack.isEmpty()) {
                return;
            }
            methodNode = stack.pop();
        }
        methodNode.setEndTime(new Date());
        methodNode.setReturnType(returnType);
        setMembership(stack, methodNode);
    }

    /**
     * 方法：setMembership
     * 描述：利用栈实现建立节点的父子关系
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param stack      :
     * @param methodNode :
     * @return : void
     * @date: 2021年02月18日 5:16 下午
     */
    private static void setMembership(Stack<MethodNode> stack, MethodNode methodNode) {
        if (!stack.isEmpty()) {
            MethodNode node = stack.pop();
            List<MethodNode> list = node.getMethodNodes();
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(methodNode);
            node.setMethodNodes(list);
            stack.push(node);
        } else {
            methodNode.setFirst(true);
        }
        save(methodNode);
    }

    /**
     * 方法：save
     * 描述：把节点保存到数据库
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param methodNode :
     * @return : void
     * @date: 2021年02月18日 5:16 下午
     */
    private static void save(MethodNode methodNode) {
        try {
            MethodNodeService methodNodeService = ApplicationContextHelper.popBean(MethodNodeService.class);
            Optional.ofNullable(methodNodeService).map(s -> s.saveNotRedo(methodNode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
