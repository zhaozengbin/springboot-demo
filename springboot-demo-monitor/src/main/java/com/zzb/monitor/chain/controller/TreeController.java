package com.zzb.monitor.chain.controller;

import com.zzb.monitor.chain.db.entity.MethodNode;
import com.zzb.monitor.chain.db.service.MethodNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Create by yster@foxmail.com 2019/1/31 0031 19:51
 */
@RestController
public class TreeController {

    @Autowired
    private MethodNodeService methodNodeService;

    private Logger logger = LoggerFactory.getLogger(TreeController.class);

    /**
     * 返回HTML网页形式的分析结果
     *
     * @return
     */
    @RequestMapping(value = "/projecttree", method = RequestMethod.GET)
    @ResponseBody
    public List<MethodNode> getViewAll() {
        List<MethodNode> nodes = methodNodeService.findAll();
        if (nodes != null) {
            nodes = nodes.stream().filter(MethodNode::isFirst).collect(Collectors.toList());
        }
        return nodes;
    }

    @RequestMapping(value = "/projecttree/all", method = RequestMethod.GET)
    @ResponseBody
    public List<MethodNode> getView() {
        return methodNodeService.findAll();
    }

    @RequestMapping("/projecttree/{methodId}")
    @ResponseBody
    public MethodNode getByMehtodId(@PathVariable Long methodId) {
        return methodNodeService.findAllById(methodId);
    }

    @RequestMapping("/projecttree/name/{mehtodName}")
    @ResponseBody
    public List<MethodNode> getByMehtodName(@PathVariable String mehtodName) {
        return methodNodeService.findAllByMethodName(mehtodName);
    }

    @RequestMapping("/projecttree/fullName/{fulleName}")
    @ResponseBody
    public List<MethodNode> getByMehtodFullName(@PathVariable String fulleName) {
        return methodNodeService.findAllByFullName(fulleName);
    }
}
