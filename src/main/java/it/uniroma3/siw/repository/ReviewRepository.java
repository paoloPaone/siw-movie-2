package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;

public interface ReviewRepository extends CrudRepository<Review, Long> {

	public List<Review> getReviewsByMovieId(Long movieId);

	public void deleteReviewsByMovieId(Long movieId);

	public Integer countByMovie(Movie movie);

	public boolean existsByMovieIdAndOwnerId(Long movieId, Long userId);

	public Review findByMovieIdAndOwnerId(Long movieId, Long userId);




}
