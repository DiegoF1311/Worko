package co.edu.unbosque.model;

public class TrainingRoutine {
	private String routinename;
	private String routinedes;
	private String routinediff;
	private String date;
	private String duration;
	
	public TrainingRoutine() {
		// TODO Auto-generated constructor stub
	}

	public TrainingRoutine(String routinename, String routinedes, String routinediff, String date, String duration) {
		super();
		this.routinename = routinename;
		this.routinedes = routinedes;
		this.routinediff = routinediff;
		this.date = date;
		this.duration = duration;
	}

	public String getRoutinename() {
		return routinename;
	}

	public void setRoutinename(String routinename) {
		this.routinename = routinename;
	}

	public String getRoutinedes() {
		return routinedes;
	}

	public void setRoutinedes(String routinedes) {
		this.routinedes = routinedes;
	}

	public String getRoutinediff() {
		return routinediff;
	}

	public void setRoutinediff(String routinediff) {
		this.routinediff = routinediff;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	
}
