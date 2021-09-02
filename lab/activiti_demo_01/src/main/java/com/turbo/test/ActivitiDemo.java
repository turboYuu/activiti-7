package com.turbo.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

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
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4.输出部署信息
        System.out.println("流程部署id="+deploy.getId());
        System.out.println("流程部署名字="+deploy.getName());
    }


    /**
     * 启动流程实例
     *
     * act_hi_actinst 流程实例执行历史
     * act_hi_identitylink 流程的参与用户历史信息
     * act_hi_procinst 流程实例历史信息
     * act_hi_taskinst 流程任务历史信息
     * act_ru_execution 流程执行信息
     * act_ru_identitylink 流程的参与用户信息
     * act_ru_task 任务信息
     */
    @Test
    public void testStartProcess(){
        // 1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        // 4.输出内容
        System.out.println("流程定义id:"+instance.getProcessDefinitionId());
        System.out.println("流程实例id:"+instance.getId());
        System.out.println("当前活动的id:"+instance.getActivityId());
    }

    /**
     * 查询个人待执行的任务
     */
    @Test
    public void testFindPersonTaskList(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3.根据流程的key 和任务的负责人 查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") // 要查询的负责人
                .list();
        // 4.输出
        for (Task task:taskList){
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());

        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取service
        TaskService taskService = processEngine.getTaskService();

        // 根据任务id完成任务 完成zhangsan的任务
        //taskService.complete("2505");

        // 根据流程的key 和任务的负责人 jerry myEvection 查询任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") // 要查询的负责人
                .singleResult();
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection") // 流程key
//                .taskAssignee("jack") // 要查询的负责人
//                .singleResult();
//        Task task = taskService.createTaskQuery()
//                .processDefinitionKey("myEvection") // 流程key
//                .taskAssignee("rose") // 要查询的负责人
//                .singleResult();
        System.out.println("流程实例id："+task.getProcessInstanceId());
        System.out.println("任务id："+task.getId());
        System.out.println("任务负责人："+task.getAssignee());
        System.out.println("任务名称："+task.getName());
        // 完成jerry的任务
        taskService.complete(task.getId());

    }

    /**
     * 使用zip包及逆行批量部署
     */
    @Test
    public void deployProcessByZip(){
        // 1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.流程部署
        // 读取资源包文件，构造成inputstream
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/evection.zip");
        // 用inputStream 构造ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 使用压缩包的流 进行部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id："+deploy.getId());
        System.out.println("流程部署名称："+deploy.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        // 1.获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取 repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.获取processDefinitionQuery
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 4.查询当前所有的流程定义 返回流程定义信息的集合
        // processDefinitionKey(流程定义的key)
        // orderByProcessDefinitionVersion() 进行排序
        // desc() 倒序
        // list() 查询出所有内容
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        // 5.输出
        for (ProcessDefinition processDefinition : definitionList) {

            System.out.println("流程定义id:"+processDefinition.getId());
            System.out.println("流程定义名称："+processDefinition.getName());
            System.out.println("流程定义key:"+processDefinition.getKey());
            System.out.println("流程定义版本："+processDefinition.getVersion());
            System.out.println("流程部署id:"+processDefinition.getDeploymentId());
        }
    }

    /**
     * 删除流程部署信息
     * act_ge_bytearray
     * act_re_deployment
     * act_re_procdef
     * 删除流程定义时不会删除历史信息
     *
     * 当前的流程并没有完成，想要删除的话要使用特殊的方式，原理就是 级联删除
     */
    @Test
    public void deleteDeployment(){
        // 1.获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取 repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.通过部署id删除流程部署信息
        String deploymentId = "2501";
//        repositoryService.deleteDeployment(deploymentId);
        repositoryService.deleteDeployment(deploymentId,true);
    }


    /**
     * 下载资源文件
     * 方案1：使用Activiti提供的API下载资源文件，保存到文件目录 IO
     * 方案2：自己写代码从数据库中下载文件，使用jdbc对blob类型，clob类型数据读取出来，保存到文件目录
     *
     *      解决IO操作：commons-io.jar
     *
     * 这里使用方案1，RespositoryService
     */
    @Test
    public void getDeployment() throws IOException {
        // 1.获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取 repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.获取查询对象 processDefinitionQuery ，查询流程定义
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4.通过流程定义信息，获取部署id
        String deploymentId = processDefinition.getDeploymentId();
        // 5.通过repositoryService，传递部署id，读取资源信息（png 和 bpmn 文件）
            // 5.1 获取png图片的流
            // 从流程定义表中，获取png图片的目录和名字，通过部署id和文件名字来获取图片资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());

            // 5.2 获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 6.资源文件写到文件目录 构造 OutputStream
        File pngFile = new File("e:/evectionflow01.png");
        File bpmnFile = new File("e:/evectionflow01.bpmn");

        FileOutputStream pngOutputStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        // 7.输入流 ，输出流的转换
        IOUtils.copy(pngInput,pngOutputStream);
        IOUtils.copy(bpmnInput,bpmnOutputStream);
        // 8.关闭流
        pngOutputStream.close();
        bpmnOutputStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
        // 1.获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取actinst表的查询对象
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 查询actinst 条件根据instanceId
        //historicActivityInstanceQuery.processInstanceId("10001");
        // 查询actinst 条件根据DefinitionId
        historicActivityInstanceQuery.processDefinitionId("myEvection:1:7504");
        // 增加排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> activityInstanceList = historicActivityInstanceQuery.list();

        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("=============================");

        }
    }


}
