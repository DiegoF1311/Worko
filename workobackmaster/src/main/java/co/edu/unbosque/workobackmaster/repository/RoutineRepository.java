package co.edu.unbosque.workobackmaster.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.unbosque.workobackmaster.model.Routine;
import java.util.List;



public interface RoutineRepository extends MongoRepository<Routine, String>{
	List<Routine> findByIdroutine(Long idroutine);
}
