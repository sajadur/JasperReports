package com.springboot.jasperreports.service;

import java.util.List;

import com.springboot.jasperreports.model.Employee;

public interface EmployeeService {

	List<Employee> findAll();
}
