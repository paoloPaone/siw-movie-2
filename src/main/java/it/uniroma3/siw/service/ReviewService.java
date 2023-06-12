package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private MovieService movieService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;


	@Transactional
	public Review getReviewById(Long id) {
		return this.reviewRepository.findById(id).orElse(null);
	}

	@Transactional
	public void addReview(Review review) {
		this.reviewRepository.save(review);
	}

	@Transactional
	public void updateReview(Review review) {
		this.reviewRepository.save(review);
	}	

	@Transactional
	public void deleteReview(Long id) {
		Review review = this.getReviewById(id);
		Movie movie = review.getMovie();
		movie.getReviews().remove(review);
		this.movieService.updateMovie(movie);
		User owner = review.getOwner();
		owner.getReview().remove(movie.getTitle());
		owner.getReviews().remove(review);
		this.userService.updateUser(owner);
		this.reviewRepository.deleteById(id);
	}

	@Transactional
	public void updateReview(Review review, Movie movie) {
		// Imposta il film nella recensione
		review.setMovie(movie);
		// Imposta la data corrente alla recensione
		review.setDate(LocalDateTime.now());
		// Salva la recensione nel database
		this.addReview(review);
		// Aggiungi la recensione al film
		movie.getReviews().add(review);	
	}



	@Transactional
	public List<Review> getReviewsByMovieId(Long movieId) {
		return this.reviewRepository.getReviewsByMovieId(movieId);
	}

	@Transactional
	public void deleteReviewsByMovieId(Long movieId) {
		this.reviewRepository.deleteReviewsByMovieId(movieId);

	}

	@Transactional
	public User setProprietario( Review review) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		review.setOwner(user);
		return user;
	}
	
	@Transactional
	public void deleteReviewById(Long ReviewId) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		Review review = this.getReviewById(ReviewId);
		review.getMovie().getReviews().remove(review);

		if (review.getOwner().equals(user)) {
			this.deleteReview(ReviewId);
		}
	}
	

}
