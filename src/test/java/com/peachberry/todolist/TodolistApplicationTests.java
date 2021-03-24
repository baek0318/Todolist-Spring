package com.peachberry.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class TodolistApplicationTests {
	@Autowired
	DataSource dataSource;
	@Test
	void contextLoads() throws SQLException {
		System.out.println(dataSource.getConnection().getMetaData()+"");
	}

}
