package co.edu.unbosque.workobackmaster.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.unbosque.workobackmaster.model.Login;


public interface LoginRepository extends MongoRepository<Login, String>{

}
