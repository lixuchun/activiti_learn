package com.lixuchun.k_group2;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {

	/**
	 * 用来指定任务的办理人 组任务的
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		// 指定组任务办理人
		delegateTask.addCandidateUser("郭靖");
		delegateTask.addCandidateUser("黄蓉");
		delegateTask.addCandidateUser("洪七公");
		delegateTask.addCandidateUser("欧阳锋");
	}

}
