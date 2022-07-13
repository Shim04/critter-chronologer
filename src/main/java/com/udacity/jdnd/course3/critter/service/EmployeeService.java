package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.exception.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()) {
            optionalEmployee.get().setDaysAvailable(daysAvailable);
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    public List<Employee> findEmployeesForService(LocalDate localDate, Set<EmployeeSkill> skills) {
        List<Employee> employeesWithSkills = new ArrayList<>();
        List<Employee> employeesAvailable = employeeRepository.findEmployeeByDaysAvailable(localDate.getDayOfWeek());
        employeesAvailable.forEach(employee -> {
            boolean containsSkills = employee.getSkills().containsAll(skills);
            if(containsSkills) {
                employeesWithSkills.add(employee);
            }
        });
        return employeesWithSkills;
    }
}
