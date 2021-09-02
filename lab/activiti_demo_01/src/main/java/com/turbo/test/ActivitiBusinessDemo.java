package com.turbo.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key 到Activiti的表
     */
    @Test
    public void addBusinessKey(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.启动流程的过程中添加BusinessKey
            // 第1个参数，流程定义的key，
            // 第2个参数：businessKey，存出差申请单的id 就是1001
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        // 4.内容输出
        System.out.println("businessKey="+instance.getBusinessKey());

    }
}
