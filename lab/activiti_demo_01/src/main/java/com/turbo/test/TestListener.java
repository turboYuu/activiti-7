package com.turbo.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestListener {

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
                .name("测试监听")
                .addClasspathResource("bpmn/demo-listen.bpmn")
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

        // 3.启动流程实例
        runtimeService.startProcessInstanceByKey("testListener");
    }
}
