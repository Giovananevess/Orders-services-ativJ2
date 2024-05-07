package application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import application.model.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
