package co.edu.unbosque.beans;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import co.edu.unbosque.controller.HttpClientSynchronous;
import co.edu.unbosque.model.Exercise;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("ExerciseBean")
@RequestScoped
public class ExerciseBean {
	
	private List<Exercise> exercises = new ArrayList<>();
	
	public ExerciseBean() {
		
	}
	
	@PostConstruct
	public void init() {
		exercises = getAllExercises();
	}
	
	public List<Exercise> getAllExercises() {
		String parameters = "getAllExercises";
		return HttpClientSynchronous.getAllExercises("http://localhost:8085/execute/get?path="+urlEncode(parameters));
	}
	
	private String urlEncode(String value) {
		return value == null ? "" : URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	public List<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
	
}
