package com.turbo.test;

import com.turbo.demo.pojo.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestVariables1 {

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
                .name("出差申请流程-variables1")
                .addClasspathResource("bpmn/evection-global.bpmn")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }

    @Test
    public void testStartProcess(){
        // 1.流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 流程定义的key
        String key = "myEvection2";
        // 流程变量
        Evection evection = new Evection();
        evection.setNum(4d);
        // 流程变量的map
        Map<String,Object> variables = new HashMap<>();
        // 设定任务负责人
        variables.put("assignee0","张三1");
        variables.put("assignee1","李经理1");
        variables.put("assignee2","王总经理1");
        variables.put("assignee3","赵财务1");
        // 把流程变量的pojo放入map
        variables.put("evection",evection);


        // 启动流程实例
        runtimeService.startProcessInstanceByKey(key,variables);
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask(){

        //任务id
        String key = "myEvection2";
        // 任务负责人
        String assingee = "王总经理1";
        //获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        // 完成任务前，需要校验该负责人可以完成当前任务
        // 校验方法：
        // 根据任务id和任务负责人查询当前任务，如果查到该用户有权限，就完成
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assingee)
                .singleResult();

        if(task != null){
            taskService.complete(task.getId());
            System.out.println("任务执行完成");
        }
    }
}
