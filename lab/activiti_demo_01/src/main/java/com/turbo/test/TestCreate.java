package com.turbo.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

import javax.sound.midi.Soundbank;

public class TestCreate {

    /**
     * 生成activiti数据库表，
     */
    @Test
    public void createDbTable(){
        // 需要使用activiti提供的工具类,使用方法getDefaultProcessEngine
        // getDefaultProcessEngine会默认从resources下读取名字为activiti.cfg.xml的文件
        //创建processEngines时，就会创建表
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RepositoryService repositoryService = processEngine.getRepositoryService();

        //使用自定义方式
        // 配置文件名可以自定义,bean的名字也可以自定义
        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml");

        ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();

        System.out.println(processEngine);
    }
}
