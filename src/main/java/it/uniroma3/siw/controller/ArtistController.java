package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Set;

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

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistRepository artistRepository;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private ArtistValidator artistValidator;
	@Autowired
	private ArtistService artistService;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/deleteArtist")
	public String deleteArtist(@RequestParam("artistId") Long artistId, Model model) {
		Artist artist = this.artistRepository.findById(artistId).orElse(null);
		Set<Movie> directedMovies = artist.getDirectedMovies();
		Set<Movie> starredMovies = artist.getStarredMovies();
		for (Movie movie : directedMovies) {
			movie.setDirector(null);
		}
		for (Movie movie : starredMovies) {
			movie.getActors().remove(artist);
		}
		model.addAttribute("messaggio", "L'artista è stato eliminato correttamente!");
		this.artistRepository.deleteById(artistId);
		return "admin/deleteArtistSuccess.html"; 
		
	}
	
	@GetMapping(value="/admin/artists")
	public String showAllArtists(Model model) {
		model.addAttribute("artists",this.artistRepository.findAll());
		return "admin/artists.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(Model model, @Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult,
			@RequestParam("file") MultipartFile image) throws IOException {
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.artistService.addArtist(artist, image);
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html";
		}
	}
	
	
	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistRepository.findById(id).get());
		return "artist.html";
	}
	
	@GetMapping("/admin/deleteArtist/{id}")
	public String confirmDeleteArtist(@PathVariable("id") Long id, Model model) {
		Artist artist = this.artistRepository.findById(id).get();
		model.addAttribute("artist", artist);
		return "admin/confirmDeleteArtist.html";
	}


	@GetMapping("/artists")
	public String getArtists(Model model) {
			model.addAttribute("artists",this.artistRepository.findAll());
			try {
	    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			model.addAttribute("user", credentials.getUser());
			return "registered/artists.html";
	    	}
			catch(Exception e ) {
				//user non esiste			
				return "artists.html";
			}		
		}
	
}
