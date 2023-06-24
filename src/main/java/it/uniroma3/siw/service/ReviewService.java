package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    	owner.getReviews().remove(review);
    	this.userService.updateUser(owner);
    	this.reviewRepository.deleteById(id);
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
	public Review updateReview(Long id, Integer rating, String text, String title) {
		Review review = this.getReviewById(id);
		review.setDate(LocalDateTime.now());
		review.setRating(rating);
		review.setText(text);
		review.setTitle(title);
		this.updateReview(review);
		return review;
	}

  

    @Transactional
	public Integer getNumeroRecensioni(Movie movie) {
		return this.reviewRepository.countByMovie(movie);
	}

	public boolean existsByMovieIdAndUserId(Long movieId, Long userId) {
		return this.reviewRepository.existsByMovieIdAndOwnerId(movieId,userId);
		 
	}

	public Review findByMovieIdAndOwnerId(Long movieId, Long userId) {
		return this.reviewRepository.findByMovieIdAndOwnerId(movieId,userId);
	}
}
