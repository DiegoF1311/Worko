package co.edu.unbosque.workobackmaster.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.unbosque.workobackmaster.model.Training;


public interface TrainingRepository extends MongoRepository<Training, String>{
	
}
