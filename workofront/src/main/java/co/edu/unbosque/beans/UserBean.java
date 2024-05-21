package co.edu.unbosque.beans;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.primefaces.PrimeFaces;

import co.edu.unbosque.controller.HttpClientSynchronous;
import co.edu.unbosque.model.Exercise;
import co.edu.unbosque.model.ExerciseRoutine;
import co.edu.unbosque.model.Routine;
import co.edu.unbosque.model.Training;
import co.edu.unbosque.model.TrainingRoutine;
import co.edu.unbosque.model.User;
import co.edu.unbosque.service.ExerciseRoutineService;
import co.edu.unbosque.service.ExerciseService;
import co.edu.unbosque.service.RoutineService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("UserBean")
@RequestScoped
public class UserBean {
	private String username = "";
	private String password = "";
	private static String iduser = "";
	private int itemscarousel;
	private static List<TrainingRoutine> list = new ArrayList<>();
	
	private static List<Routine> routines;
	private static List<Exercise> exercises;
	private static List<ExerciseRoutine> exercisesRoutines;
	private static Routine selectedRoutine;
	private static Exercise selectedExercise;
	private int currentIndex;
    private int seconds = 0;
    private Timer timer;
    private boolean running = false;
	
	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	
	private User usrloged = new User();
	
	public String login() {
		String parameters = "userlogin?username="+username+"&password="+password;
		String response = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path="+urlEncode(parameters));
		System.out.println(response);
		if (response.equals("Usuario no encontrado!") || response.equals("Credenciales erroneas!")) {
			showStickyLogin("406", "Error al iniciar sesion");
			return "";
		} else {
			showStickyLogin("201", "Inicio de sesion realizado con exito!");
			iduser = response;
			trainingsroutines();
			return "home.xhtml?faces-redirect=true";
		}
	}
	
	public List<Training> trainings() {
		String parameters = "getTrainingsByUser?iduser="+iduser;
		return HttpClientSynchronous.trainingsByUser("http://localhost:8085/execute/get?path="+urlEncode(parameters));
	}
	
	public List<Routine> routines() {
		String parameters = "getByTrainings?idusr="+iduser;
		return HttpClientSynchronous.routinesByTrainings("http://localhost:8085/execute/get?path="+urlEncode(parameters));
	}
	
	public void trainingsroutines() {
		List<Training> t = trainings();
		List<Routine> r = routines();
		List<TrainingRoutine> auxlist = new ArrayList<>();
		for (int i = 0; i < t.size(); i++) {
			auxlist.add(new TrainingRoutine(r.get(i).getName(), r.get(i).getDescription(), r.get(i).getDifficulty(), t.get(i).getDate().toString(), t.get(i).getDuration() + " Minutos"));
		}
		setList(auxlist);
		calcItems();
	}
	
	public void calcItems() {
		if (getList().size() < 3) {
			itemscarousel = list.size();
		} else {
			itemscarousel = 3;
		}
	}
	
	public void showStickyLogin(String code, String content) {
		if (code.equals("201")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", content));
		} else if (code.equals("406")) {
			FacesContext.getCurrentInstance().addMessage("sticky-key",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", content));
		}
	}
	
	public String create() {
		String parameters = "getUserById?id="+iduser;
		usrloged = HttpClientSynchronous.userById("http://localhost:8085/execute/get?path="+urlEncode(parameters));
		Training t = new Training(usrloged.getIdusr(), selectedRoutine.getIdroutine(), Calendar.getInstance().getTime(), seconds);
		String response = HttpClientSynchronous.doPost("execute/createTraining", t);
		System.out.println("Crear training "+response);
    	stopTimer();
    	System.out.println(seconds);
    	PrimeFaces.current().executeScript("alert('Entrenamiento guardado!!');");
    	trainingsroutines();
    	return "home.xhtml";
    }
    public int getSeconds() {
        return seconds;
    }

    public boolean isRunning() {
        return running;
    }

    public void startTimer() {
    	System.out.println("corriendo");
        if (timer != null) {
            timer.cancel();
        }
        seconds = 0;
        timer = new Timer();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        running = false;
    }
	public String chooseRoutine(Routine routine) {
		UserBean.selectedRoutine = routine;
		this.currentIndex = 0;
		UserBean.exercises = getExercises(routine.getIdroutine()+"");
		UserBean.exercisesRoutines = getExercisesRoutines();
		UserBean.selectedExercise = exercises.get(currentIndex);
		return "exercise.xhtml?faces-redirect=true";
	}
	
	public String getSetsAndReps() {
		if (exercisesRoutines != null && selectedExercise != null) {
			for (ExerciseRoutine er : exercisesRoutines) {
				if (er.getIdexercise() == selectedExercise.getIdexercise()) {
					return er.getSets() + " sets de " + er.getRepetitions() + " repeticiones";
				}
			}
		}
		return "Informaci�n no disponible";
	}

	public List<Routine> getRoutines() {
		if (routines == null) {
			String json = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getAllRoutines");
			routines = RoutineService.routines(json);
		}
		return routines;
	}
	public void setRoutines(List<Routine> routines) {
		UserBean.routines = routines;
	}
	public List<Exercise> getExercises(String id) {
	    if (exercises == null) {
	        String json = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getByRoutine%3Fidroutine%3D"+id);
	        exercises = ExerciseService.exercises(json);
	    }
	    return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		UserBean.exercises = exercises;
	}
	public List<ExerciseRoutine> getExercisesRoutines() {
		if (exercisesRoutines == null) {
			String json = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getAllExerciseRoutine");
			exercisesRoutines = ExerciseRoutineService.exercisesRoutines(json);
		}
		return exercisesRoutines;
	}
	
	public void setExercisesRoutines(List<ExerciseRoutine> exercisesRoutines) {
		UserBean.exercisesRoutines = exercisesRoutines;
	}

	public Routine getSelectedRoutine() {
		return selectedRoutine;
	}
	public void setSelectedRoutine(Routine selectedRoutine) {
		UserBean.selectedRoutine = selectedRoutine;
	}
	
	public Exercise getSelectedExercise() {
		return selectedExercise;
	}
	public void setSelectedExercise(Exercise selectedExercise) {
		UserBean.selectedExercise = selectedExercise;
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}
	
	private String urlEncode(String value) {
		return value == null ? "" : URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		UserBean.iduser = iduser;
	}

	public User getUsrloged() {
		return usrloged;
	}

	public void setUsrloged(User usrloged) {
		this.usrloged = usrloged;
	}
	
	public List<TrainingRoutine> getList() {
		return list;
	}

	public void setList(List<TrainingRoutine> list) {
		UserBean.list = list;
	}

	public int getItemscarousel() {
		return itemscarousel;
	}

	public void setItemscarousel(int itemscarousel) {
		this.itemscarousel = itemscarousel;
	}
	
}
