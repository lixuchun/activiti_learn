package com.lixuchun.j_personalTask2;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {

	/**
	 * ����ָ������İ�����
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		// ָ����������İ����ˣ�Ҳ����ָ��������İ�����
		// ͨ����ȥ��ѯ���ݿ� ����һ������İ����˲�ѯ��ȡ �˺�ͨ�� setAssignee()�趨������
		delegateTask.setAssignee("���ʦ̫");
	}

}
