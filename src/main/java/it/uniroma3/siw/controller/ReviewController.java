package it.uniroma3.siw.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MovieService movieService;
	@Autowired 
	private CredentialsService credentialsService;
	@Autowired
	private UserService userService;




	@GetMapping("/reviews/{id}")
	public String getReview(@PathVariable("reviewId") Long id, Model model) {
		Review review = reviewService.getReviewById(id);
		model.addAttribute("review", review);
		return "review";
	}

	@GetMapping("/utente/addReview/{movieId}")
	//@PreAuthorize("hasRole('ROLE_USER')")
	public String formNewReview(@PathVariable("movieId") Long id, Model model,Authentication authentication) {
		Review review = new Review();
		model.addAttribute("review",review);
		return "utente/formNewReview.html";

	}

	@GetMapping(value="/admin/deleteReview/{reviewId}")
	public String deleteReview(@PathVariable("reviewId") Long id,Model model) {
		reviewService.deleteReview(id);
		model.addAttribute("message", "La recensione è stta eliminata con successo");
		return "admin/confirmDeleteReview.html";
	}

	@PostMapping(value="/utente/aggiornaReview/{reviewId}")
	public String aggiornaReview(@PathVariable("reviewId") Long id, @RequestParam("title") String title,
			@RequestParam("text") String text,
			@RequestParam("rating") Integer rating,Authentication authentication,Model model) {
		Review review = this.reviewService.getReviewById(id);
		review.setDate(LocalDateTime.now());
		review.setRating(rating);
		review.setText(text);
		review.setTitle(title);
		this.reviewService.updateReview(review);
		model.addAttribute("review", review);
		model.addAttribute("movie",review.getMovie());
		return "utente/review.html";
	}


	@GetMapping("/utente/updateReview/{movieId}")
	public String updateReview(@PathVariable("movieId") Long id,Authentication authentication, Model model) {
		Movie movie = this.movieService.getMovieById(id);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		Review review = user.getReview().get(movie.getTitle());
		model.addAttribute("review", review);
		return "utente/changeReview.html";
	}

	@PostMapping(value="/utente/reviews/add/{movieId}") 
	public String newReview(@Valid @ModelAttribute("review") Review review,@PathVariable("movieId") Long id, BindingResult bindingResult, Model model,Authentication authentication) {
		//this.reviewValidator.validate(movie, bindingResult);
		//if (!bindingResult.hasErrors()) {
		// Imposta il proprietario della recensione
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		// Ottieni il film dal servizio
		Movie movie = movieService.getMovieById(id);				
		// se all'utente è gia associata una recensione con quel film sostuicìsci la vecchia  recensione con la nuova
		Map<String, Review> movie2review = user.getReview();
		String titleMovie = movie.getTitle();
		if(movie2review.containsKey(titleMovie))
			movie2review.put(titleMovie,new Review());



		review.setOwner(user);


		// Imposta il film nella recensione
		review.setMovie(movie);

		// Imposta la data corrente alla recensione
		review.setDate(LocalDateTime.now());

		// Salva la recensione nel database
		reviewService.addReview(review);

		// Aggiungi la recensione al film
		movie.getReviews().add(review);
		movieService.updateMovie(movie);

		// Aggiorna la recensione dell'utente aggiungendo l'info sul titolo del film recensito
		movie2review.put(titleMovie, review);
		userService.updateUser(user);
		reviewService.updateReview(review);
		model.addAttribute("review", review);
		model.addAttribute("movie", movie);
		return "utente/review.html";
		//    		} else {
		//    			return "admin/formNewMovie.html"; 
		//    		}
	}




	@GetMapping("/utente/deleteReview/{reviewId}")
	public String deleteReview(@PathVariable("reviewId") Long id, Authentication authentication) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		Review review = reviewService.getReviewById(id);
		review.getMovie().getReviews().remove(review);

		if (review.getOwner().equals(user)) {
			reviewService.deleteReview(id);
		}


		return "utente/formNewReview.html";
	}


}

