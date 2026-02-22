package com.employee.manage.service;

import org.springframework.data.domain.Page;

import com.employee.manage.dto.EmployeeDto;

public interface EmployeeService {

	EmployeeDto createEmployee(EmployeeDto empDto);
	
	EmployeeDto getEmployeeById(Long empId);
	
	Page<EmployeeDto>  getAllEmployee(int page, int size);
	
	EmployeeDto updateEmployee(EmployeeDto empDto);
	
	void deleteEmployee(Long empId);
}
