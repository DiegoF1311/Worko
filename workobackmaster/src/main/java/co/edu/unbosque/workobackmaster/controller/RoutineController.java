package co.edu.unbosque.workobackmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.workobackmaster.model.Routine;
import co.edu.unbosque.workobackmaster.model.Training;
import co.edu.unbosque.workobackmaster.service.RoutineService;
import co.edu.unbosque.workobackmaster.service.SequenceService;
import co.edu.unbosque.workobackmaster.service.TrainingService;
import co.edu.unbosque.workobackmaster.service.UserService;


@RestController
@RequestMapping(name = "/routines")
@Transactional
@CrossOrigin(origins = {"*"})
public class RoutineController {
	
	@Autowired
	public RoutineService routineService;
	@Autowired
	public TrainingService trainingService;
	@Autowired
	public UserService userService;
	@Autowired
	public SequenceService sequenceService;
	
	@PostMapping("/createRoutine")
	public void create(@RequestParam String name, @RequestParam String description, @RequestParam String difficulty) {
		Routine aux = new Routine(sequenceService.getNextValue("seqroutine"), name, description, difficulty);
		routineService.create(aux);
	}
	
	@GetMapping("/getAllRoutines")
	public ResponseEntity<List<Routine>> getAll() {
		List<Routine> aux = routineService.getAll();
		if (aux.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(aux);
	}
	
	@GetMapping("/getCurrentId")
	public ResponseEntity<Long> getCurrentId() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(sequenceService.getCurrentValue("seqroutine"));
	}
	
	@GetMapping("/getByTrainings")
	public ResponseEntity<List<Routine>> getByTrainings(@RequestParam String idusr) {
		List<Training> aux = trainingService.getTrainingsByUser(userService.findById(idusr).getIdusr());
		if (aux.isEmpty()) {			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		Long[] ids = new Long[aux.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = aux.get(i).getIdroutine();
		}
		List<Routine> list = routineService.getByTrainings(ids);
		if (list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
}
