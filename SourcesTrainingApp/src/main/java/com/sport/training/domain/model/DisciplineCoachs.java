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
 * A registration represents the event that an athlete registered. This registration has one
 * registration event and is relevant for one athlete.
 *
 * @see User
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_DISCIPLINE_COACHS") 
public class DisciplineCoachs implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISCIPLINE_FK")
	@NotNull(message = "invalid Discipline")
	private Discipline discipline;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_FK", nullable = false)
    @NotNull(message = "invalid Customer")
    private User athlete;

    // ======================================
    // =            Constructors            =
    // ======================================
    public DisciplineCoachs() {;
    }

    public DisciplineCoachs(final Discipline discipline, final User athlete) {
    	setDiscipline(discipline);
        setAthlete(athlete);
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
    public Long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}
	
    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(final Discipline discipline) {
    	this.discipline = discipline;
    }

    public User getAthlete() {
        return athlete;
    }

    public void setAthlete(final User athlete) {
    	this.athlete = athlete;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("DisciplineCoachs{");
        buf.append("id=").append(getId());
        buf.append(",disciplineID=").append(getDiscipline());
        buf.append(",athleteID=").append(getAthlete().getUsername());
        buf.append('}');
        return buf.toString();
    }
}
