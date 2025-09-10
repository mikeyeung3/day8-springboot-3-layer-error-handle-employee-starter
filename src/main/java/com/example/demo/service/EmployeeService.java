package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvaildSalaryForEmployeeAgeGreaterThan29;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidOperationOnInactiveEmployee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employeeById = employeeRepository.getEmployeeById(id);
        if (employeeById == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeById;
    }

    public Employee createEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidAgeEmployeeException("Employee cannot be null");
        }
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("Employee's age must be between 18 and 65");
        }
        if (employee.getAge() > 29 && employee.getSalary() < 20000) {
            throw new InvaildSalaryForEmployeeAgeGreaterThan29("Employee's salary must be at least 20000 for age greater than 29");
        }
        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = employeeRepository.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        if (found.getActive() != null && !found.getActive()) {
            throw new InvalidOperationOnInactiveEmployee(String.format("Employee %s is not active", found.getName()));
        }
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }

    public void deleteEmployee(int id) {
        Employee found = employeeRepository.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeRepository.deleteEmployee(id);
    }

    public void empty() {
        employeeRepository.empty();
    }
}
