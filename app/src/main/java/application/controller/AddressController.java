package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import application.model.Address;
import application.model.User;
import application.repository.AddressRepository;
import application.repository.UserRepository;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public Iterable<Address> getAll() {
        return addressRepo.findAll();
    }

    @GetMapping("/{id}")
    public Address getOne(@PathVariable Long id) {
        Optional<Address> result = addressRepo.findById(id);
        if(result.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Endereço Não Encontrado"
            );
        }
        return result.get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if(addressRepo.existsById(id)) {
            addressRepo.deleteById(id);
        } else {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Endereço Não Encontrado"
            );
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Address post(@RequestBody Address address) {
        if(!userRepo.existsById(address.getUser().getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Usuário vinculado não encontrado"
            );
        }
        return addressRepo.save(address);
    }

    @PutMapping("/{id}")
    public Address put(@RequestBody Address address, @PathVariable Long id) {
        Optional<Address> result = addressRepo.findById(id);
        if(result.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Endereço Não Encontrado"
            );
        }
        if(!userRepo.existsById(address.getUser().getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Usuário vinculado não encontrado"
            );
        }
        Address existingAddress = result.get();
        existingAddress.setStreet(address.getStreet());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        existingAddress.setPostalCode(address.getPostalCode());
        existingAddress.setUser(address.getUser());

        return addressRepo.save(existingAddress);
    }
}
