package com.turbo.demo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 监听器
 */
public class MyTaskListener implements TaskListener {
    /**
     * 指定负责人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {
        // 判断当前的任务是创建申请，并且事件名字：create
        if("创建申请".equals(delegateTask.getName()) &&
            "create".equals(delegateTask.getEventName())){
            delegateTask.setAssignee("张三");
        }



    }
}
