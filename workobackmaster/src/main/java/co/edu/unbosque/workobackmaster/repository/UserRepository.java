package co.edu.unbosque.workobackmaster.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.unbosque.workobackmaster.model.User;


public interface UserRepository extends MongoRepository<User, String>{

}
