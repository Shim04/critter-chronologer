package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    private ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findSchedulesByPetId(Long id) {
        return scheduleRepository.findSchedulesByPetsId(id);
    }

    public List<Schedule> findSchedulesByEmployeeId(Long id) {
        return scheduleRepository.findSchedulesByEmployeesId(id);
    }

    public List<Schedule> findSchedulesByCustomer(Customer customer) {
        List<Pet> pets = customer.getPets();
        List<Schedule> res = new ArrayList<>();
        pets.forEach(pet -> {
            List<Schedule> curr = scheduleRepository.findSchedulesByPetsId(pet.getId());
            res.addAll(curr);
        });
        return res;
    }
}
