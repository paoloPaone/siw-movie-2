package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Movie;

public interface MovieRepository extends CrudRepository<Movie,Long> {
	
	public List<Movie> findByYear(Integer year);
	
	public boolean existsByTitleAndYear(String title, Integer year);
	
	@Query("SELECT AVG(r.rating) FROM Movie m JOIN m.reviews r WHERE m.id = :movieId")
    Double computeAverageRatingById(@Param("movieId") Long movieId);


	@Query("SELECT COUNT(*) FROM Movie")
	Long numeroFilm();

}
