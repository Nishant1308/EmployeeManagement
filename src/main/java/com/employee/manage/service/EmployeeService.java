package com.employee.manage.service;

import java.util.List;

import com.employee.manage.dto.EmployeeDto;

public interface EmployeeService {

	EmployeeDto createEmployee(EmployeeDto empDto);
	
	EmployeeDto getEmployeeById(Long empId);
	
	List<EmployeeDto>  getAllEmployee();
	
	EmployeeDto updateEmployee(EmployeeDto empDto);
	
	void deleteEmployee(Long empId);
}
