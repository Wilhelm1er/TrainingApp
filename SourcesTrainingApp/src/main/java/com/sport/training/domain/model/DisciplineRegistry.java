package com.sport.training.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * A discipline register represents the discipline that a coach registered. This registration has one
 * registration event and is relevant for one coach.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_DISCIPLINE_REGISTRY")  
public class DisciplineRegistry implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="disc_seq_gen")
	private Long id;
	
	@Column(name = "REGISTER_DATE")
	private Date registerDate;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="COACH_FK", nullable = false)
    @NotNull(message = "invalid Athlete")
    private User coach;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DISCIPLINE_FK")
    @NotNull(message = "invalid Discipline")
    private Discipline discipline;

	// ======================================
    // =            Constructors            =
    // ======================================
    public DisciplineRegistry() {
    	this.registerDate=new Date();
    }

    public DisciplineRegistry(final Discipline discipline,final User coach) {
    	this.registerDate=new Date();
    	setRegisterDate(registerDate);
    	setDiscipline(discipline);
        setCoach(coach);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
    
    public void setCoach(User coach) {
    	this.coach = coach;
		
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
		
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
		
	}

	public User getCoach() {
		return coach;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	@Override
	public String toString() {
		return "DisciplineRegistry [id=" + id + ", registerDate=" + registerDate + ", coach=" + coach + ", discipline="
				+ discipline + "]";
	}
	
	
}
