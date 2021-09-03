package com.turbo.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestAssignessUel {

    /**
     * 测试流程部署
     */
    @Test
    public void testDeployment(){
        // 1.创建 ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.使用service进行流程部署，定义一个流程的名字 ，把bpmn和png部署到数据库
        Deployment deploy = repositoryService.createDeployment()
                .name("出差申请流程-uel")
                .addClasspathResource("bpmn/evection-uel.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }

    @Test
    public void startAssigneeUel(){
        // 1.流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设定assignee的值，用来替换uel表达式
        Map<String,Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0","张三");
        assigneeMap.put("assignee1","李经理");
        assigneeMap.put("assignee2","王总经理");
        assigneeMap.put("assignee3","赵财务");
        // 3.启动流程实例
        runtimeService.startProcessInstanceByKey("myEvecvtion1",assigneeMap);
    }
}
