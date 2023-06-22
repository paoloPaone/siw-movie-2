package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import it.uniroma3.siw.controller.validator.ImageValidator;
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
	@Autowired
	private ImageValidator imageValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}
	
	@GetMapping(value="/admin/indexArtist")
	public String indexArtist() {
		return "admin/indexArtist.html";
	}
	
	@PostMapping("/admin/aggiornaDataDiMorteArtista") 
	public String updateDateOfDeath(@RequestParam("artistId") Long id,@RequestParam("dateOfDeath") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDiMorte,Model model ) {
		Artist artist = this.artistService.getArtistById(id);
		artist.setDateOfDeath(dataDiMorte);
		this.artistService.updateArtist(artist);
		model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";
	}
	
	@PostMapping("/admin/rimuoviDataDiMorteArtista") 
	public String delteDateOfDeath(@RequestParam("artistId") Long id,Model model ) {
		Artist artist = this.artistService.getArtistById(id);
		artist.setDateOfDeath(null);
		this.artistService.updateArtist(artist);
		model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";
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
		//modifico localmente questo metodo per avrere tuti gli artisti rdinati per scognome 
		model.addAttribute("artists", this.artistService.allArtists());
		//model.addAttribute("artists",this.artistRepository.findAll());
		return "admin/artists.html";
	}
	
	@PostMapping("/admin/artist")
	public String newArtist(Model model, @Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult,
			@RequestParam("file") MultipartFile image) throws IOException {
		this.imageValidator.validate(image,bindingResult);
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.artistService.addPicture(artist, image);
			model.addAttribute("artist", artist);
			return "artist.html";
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNewArtist.html";
		}
	}
	
	@PostMapping("/admin/inserisciImmagineArtista")
	public String uploadArtistImage(@RequestParam("artistId") Long id, 
			@RequestParam("file") MultipartFile image,Model model) throws IOException {
		Artist artist = this.artistService.getArtistById(id);
		artistService.addPicture(artist, image);
			model.addAttribute("artists", this.artistService.allArtists());
			return "artists.html";
		
	}
	
	@PostMapping("/admin/aggiornaImmagineArtista")
	public String updateArtistImage(@RequestParam("artistId") Long id, 
			@RequestParam("file") MultipartFile image,Model model) throws IOException {
		Artist artist = this.artistService.getArtistById(id);
		artistService.setPicture(artist, image);
			model.addAttribute("artists", this.artistService.allArtists());
			return "artists.html";
		
	}


	
	
	@GetMapping("/admin/updateArtist/{id}")
	public String updateArtist(@PathVariable("id") Long id,Model model) {
		model.addAttribute("artist",this.artistRepository.findById(id).orElse(null));
		return "admin/formUpdateArtist.html";
		
	}
	
	@PostMapping("/admin/addImageToArtist/")
	public String addImageToArtist(@RequestParam("artistid") Long id,BindingResult bindingResult,MultipartFile image ,Model model) throws IOException {
		Artist artist = this.artistRepository.findById(id).orElse(null);
		this.artistValidator.validate(artist, bindingResult);
		this.artistService.addPicture(artist, image);
		model.addAttribute("artist", artist);
		return "admin/formUpdateArtist.html";	
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
			model.addAttribute("artists",this.artistService.allArtists());
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
