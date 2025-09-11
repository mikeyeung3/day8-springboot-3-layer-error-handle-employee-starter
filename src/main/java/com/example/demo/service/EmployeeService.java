package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvaildSalaryForEmployeeAgeGreaterThan29Exception;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidOperationOnInactiveEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).stream().toList();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeeByGender(gender);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeeByGender(gender, pageable).stream().toList();
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Optional<Employee> employeeById = employeeRepository.findById(id);
        if (employeeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeById.get();
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("Employee's age cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Employee's age must be between 18 and 65");
        }
        if (employee.getAge() > 29 && employee.getSalary() < 20000) {
            throw new InvaildSalaryForEmployeeAgeGreaterThan29Exception("Salary must be greater than 20000 for employees older than 29");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Optional<Employee> found = employeeRepository.findById(id);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (found.get().getActive() != null && !found.get().getActive()) {
            throw new InvalidOperationOnInactiveEmployeeException(String.format("Employee %s is not active", found.get().getName()));
        }
        updatedEmployee.setId(id);
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee employeeById = getEmployeeById(id);
        employeeById.setActive(false);
        employeeRepository.save(employeeById);
    }
}