package com.zzb.monitor.chain.db.service.util;


import com.zzb.monitor.chain.db.entity.MethodNode;

import java.util.List;

/**
 * 类名称：ServiceUtil
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:20 下午
 * 修改备注：TODO
 */
public class ServiceUtil {

    /**
     * 方法：analyze
     * 描述：分析调用链是否相同 调用链情况相同返回True
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param node   :
     * @param nodeId :
     * @return : boolean
     * @date: 2021年02月18日 5:20 下午
     */
    public static boolean analyze(MethodNode node, MethodNode nodeId) {
        List<MethodNode> nodes = node.getMethodNodes();
        List<MethodNode> idNodes = nodeId.getMethodNodes();
        int nodeNum = nodes != null ? nodes.size() : 0;
        int idNodeNum = idNodes != null ? idNodes.size() : 0;
        String nodeStr = "";
        if (nodeNum > 0) {
            StringBuilder builder = new StringBuilder();
            nodes.forEach(s -> builder.append(s.getMethodId()));
            nodeStr = builder.toString();
        }
        String idNodeStr = "";
        if (idNodeNum > 0) {
            StringBuilder builder = new StringBuilder();
            idNodes.forEach(s -> builder.append(s.getMethodId()));
            idNodeStr = builder.toString();
        }
        return nodeStr.equals(idNodeStr);
    }

    public static boolean analyzeList(MethodNode node, List<MethodNode> nodeId) {
        for (MethodNode methodNode : nodeId) {
            if (analyze(node, methodNode)) {
                return true;
            }
        }
        return false;
    }

}
