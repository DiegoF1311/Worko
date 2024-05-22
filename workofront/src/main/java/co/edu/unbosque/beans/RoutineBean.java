package co.edu.unbosque.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.PrimeFaces;

import co.edu.unbosque.controller.HttpClientSynchronous;
import co.edu.unbosque.model.ExerciseRoutine;
import co.edu.unbosque.model.Routine;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named("RoutineBean")
@RequestScoped
public class RoutineBean {
	private String name = "";
    private String description = "";
    private String difficulty = "";
    private static List<Long> selectedExercises = new ArrayList<>();
    private static Map<Long, Integer> setsMap = new HashMap<>();
    private static Map<Long, Integer> repsMap = new HashMap<>();


    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public List<Long> getSelectedExercises() {
        return selectedExercises;
    }

    public Map<Long, Integer> getSetsMap() {
        return setsMap;
    }

    public void setSetsMap(Map<Long, Integer> setsMap) {
        RoutineBean.setsMap = setsMap;
    }

    public Map<Long, Integer> getRepsMap() {
        return repsMap;
    }

    public void setRepsMap(Map<Long, Integer> repsMap) {
        RoutineBean.repsMap = repsMap;
    }

    public void confirm(Long exerciseId) {
        Integer sets = Integer.parseInt(setsMap.get(exerciseId)+"");
        Integer reps = Integer.parseInt(repsMap.get(exerciseId)+"");

        if (sets != null && reps != null) {
            System.out.println("Exercise ID: " + exerciseId + ", Sets: " + sets + ", Reps: " + reps);
            if (!selectedExercises.contains(exerciseId)) {
                selectedExercises.add(exerciseId);
            }
        } else {
            System.out.println("Sets or Reps not provided for Exercise ID: " + exerciseId);
        }
    }
    
    public String createRoutine() {
    	Routine r = new Routine(null, name, description, difficulty);
    	String response = HttpClientSynchronous.doPost("execute/createRoutine", r);
    	System.out.println("R: "+response);
    	createExerciseRoutine();
    	PrimeFaces.current().executeScript("alert('Rutina creada!!');");
    	selectedExercises = new ArrayList<>();
    	setsMap = new HashMap<>();
    	repsMap = new HashMap<>();
    	return "home.xhtml";
    }
    
    public void createExerciseRoutine() {
    	String idrout = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getCurrentId");
    	for (int i = 0; i < selectedExercises.size(); i++) {	
    		Long ex = selectedExercises.get(i);
    		ExerciseRoutine er = new ExerciseRoutine(Long.parseLong(idrout), ex, Integer.parseInt(setsMap.get(ex)+""), Integer.parseInt(repsMap.get(ex)+""));
    		String response = HttpClientSynchronous.doPost("execute/createExerciseRoutine", er);
    		System.out.println("ER: "+response);
		}
    }
}
