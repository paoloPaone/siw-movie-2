package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title", "year"}))
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String title;
	
	@OneToMany(cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
	private List<Image> images;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	private Image picture;


	@NotNull
	@Min(1900)
	@Max(2023)
	private Integer year;


	@ManyToOne
	private Artist director;

	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Artist> actors;


	@OneToMany(mappedBy = "movie",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<Review> reviews;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}



	public Artist getDirector() {
		return director;
	}

	public void setDirector(Artist director) {
		this.director = director;
	}

	public Set<Artist> getActors() {
		return actors;
	}

	public void setActors(Set<Artist> actors) {
		this.actors = actors;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, year);
	}

	public Image getPicture() {
		return picture;
	}

	public void setPicture(Image picture) {
		this.picture = picture;
	}

	@Override
	public boolean equals(Object obj) {
		Movie that = (Movie) obj;
		return Objects.equals(title, that.title) && Objects.equals(year, that.year);
	}


}
