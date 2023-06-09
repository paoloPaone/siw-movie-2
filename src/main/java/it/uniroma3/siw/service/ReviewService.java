package it.uniroma3.siw.service;

import java.util.List;

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
    	owner.getReview().remove(movie.getTitle());
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
}
