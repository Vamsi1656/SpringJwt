package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeRequest;
import com.example.demo.entity.EmployeeResponse;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.services.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService empservice;
	
	@Autowired
	private JwtUtil util;
	
	// save user data in Database
	@PostMapping("/employee")
	
	public ResponseEntity<String> saveEmployee(@RequestBody Employee emp)
	{
		
		Integer id = empservice.saveEmployee(emp);
		String body="Employee '"+id+"' saved";
//		return new ResponseEntity<String>(body,HttpStatus.OK);
		return ResponseEntity.ok(body);
	
	}
	
	// Validate user and generate token(login)
	@PostMapping("/login")
	public ResponseEntity<EmployeeResponse> loginemp(
		@RequestBody EmployeeRequest request){
		String token=util.generateToken(request.getUsername());
		return ResponseEntity.ok(new EmployeeResponse(token,"Success"));
	}
	
	
}
