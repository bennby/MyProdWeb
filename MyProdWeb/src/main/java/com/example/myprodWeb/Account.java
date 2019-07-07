package com.example.myprodWeb;

public class Account {
	private Long id;
	private String name;
	private Integer balance;
	private Integer count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Account(Long id, String name, Integer balance, Integer count) {
		super();
		this.id = id;
		this.name = name;
		this.balance = balance;
		this.count = count;
	}

	public Account(String name, Integer balance, Integer count) {
		super();
		this.name = name;
		this.balance = balance;
		this.count=count;
	}

	public Account() {
		super();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String toString() {
		return "[序号: " + id + ", 商品名称姓名: " + name + ", 价格: " + balance
				+ ", 数量: " + count + "]";
	}

}

