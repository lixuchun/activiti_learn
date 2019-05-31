package activiti_01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActiviti {
	/**
	 * ʹ�ô��봴����������Ҫ��23���ű�
	 * ���в���������������processEngin
	 */
	@Test
	public void creativeTable () {
		
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_learn?characterEncoding=utf8&useSSL=true");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("101022");
		
		/**
		 * public static final String DB_SCHEMA_UPDATE_FALSE = "false"; // �����Զ������� ��Ҫ�����
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop"; // ��ɾ���ٴ���
		 * public static final String DB_SCHEMA_UPDATE_TRUE = "true"; // ��������� �Զ�������
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		// �������ĺ��Ķ��� �����������
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);
	}
	
	/**
	 * ʹ�������ļ������������洴������
	 */
	@Test
	public void creativeTable2 () {
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		// �������ĺ��Ķ��� �����������
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);
	}
	
}
