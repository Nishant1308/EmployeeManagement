package com.employee.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.manage.dto.EmployeeDto;
import com.employee.manage.service.EmployeeService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService service;
	
	@PostMapping("/save")
	public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto dto){
		EmployeeDto saved = service.createEmployee(dto);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long id){
		EmployeeDto getEmp = service.getEmployeeById(id);
		return new ResponseEntity<>(getEmp, HttpStatus.OK);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<EmployeeDto>> getAllEmployee(){
		List<EmployeeDto> all = service.getAllEmployee();
		return ResponseEntity.ok(all);
	}
	
	@PutMapping("/update")
	public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto dto){
		EmployeeDto employee = service.updateEmployee(dto);
		return new ResponseEntity<>(employee, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id){
		service.deleteEmployee(id);
		return ResponseEntity.ok("Employee Deleted.");
	}
}