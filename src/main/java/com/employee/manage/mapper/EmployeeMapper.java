package com.employee.manage.mapper;

import org.springframework.stereotype.Component;

import com.employee.manage.dto.EmployeeDto;
import com.employee.manage.entity.Employee;

@Component
public class EmployeeMapper {

	public static EmployeeDto mapToEmployeeDto(Employee emp) {
		EmployeeDto empDto = new EmployeeDto();
		empDto.setId(emp.getId());
		empDto.setFirstName(emp.getFirstName());
		empDto.setLastName(emp.getLastName());
		empDto.setEmail(emp.getEmail());

		return empDto;
	}

	public static Employee mapToEmplyee(EmployeeDto dto) {
		Employee emp = new Employee();
		emp.setId(dto.getId());
		emp.setFirstName(dto.getFirstName());
		emp.setLastName(dto.getLastName());
		emp.setEmail(dto.getEmail());

		return emp;
	}
}
