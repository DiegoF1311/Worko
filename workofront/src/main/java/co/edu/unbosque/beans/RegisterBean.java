package co.edu.unbosque.beans;

import java.util.Calendar;
import java.util.Date;

import org.primefaces.PrimeFaces;

import co.edu.unbosque.controller.HttpClientSynchronous;
import co.edu.unbosque.model.Login;
import co.edu.unbosque.model.User;
import co.edu.unbosque.util.EmailSender;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("RegisterBean")
@RequestScoped
public class RegisterBean {
	private static String firstname = "";
	private static String lastname = "";
	private static String username = "";
	private static String email = "";
	private static String genre = "";
	private static Integer age;
	private static Integer height;
	private static Integer weight;
	private static Date date;
	private static String password;
	private static String passwordconfirm;
	
	public RegisterBean() {
		// TODO Auto-generated constructor stub
	}
	
	public String createUser() {
		if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || genre.isEmpty()) {
			showStickyReg("406", "Debes llenar todos los datos del formulario!!");
			PrimeFaces.current().executeScript("alert('Debes llenar todos los datos del formulario!!');");
			return "";
		}
		EmailSender.sendEmail(email, username);
		PrimeFaces.current().executeScript("alert('Datos de usuario registrados!!');");
		showStickyReg("201", "Datos de usuario registrados!!");
		return "password.xhtml?faces-redirect=true";
	}
	
	public String signIn() {
		if (password.isEmpty() || passwordconfirm.isEmpty()) {
			PrimeFaces.current().executeScript("alert('Llena los campos!');");
			return "";			
		}
		if (password.equals(passwordconfirm)) {
			String name = firstname + " " + lastname;
			User u = new User(null, username, name, email, age, genre, height, weight, Calendar.getInstance().getTime());
			System.out.println(u.getRegistrationdate());
			Login l = new Login(null, password);
			String response = HttpClientSynchronous.doPostUser(u, l);
			System.out.println(response);
			PrimeFaces.current().executeScript("alert('Usuario Registrado con exito!!');");
			showStickyReg("201", "Usuario Registrado");
			return "login.xhtml?faces-redirect=true";
		}
		PrimeFaces.current().executeScript("alert('No coinciden!!');");
		showStickyReg("406", "No coinciden!!");
		return "";
	}
	
	public void showStickyReg(String code, String content) {
		if (code.equals("201")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Hecho", content));
		} else if (code.equals("406")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", content));
		}
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		RegisterBean.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		RegisterBean.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		RegisterBean.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		RegisterBean.email = email;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		RegisterBean.genre = genre;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		RegisterBean.age = age;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		RegisterBean.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		RegisterBean.weight = weight;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		RegisterBean.date = date;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		RegisterBean.password = password;
	}

	public String getPasswordconfirm() {
		return passwordconfirm;
	}

	public void setPasswordconfirm(String passwordconfirm) {
		RegisterBean.passwordconfirm = passwordconfirm;
	}
}
