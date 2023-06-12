package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.ImageValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ArtistRepository artistRepository;
	@Autowired
	private ArtistService artistService;
	@Autowired
	private UserService userService;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private ImageValidator imageValidator;



	@Transactional
	public Iterable<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	@Transactional
	public Movie getMovieById(Long id) {
		return movieRepository.findById(id).orElse(null);
	}

	@Transactional
	public void addMovie(@Valid Movie movie, MultipartFile image) throws IOException {
		Image movieImg = new Image(image.getBytes());
		this.imageRepository.save(movieImg);
		movie.setPicture(movieImg);
		this.movieRepository.save(movie);
		movieRepository.save(movie);
	}

	@Transactional
	public void updateMovie(Movie movie) {
		movieRepository.save(movie);
	}

	@Transactional
	public void addImage(Movie movie, MultipartFile image) throws IOException{
		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
			Image movieImg = new Image(image.getBytes());
			this.imageRepository.save(movieImg);
			movie.getImages().add(movieImg);
			this.movieRepository.save(movie);
		}

	}
	
	@Transactional
	public void addPoster(Movie movie, MultipartFile image) throws IOException{
		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
			Image movieImg = new Image(image.getBytes());
			this.imageRepository.save(movieImg);
			movie.setPicture(movieImg);
			this.movieRepository.save(movie);
		}

	}



	@Transactional
	public void removeImage(Long movieId, Long imageId) {
		Image image = this.imageRepository.findById(imageId).get();
		Movie movie = this.getMovieById(movieId);
		movie.getImages().remove(image);
		this.movieRepository.save(movie);
	}

	@Transactional
	public void deleteMovie(Long id) {
		Movie movie = this.getMovieById(id);
		List<Review> reviews = movie.getReviews();
		for (Review review : reviews) {
			User owner = review.getOwner();
			owner.getReviews().remove(review);
			owner.getReview().remove(movie.getTitle());
			this.userService.saveUser(owner);
		}
		movieRepository.deleteById(id);
	}

	@Transactional
	public Movie setDirectorToMovie(Long directorId, Long movieId) {
		Movie movie = this.movieRepository.findById(movieId).get();
		Artist director = this.artistRepository.findById(directorId).get();
		movie.setDirector(director);
		this.updateMovie(movie);
		return movie;
	}

	@Transactional
	public List<Movie> getMoviesByYear(int year) {
		return this.movieRepository.findByYear(year);

	}

	@Transactional
	public Movie addActorToMovieActors(Long movieId, Long actorId) {
		Movie movie = this.getMovieById(movieId);
		Artist actor = this.artistService.getArtistById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.updateMovie(movie);
		return movie;

	}

	@Transactional
	public Movie removeActorFromMovie(Long movieId, Long actorId) {
		Movie movie = this.getMovieById(movieId);
		Artist actor = this.artistService.getArtistById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieRepository.save(movie);
		return movie;

	}
}
