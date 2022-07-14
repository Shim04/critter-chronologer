package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {
    private PetRepository petRepository;
    private CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    // https://knowledge.udacity.com/questions/562817
    // update save method to sync customer and pet
    public Pet savePet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getCustomer();
        List<Pet> pets = customer.getPets();
        pets.add(savedPet);
        customer.setPets(pets);
        customerRepository.save(customer);
        return savedPet;
    }

    public Pet findPetById(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if(optionalPet.isPresent()) {
            return optionalPet.get();
        } else {
            throw new PetNotFoundException();
        }
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
