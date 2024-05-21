package co.edu.unbosque.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import co.edu.unbosque.model.Training;

public class TrainingService {
	
	public static List<Training> trainingsByUser(String json) {
		Gson gson = new Gson();
		try {
			JsonElement jsonElement = JsonParser.parseString(json);

			if (jsonElement.isJsonArray()) {
				Training[] trainingsArray = gson.fromJson(json, Training[].class);
				return new ArrayList<>(Arrays.asList(trainingsArray));
			} else if (jsonElement.isJsonObject()) {
				Training training = gson.fromJson(json, Training.class);
				List<Training> trainingList = new ArrayList<>();
				trainingList.add(training);
				return trainingList;
			} else {
				System.out.println("El JSON proporcionado no es ni un objeto ni un arreglo válido.");
				return new ArrayList<>();
			}
		} catch (JsonSyntaxException e) {
			System.out.println("Error al parsear el JSON " + e);
			return new ArrayList<>();
		}
	}
	
}
