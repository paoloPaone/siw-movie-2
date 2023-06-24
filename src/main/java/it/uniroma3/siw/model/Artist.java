package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "surname"}))
public class Artist {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String surname;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfBirth;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dateOfDeath;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Image picture;

	@OneToMany(mappedBy = "director",fetch = FetchType.EAGER)
	private Set<Movie> directedMovies;

	@ManyToMany(mappedBy = "actors",fetch = FetchType.EAGER)
	private Set<Movie> starredMovies;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Movie> getDirectedMovies() {
		return directedMovies;
	}

	public void setDirectedMovies(Set<Movie> filmDiretti) {
		this.directedMovies = filmDiretti;
	}

	public Set<Movie> getStarredMovies() {
		return starredMovies;
	}

	public void setStarredMovies(Set<Movie> filmRecitati) {
		this.starredMovies = filmRecitati;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, name, surname);
	}


	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Image getPicture() {
		return picture;
	}

	public void setPicture(Image picture) {
		this.picture = picture;
	}
	 
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist that = (Artist) obj;
		return ( this.getName().equals(that.getName()) && 
				this.getSurname().equals(that.getSurname()) );	
	}

}
