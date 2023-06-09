package it.uniroma3.siw.model;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "users") // cambiamo nome perchè in postgres user e' una parola riservata
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String surname;

	private String email;
	@OneToMany(mappedBy = "owner",cascade = CascadeType.REMOVE)
	private List<Review> reviews;

	@MapKeyColumn(name="review_key")
	@OneToMany(cascade = CascadeType.REMOVE)
	//devo verificare che se all'utente è gia associata una recensione a quello specifico film ,
	//allora non devo permettere l'aggiunta recensione. 
	private Map<String, Review> review;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, Review> getReview() {
		return review;
	}

	public void setReview(Map<String, Review> reviews) {
		this.review = reviews;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}