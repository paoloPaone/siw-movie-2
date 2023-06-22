package it.uniroma3.siw.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u.review FROM User u WHERE u.id = :userId")
    Map<String, Review> getUserReviewsMap(@Param("userId") Long userId);

}
