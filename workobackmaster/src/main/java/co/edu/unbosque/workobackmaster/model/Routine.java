package co.edu.unbosque.workobackmaster.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "routines")
public class Routine {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private Long id_routine;
	private String name;
	private String description;
	private String difficulty;
	
	public Routine() {
		// TODO Auto-generated constructor stub
	}

	public Routine(Long id_routine, String name, String description, String difficulty) {
		this.id_routine = id_routine;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getId_routine() {
		return id_routine;
	}

	public void setId_routine(Long id_routine) {
		this.id_routine = id_routine;
	}

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

	@Override
	public String toString() {
		return "Routine [id=" + id + ", id_routine=" + id_routine + ", name=" + name + ", description=" + description
				+ ", difficulty=" + difficulty + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
}
