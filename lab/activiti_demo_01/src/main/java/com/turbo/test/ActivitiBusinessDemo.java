package com.turbo.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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


    /**
     * 全部流程实例的挂起 和 激活
     * suspend 暂停
     */
    @Test
    public void suspendAllProcessInstance(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.查询流程定义信息
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = definitionQuery.processDefinitionKey("myEvection").singleResult();
        // 4.获取当前流程定义的实例 是否都是挂起的状态，
        boolean suspended = processDefinition.isSuspended();

        // 5.获取流程定义id
        String definitionId = processDefinition.getId();
        // 6.如果是挂起状态，改为激活状态
        if(suspended){
            // 参数1：流程定义id,参数2：是否激活，参数3：激活的时间
            repositoryService.activateProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id："+definitionId+",已激活");
        }else{
            // 7.如果是激活状态，改为挂起状态 参数1：流程定义id,参数2：是否暂停，参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id:"+definitionId+",已挂起");
        }
    }

    /**
     * 挂起或激活单个流程实例
     *
     */
    @Test
    public void suspendSingleProcessInstance(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.通过RunTimeService获取流程实例对象
        ProcessInstance singleResult = runtimeService.createProcessInstanceQuery()
                .processInstanceId("15001")
                .singleResult();
        // 4.得到当前流程实例的暂停状态 true-已暂停 false-激活
        boolean suspended = singleResult.isSuspended();
        // 5.获取流程实例id
        String instanceId = singleResult.getId();

        if(suspended){
            // 6.判断是否已经暂停，如果是就激活
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例:"+instanceId+",已激活");
        }else{
            // 7.如果激活，就暂停
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例:"+instanceId+",已挂起");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.TaskService获取任务,参数 流程实例id,流程负责人
        Task task = taskService.createTaskQuery()
                .processInstanceId("15001")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println("流程实例id:"+task.getProcessInstanceId());
        System.out.println("流程任务id:"+task.getId());
        System.out.println("任务负责人:"+task.getAssignee());
        System.out.println("任务名称:"+task.getName());
        // 4.根据任务的id完成任务
        taskService.complete(task.getId());
    }
}
