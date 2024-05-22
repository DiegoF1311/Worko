package co.edu.unbosque.beans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.Timer;

import org.primefaces.PrimeFaces;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

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
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

@Named("UserBean")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username = "";
	private String password = "";
	private static String iduser = "";
	private int itemscarousel;
	private static List<TrainingRoutine> list = new ArrayList<>();

	private static List<Routine> routines;
	private static List<Exercise> exercises;
	private static List<Exercise> allexercises;
	private static List<ExerciseRoutine> exercisesRoutines;
	private static Routine selectedRoutine;
	private static Exercise selectedExercise;
	private int currentIndex;
	private Timer time;
	private int timeLapse = 0;
	private static int minutes = 0;
	private static int seconds = 0;
	private  String sec;
	private  String min;
	private User usrloged = new User();

	public UserBean() {
		// TODO Auto-generated constructor stub
	}

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
			loadUserProfile();
			return "home.xhtml?faces-redirect=true";
		}
	}
	
	public String updateRoutines() {
		String json = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getAllRoutines");
		routines = RoutineService.routines(json);
		return "availableroutines.xhtml?faces-redirect=true";
	}
	
	private void loadUserProfile() {
		String parameters = "getUserById?id=" + iduser;
		usrloged = HttpClientSynchronous.userById("http://localhost:8085/execute/get?path=" + urlEncode(parameters));
		System.out.println("Datos del usuario: " + usrloged);
	}

	public List<Training> trainings() {
		String parameters = "getTrainingsByUser?iduser="+iduser;
		return HttpClientSynchronous.trainingsByUser("http://localhost:8085/execute/get?path="+urlEncode(parameters));
	}

	private Cell createHeaderCell(String content) {
		return new Cell().add(new Paragraph(content).setBold()).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
	}

	private Cell createCell(String content) {
		return new Cell().add(new Paragraph(content)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
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
	
	public void startTimer() {
		timeLapse = 0;
		System.out.println("Timer iniciado");
		time = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timeLapse += 1000;
				minutes = ( timeLapse / 60000 ) % 60;
				seconds = ( timeLapse / 1000) % 60;
				sec = String.format("%02d",seconds);
				min = String.format("%02d",minutes);
			}
		});
		time.start();
	}

	public void stopTimer() {
		System.out.println("Timer detenido");
		time.stop();
		System.out.println(seconds);
		System.out.println(minutes);
	}


	public String create() {
		String parameters = "getUserById?id="+iduser;
		usrloged = HttpClientSynchronous.userById("http://localhost:8085/execute/get?path="+urlEncode(parameters));
		Training t = new Training(usrloged.getIdusr(), selectedRoutine.getIdroutine(), Calendar.getInstance().getTime(), minutes);
		String response = HttpClientSynchronous.doPost("execute/createTraining", t);
		System.out.println("Crear training "+response);
		PrimeFaces.current().executeScript("alert('Entrenamiento completado con exito, tiempo registrado: " + min + ":" + sec + "');");
		stopTimer();
		trainingsroutines();
		min = "00";
		sec = "00";
		return "home.xhtml?faces-redirect=true";
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
		return "Informacion no disponible";
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
	public List<Exercise> getAllExercises() {
		if (allexercises == null) {
			String json = HttpClientSynchronous.doGetAndParse("http://localhost:8085/execute/get?path=getAllExercises");
			allexercises = ExerciseService.exercises(json);
		}
		return allexercises;
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
	
	public String getSec() {
		return sec;
	}


	public void setSec(String sec) {
		this.sec = sec;
	}


	public String getMin() {
		return min;
	}


	public void setMin(String min) {
		this.min = min;
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
	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		UserBean.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		UserBean.seconds = seconds;
	}

	public void setItemscarousel(int itemscarousel) {
		this.itemscarousel = itemscarousel;
	}
	
	public void generatePdf() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		
		document.add(new Paragraph("Informe de entrenamientos").setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph(usrloged.getName()).setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph("Estos son los entrenamientos que has realizado")).setTextAlignment(TextAlignment.CENTER);
		
		float[] columnWidths = {1, 2, 4, 3, 2};
		Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
		
		table.addHeaderCell(createHeaderCell("ID ENTRENAMIENTO"));
		table.addHeaderCell(createHeaderCell("ID RUTINA"));
		table.addHeaderCell(createHeaderCell("NOMBRE DE LA RUTINA"));
		table.addHeaderCell(createHeaderCell("FECHA"));
		table.addHeaderCell(createHeaderCell("DURACI�N"));
		
		int id = 1;
		List<Routine> routines = getRoutines(); 
		
		for (Training training : trainings()) {
			table.addCell(createCell(String.valueOf(id++)));
			table.addCell(createCell(training.getIdroutine() + ""));
			
			routines = getRoutines();
			String routineName = "Nombre no disponible";
			for (Routine r : routines) {
				if (r.getIdroutine().equals(training.getIdroutine())) {
					routineName = r.getName();
					break;
				}
			}
			
			table.addCell(createCell(routineName));
			table.addCell(createCell(String.valueOf(training.getDate())));
			table.addCell(createCell(String.valueOf(training.getDuration())));
		}
		
		document.add(table);
		document.close();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.reset();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=informeEntrenamientos.pdf");
		response.getOutputStream().write(baos.toByteArray());
		response.getOutputStream().flush();
		
		facesContext.responseComplete();
	}
	
	public void generateExercisesPdf() throws IOException {
		System.out.println("Entro generar informe ejercicios");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);
		
		document.add(new Paragraph("Informe de Ejercicios").setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph(usrloged.getName()).setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph("Estos son todos los ejercicios con los que cuenta nuestra app")).setTextAlignment(TextAlignment.CENTER);
		
		float[] columnWidths = {1, 2, 4, 3, 2};
		Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
		
		table.addHeaderCell(createHeaderCell("ID EJERCICIO"));
		table.addHeaderCell(createHeaderCell("NOMBRE"));
		table.addHeaderCell(createHeaderCell("DIFICULTAD"));
		table.addHeaderCell(createHeaderCell("ENFOQUE"));
		table.addHeaderCell(createHeaderCell("EQUIPAMIENTO"));
		
		allexercises = getAllExercises();
		if (allexercises == null || allexercises.isEmpty()) {
			document.add(new Paragraph("No hay ejercicios disponibles.").setTextAlignment(TextAlignment.CENTER));
		} else {
			System.out.println(allexercises);
			for (Exercise exercise : allexercises) {
				table.addCell(createCell(String.valueOf(exercise.getIdexercise())));
				table.addCell(createCell(exercise.getName()));
				table.addCell(createCell(exercise.getDifficulty()));
				table.addCell(createCell(exercise.getFocus()));
				table.addCell(createCell(exercise.getEquipment()));
			}
			document.add(table);
		}
		document.close();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.reset();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=informeEjerjecicios.pdf");
		response.getOutputStream().write(baos.toByteArray());
		response.getOutputStream().flush();
		
		facesContext.responseComplete();
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
}
