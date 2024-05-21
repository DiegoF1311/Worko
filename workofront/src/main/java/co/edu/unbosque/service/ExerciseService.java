package co.edu.unbosque.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import co.edu.unbosque.model.Exercise;
public class ExerciseService {
	
	public static List<Exercise> getAll(String json) {
		Gson gson = new Gson();
		try {
			JsonElement jsonElement = JsonParser.parseString(json);

			if (jsonElement.isJsonArray()) {
				Exercise[] exercises = gson.fromJson(json, Exercise[].class);
				return new ArrayList<>(Arrays.asList(exercises));
			} else if (jsonElement.isJsonObject()) {
				Exercise exercise = gson.fromJson(json, Exercise.class);
				List<Exercise> exercises = new ArrayList<>();
				exercises.add(exercise);
				return exercises;
			} else {
				System.out.println("El JSON proporcionado no es ni un objeto ni un arreglo válido.");
				return new ArrayList<>();
			}
		} catch (JsonSyntaxException e) {
			System.out.println("Error al parsear el JSON " + e);
			return new ArrayList<>();
		}
	}
	
	public static List<Exercise> exercises(String json) {
	    Gson gson = new Gson();
	    List<Exercise> exerciseList = new ArrayList<>();
	    
	    try {
	        Exercise[] exerciseArray = gson.fromJson(json, Exercise[].class);
	        exerciseList = Arrays.asList(exerciseArray);
	    } catch (JsonSyntaxException e) {
	        try {
	            Exercise exercise = gson.fromJson(json, Exercise.class);
	            exerciseList.add(exercise);
	        } catch (JsonSyntaxException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return exerciseList;
	}

}
