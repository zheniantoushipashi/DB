package Serialization;

import java.util.Random;

public class Person {

	private String name;

	public Person(String name, int age, int number){ 
		this.name = name; 
		this.age = age; 
		this.Number = number; 
		} 
	
	public Person() {
		Random rabdom = new Random();
		name = randomGenerateName(rabdom);
		age = randomGenerateAge(rabdom);
		Number = randomGenerateNumber(rabdom);
	}

	private String randomGenerateName(Random rabdom) {
		int start = 65;
		StringBuilder name = new StringBuilder();
		int nameLength = rabdom.nextInt(15);
		if (nameLength < 2)
			nameLength = 2;
		for (int i = 0; i < nameLength; i++) {
			name.append((char) (start + rabdom.nextInt(26)));
		}
		return name.toString();
	}

	private int randomGenerateNumber(Random rabdom) {
		return rabdom.nextInt(10000);
	}

	/**
	 * 随机生成年龄(1~120岁)
	 * 
	 * @param rabdom
	 * @return
	 */
	private int randomGenerateAge(Random rabdom) {
		int age = rabdom.nextInt(120);
		return age == 0 ? 25 : age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	private int age;
	private int sex;
	private int Number;

	public int getNumber() {
		return Number;
	}

	public void setNumber(int number) {
		Number = number;
	}
}
