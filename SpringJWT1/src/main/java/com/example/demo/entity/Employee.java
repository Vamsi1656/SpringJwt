package com.example.demo.entity;

import java.util.Set;

import jakarta.annotation.Generated;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="employee")

public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String username;
	
	private String password;
	
	
	
	@ElementCollection
	@CollectionTable(
			name="employeesroles",
			joinColumns = @JoinColumn(name="id")
			)
	
	@Column(name="role")
	private Set<String> roles;

}
