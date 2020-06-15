package com.springboot.jasperreports.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.springboot.jasperreports.model.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	final Faker faker = new Faker();
	final Random random = new Random();

	// For ease we are not making the database interaction here. 
	// Readers can inject the dao layer here to make the real-time database interactions.
	@Override
	public List<Employee> findAll() {
		final List<Employee> employees = new ArrayList<>();
		// Creating a list of employees using the "faker" object.
		for(int count=0; count<1; count++) {
			employees.add(new Employee(random.nextInt(30 + 1), faker.name().fullName(), 
					faker.job().title(), faker.job().field()));
		}
		return employees;
	}
}
