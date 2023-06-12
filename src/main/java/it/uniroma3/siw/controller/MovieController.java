package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.ImageValidator;
import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;

@Controller
public class MovieController {

	@Autowired 
	private MovieValidator movieValidator;

	@Autowired 
	private CredentialsService credentialsService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ImageValidator imageValidator;

	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	//	@GetMapping(value="/formUpdateMovie/{id}")
	//	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
	//		String ruolo = this.credentialsService.getRuolo();
	//		model.addAttribute("movie", this.movieService.getMovieById(id));
	//		if(ruolo.equals(Credentials.UTENTE_ROLE))
	//		return "utente/formUpdateMovie.html";
	//		else 
	//			return "admin/formUpdateMovie.html";		
	//	}

	//da rifattorizzzare
	@GetMapping(value="/utente/formUpdateMovie/{id}")
	public String formUpdateMovieUtente(@PathVariable("id") Long id, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = this.credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();
		Map<String, Review> review = user.getReview();
		model.addAttribute("user",user);
		model.addAttribute("review",review);
		Movie movie = this.movieService.getMovieById(id);
		model.addAttribute("movie", movie);
		model.addAttribute("recen", review.get(movie.getTitle()));
		return "utente/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovieAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/showReviews/{id}")
	public String manageReviews(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));
		model.addAttribute("reviews",this.reviewService.getReviewsByMovieId(id));
		return "admin/manageReviews.html";
	}


	//	@GetMapping(value="/indexMovie")
	//	public String indexMovie() {
	//		String ruolo = this.credentialsService.getRuolo();
	//		if(ruolo.equals(Credentials.UTENTE_ROLE))
	//		return "utente/indexMovie.html";
	//		else 
	//			return "admin/indexMovie.html";		
	//	}



	//da rifattorizzzare
	@GetMapping(value="/utente/indexMovie")
	public String indexMovieUtente() {
		return "utente/indexMovie.html";
	}

	//da rifattorizzzare
	@GetMapping(value="/admin/indexMovie")
	public String indexMovieAdmin() {
		return "admin/indexMovie.html";
	}

	//	@GetMapping(value="/manageMovies")
	//	public String indexMovie() {
	//		String ruolo = this.credentialsService.getRuolo();
	//		if(ruolo.equals(Credentials.UTENTE_ROLE))
	//		return "utente/manageMovies.html";
	//		else 
	//			return "admin/manageMovies.html";		
	//	}

	//da rifattorizzzare
	@GetMapping(value="/utente/manageMovies")
	public String manageMoviesUtente(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "utente/manageMovies.html";
	}

	//da rifattorizzzare
	@GetMapping(value="/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "admin/manageMovies.html";
	}

	@GetMapping(value="/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId, Model model) {
		model.addAttribute("movie",this.movieService.setDirectorToMovie(directorId,movieId));
		return "admin/formUpdateMovie.html";
	}


	@GetMapping(value="/admin/addDirector/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists",this.artistService.getAllArtists());
		model.addAttribute("movie", this.movieService.getMovieById(id));
		return "admin/directorsToAdd.html";
	}

	@GetMapping("/admin/confirmDeleteMovie/{movieId}")
	public String showDeleteMovieConfirmation(@PathVariable("movieId") Long movieId, Model model) {
		Movie movie = movieService.getMovieById(movieId);
		List<Review> reviews = reviewService.getReviewsByMovieId(movieId);

		model.addAttribute("movie", movie);
		model.addAttribute("reviews", reviews);

		return "admin/delete_confirmation.html";
	}

	@PostMapping(value="/admin/deleteMovie")
	public String deleteMovie(@RequestParam("movieId") Long movieId,Model model) {
		// Elimina tutte le recensioni associate al film
		//reviewService.deleteReviewsByMovieId(movieId);

		// Elimina il film
		movieService.deleteMovie(movieId);
		model.addAttribute("message", "Eliminazione completata con successo");

		return "admin/deleteSuccess.html";
	}



	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model,@RequestParam("file") MultipartFile image) throws IOException  {		
		this.imageValidator.validate(image, bindingResult);
		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieService.addMovie(movie,image);
			model.addAttribute("movie", movie);
			return "movie.html";
		} else {
			return "admin/formNewMovie.html"; 
		}
	}



	@GetMapping("/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.getMovieById(id));		
		return "movie.html";
	}

	@GetMapping("/movies")
	public String getMovies(Model model) {
		model.addAttribute("movies",this.movieService.getAllMovies());
		try {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			model.addAttribute("user", credentials.getUser());
			return "registered/movies.html";
		}
		catch(Exception e ) {
			//user non esiste			
			return "movies.html";
		}		
	}

	@GetMapping("/formSearchMovies")
	public String formSearchMovies() {
		return "formSearchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam int year) {
		model.addAttribute("movies",this.movieService.getMoviesByYear(year));
		return "foundMovies.html";
	}

	@GetMapping("/admin/updateActors/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {
		List<Artist> actorsToAdd = this.artistService.actorsToAdd(id);
		model.addAttribute("actorsToAdd", actorsToAdd);
		model.addAttribute("movie", this.movieService.getMovieById(id));
		return "admin/actorsToAdd.html";
	}
	
	@PostMapping("/admin/addImageToMovie")
	public String addImageToMovie(@RequestParam("file") MultipartFile image,@RequestParam("movieId") Long movieId,Model model) throws IOException {
		Movie movie = this.movieService.getMovieById(movieId);
		movieService.addImage(movie, image);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";


	}
	
	@PostMapping("/admin/addPosterToMovie")
	public String addPosterToMovie(@RequestParam("file") MultipartFile image,@RequestParam("movieId") Long movieId,Model model) throws IOException {
		Movie movie = this.movieService.getMovieById(movieId);
		movieService.addPoster(movie, image);
		model.addAttribute("movie", movie);
		return "admin/formUpdateMovie.html";


	}

	@GetMapping(value = "/admin/deleteImage/{movieId}/{imageId}")
	public String removeImage(@PathVariable("movieId") Long movieId, @PathVariable("imageId") Long imageId,Model model) {
		this.movieService.removeImage(movieId, imageId);
		model.addAttribute("movie", this.movieService.getMovieById(movieId));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.addActorToMovieActors(movieId,actorId);		
		List<Artist> actorsToAdd = artistService.actorsToAdd(movieId);
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);
		return "admin/actorsToAdd.html";
	}

	@GetMapping(value="/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId, Model model) {
		Movie movie = this.movieService.removeActorFromMovie(movieId,actorId);
		List<Artist> actorsToAdd = this.artistService.actorsToAdd(movieId);
		model.addAttribute("movie", movie);
		model.addAttribute("actorsToAdd", actorsToAdd);

		return "admin/actorsToAdd.html";
	}


}