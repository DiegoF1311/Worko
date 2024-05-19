package co.edu.unbosque.workobackmaster.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.unbosque.workobackmaster.model.ExerciseRoutine;


public interface ExerciseRoutineRepository extends MongoRepository<ExerciseRoutine, String>{

}
