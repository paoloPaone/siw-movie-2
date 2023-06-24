package it.uniroma3.siw.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String formNewReview(@PathVariable("movieId") Long id, Model model,Authentication authentication) {
		Review review = new Review();
		Movie movie = movieService.getMovieById(id);
		model.addAttribute("review",review);
		model.addAttribute("movie",movie);
		return "utente/formNewReview.html";

	}

	@GetMapping(value="/admin/deleteReview/{reviewId}")
	public String deleteReview(@PathVariable("reviewId") Long id,Model model) {
		reviewService.deleteReview(id);
		model.addAttribute("message", "La recensione è stata eliminata con successo");
		return "admin/confirmDeleteReview.html";
	}

	@PostMapping(value="/utente/aggiornaReview")
	public String resocontoAggiornamentoReview(@ModelAttribute("review") Review review,@RequestParam("movieId") Long movieId,Model model) {
		Movie movie = movieService.getMovieById(movieId);
		this.aggiornaInfoReview(review, movie);
		model.addAttribute("movie",movie);
		model.addAttribute("review", review);
		return "utente/review.html";
	}
	
	@PostMapping(value="/utente/reviews/add") 
	public String newReview(@Valid @ModelAttribute("review") Review review,@RequestParam("movieId") Long id, Model model) {
		Movie movie = movieService.getMovieById(id);
		aggiornaInfoReview(review, movie);
		addReviewToMovie(review, movie);
		reviewService.updateReview(review);
		model.addAttribute("review", review);
		model.addAttribute("movie", movie);
		return "utente/review.html";
	}
	
	private void aggiornaInfoReview(Review review,Movie movie ) {
		review.setMovie(movie);
		User owner = this.setProprietario(review);
		userService.updateUser(owner);
		review.setDate(LocalDateTime.now());
		reviewService.updateReview(review);
		
	}


	@GetMapping("/utente/updateReview/{movieId}")
	public String formUpdateReview(@PathVariable("movieId") Long movieId,Authentication authentication, Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		Long userId = user.getId();
		Review review = reviewService.findByMovieIdAndOwnerId(movieId,userId);
		model.addAttribute("review", review);
		model.addAttribute("movie", movieService.getMovieById(movieId));
		return "utente/changeReview.html";
	}

	

	private void addReviewToMovie(Review review, Movie movie) {
		// Imposta il film nella recensione
		// Salva la recensione nel database
		reviewService.addReview(review);
		//aggiorna il film corrente
		movieService.addReviewToMovie(review,movie);
	}

	private User setProprietario(Review review) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		Credentials credentials = credentialsService.getCredentials(username);
		User user = credentials.getUser();
		review.setOwner(user);
		userService.updateUser(user);
		user.getReviews().add(review);
		return user;
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
