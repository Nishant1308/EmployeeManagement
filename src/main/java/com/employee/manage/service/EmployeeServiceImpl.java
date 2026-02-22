package com.employee.manage.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.manage.dto.EmployeeDto;
import com.employee.manage.entity.Employee;
import com.employee.manage.exception.ResourceNotFoundException;
import com.employee.manage.mapper.EmployeeMapper;
import com.employee.manage.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private EmployeeMapper mapper;

	@Override
	public EmployeeDto createEmployee(EmployeeDto empDto) {
		Employee employee = mapper.mapToEmplyee(empDto);
		Employee saveEmp = repository.save(employee);

		return mapper.mapToEmployeeDto(saveEmp);
	}

	@Override
	public EmployeeDto getEmployeeById(Long empId) {
		Employee employee = repository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id :" + empId));
		return mapper.mapToEmployeeDto(employee);
	}

	@Override
	public List<EmployeeDto> getAllEmployee() {
		List<Employee> employee = repository.findAll();
		return employee.stream().map((emp) -> mapper.mapToEmployeeDto(emp)).collect(Collectors.toList());
	}

	@Override
	public EmployeeDto updateEmployee(EmployeeDto empDto) {
		Employee employee = repository.findById(empDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id :" + empDto.getId()));

		employee.setFirstName(empDto.getFirstName());
		employee.setLastName(empDto.getLastName());
		employee.setEmail(empDto.getEmail());

		repository.save(employee);

		return mapper.mapToEmployeeDto(employee);
	}

	@Override
	public void deleteEmployee(Long empId) {
		Employee employee = repository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with id :" + empId));
		
		repository.deleteById(empId);
		
	}

}
