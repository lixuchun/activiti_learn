package com.lixuchun.k_group2;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {

	/**
	 * ����ָ������İ����� �������
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		// ָ�������������
		delegateTask.addCandidateUser("����");
		delegateTask.addCandidateUser("����");
		delegateTask.addCandidateUser("���߹�");
		delegateTask.addCandidateUser("ŷ����");
	}

}
