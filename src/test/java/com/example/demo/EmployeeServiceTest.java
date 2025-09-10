package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvaildSalaryForEmployeeAgeGreaterThan29Exception;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidOperationOnInactiveEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void should_throw_exception_when_create_a_employee() {
        Employee employee = new Employee(null, "John Smith", 20, "MALE", 60000.0);

        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(), employee.getAge());
    }

    @Test
    void should_throw_exception_when_create_employee_of_greater_than_65_or_less_than_18_years_old() {
        Employee employee = new Employee(null, "John Smith", 16, "MALE", 60000.0);

        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_throw_exception_when_create_employee_with_age_greater_than_29_and_salary_less_than_20000() {
        Employee employee = new Employee(null, "John Smith", 30, "MALE", 2000.0);
        assertThrows(InvaildSalaryForEmployeeAgeGreaterThan29Exception.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    void should_set_employee_active_status_to_true_when_create_employee() {
        Employee employee = new Employee(null, "John Smith", 20, "MALE", 60000.0);

        when(employeeRepository.createEmployee(any(Employee.class))).thenReturn(employee);

        assertEquals(true, employee.getActive());
    }

    @Test
    void should_set_employee_active_status_to_false_when_delete_employee() {
        Employee employee = new Employee(1, "John Smith", 20, "MALE", 60000.0);
        assertTrue(employee.getActive());
        when(employeeRepository.getEmployeeById(1)).thenReturn(employee);
        employeeService.deleteEmployee(1);

        verify(employeeRepository).updateEmployee(eq(1), argThat(e -> e.getActive() == false));
    }


    @Test
    void should_throw_exception_when_update_employee_who_is_not_active() {
        Employee employee = new Employee(1, "John Smith", 20, "MALE", 60000.0);
        employee.setActive(false);

        when(employeeRepository.getEmployeeById(1)).thenReturn(employee);

        assertThrows(InvalidOperationOnInactiveEmployeeException.class, () -> employeeService.updateEmployee(1, employee));
    }
}
