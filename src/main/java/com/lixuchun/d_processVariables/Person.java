package com.lixuchun.d_processVariables;

import java.io.Serializable;

public class Person implements Serializable {
	
	private static final long serialVersionUID = -4171458937905567634L;

	private Integer id;
	
	private String name;

	private String age;
	
	public Person() {
		super();
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + "]";
	}
}
