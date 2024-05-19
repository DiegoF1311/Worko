package co.edu.unbosque.workobackmaster.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trainings")
public class Training {
	
	@Id
	private String id;
	@Indexed(unique = true)
	private Long idtraining;
	@Indexed(unique = true)
	private Long iduser;
	@Indexed(unique = true)
	private Long idroutine;
	private Date date;
	private Integer duration;
	
	public Training() {
		// TODO Auto-generated constructor stub
	}

	public Training(Long idtraining, Long iduser, Long idroutine, Date date, Integer duration) {
		this.idtraining = idtraining;
		this.iduser = iduser;
		this.idroutine = idroutine;
		this.date = date;
		this.duration = duration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdtraining() {
		return idtraining;
	}

	public void setIdtraining(Long idtraining) {
		this.idtraining = idtraining;
	}

	public Long getIduser() {
		return iduser;
	}

	public void setIduser(Long iduser) {
		this.iduser = iduser;
	}

	public Long getIdroutine() {
		return idroutine;
	}

	public void setIdroutine(Long idroutine) {
		this.idroutine = idroutine;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Training [id=" + id + ", idtraining=" + idtraining + ", iduser=" + iduser + ", idroutine="
				+ idroutine + ", date=" + date + ", duration=" + duration + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
}
