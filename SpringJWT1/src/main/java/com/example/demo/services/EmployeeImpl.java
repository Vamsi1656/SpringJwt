package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepo;

@Service
public class EmployeeImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepo repo;

	@Override
	public Integer saveEmployee(Employee emp) {
		return repo.save(emp).getId();
	
	}

}
