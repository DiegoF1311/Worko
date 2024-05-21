package co.edu.unbosque.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import co.edu.unbosque.model.Routine;


public class RoutineService {
	
	public static List<Routine> routinesByTrainings(String json) {
		Gson gson = new Gson();
		try {
			JsonElement jsonElement = JsonParser.parseString(json);

			if (jsonElement.isJsonArray()) {
				Routine[] routines = gson.fromJson(json, Routine[].class);
				return new ArrayList<>(Arrays.asList(routines));
			} else if (jsonElement.isJsonObject()) {
				Routine routine = gson.fromJson(json, Routine.class);
				List<Routine> routines = new ArrayList<>();
				routines.add(routine);
				return routines;
			} else {
				System.out.println("El JSON proporcionado no es ni un objeto ni un arreglo válido.");
				return new ArrayList<>();
			}
		} catch (JsonSyntaxException e) {
			System.out.println("Error al parsear el JSON " + e);
			return new ArrayList<>();
		}
	}
	
	public static List<Routine> routines(String json){
		Gson g = new Gson();
		Type routineListType = new TypeToken<List<Routine>>(){}.getType();
		List<Routine> routines = g.fromJson(json, routineListType);
		return routines;
	}
	
}
