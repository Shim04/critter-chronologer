package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;
    private PetService petService;
    private EmployeeService employeeService;
    private CustomerService customerService;

    public ScheduleController(ScheduleService scheduleService, PetService petService, EmployeeService employeeService, CustomerService customerService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.employeeService = employeeService;
        this.customerService = customerService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToScheduleDTO(scheduleService.saveSchedule(convertScheduleDTOToSchedule(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertScheduleListToScheduleDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertScheduleListToScheduleDTOList(scheduleService.findSchedulesByPetId(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertScheduleListToScheduleDTOList(scheduleService.findSchedulesByEmployeeId(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        return convertScheduleListToScheduleDTOList(scheduleService.findSchedulesByCustomer(customer));
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> petIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();
        if(schedule.getPets() != null) {
            schedule.getPets().forEach(pet -> petIds.add(pet.getId()));
        }
        if(schedule.getEmployees() != null) {
            schedule.getEmployees().forEach(employee -> employeeIds.add(employee.getId()));
        }
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Pet> pets = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        if(scheduleDTO.getPetIds() != null) {
            scheduleDTO.getPetIds().forEach(petId -> pets.add(petService.findPetById(petId)));
        }
        if(scheduleDTO.getEmployeeIds() != null) {
            scheduleDTO.getEmployeeIds().forEach(employeeId -> employees.add(employeeService.findEmployeeById(employeeId)));
        }
        schedule.setPets(pets);
        schedule.setEmployees(employees);
        return schedule;
    }

    private List<ScheduleDTO> convertScheduleListToScheduleDTOList(List<Schedule> schedules) {
        List<ScheduleDTO> res = new ArrayList<>();
        schedules.forEach(schedule -> res.add(convertScheduleToScheduleDTO(schedule)));
        return res;
    }
}
