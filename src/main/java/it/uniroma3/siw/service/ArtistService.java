package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.ImageValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.ImageRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
	private ImageRepository imageRepository;
    
    @Autowired
	private ImageValidator imageValidator;
    

    

    @Transactional
    public Artist getArtistById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    

    @Transactional
    public void updateArtist(Artist artist) {
        artistRepository.save(artist);
    }

    @Transactional
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }

    @Transactional
	public Iterable<Artist> getAllArtistsNotInMovie(Long movieId) { 
		return this.artistRepository.findActorsNotInMovie(movieId);
	}
    
    @Transactional
    public List<Artist> actorsToAdd(Long movieId) {
		List<Artist> actorsToAdd = new ArrayList<>();

		Iterable<Artist> actorsNotInMovie = this.getAllArtistsNotInMovie(movieId);
		for (Artist a : actorsNotInMovie) {
			actorsToAdd.add(a);
		}
		return actorsToAdd;
	}
    
    @Transactional
    public List<Artist> allArtists() {
    	List<Artist> all = new ArrayList<>();
    	Iterable<Artist> artists = this.artistRepository.findAll();
    	for(Artist a : artists) 
    		all.add(a);
    	return all;
    }


	@Transactional
	public void setPicture(Artist artist, MultipartFile image) throws IOException{
		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
			Image oldImgArtist = artist.getPicture();
			Image newArtistImg = new Image(image.getBytes());
			this.imageRepository.save(newArtistImg);
			artist.setPicture(newArtistImg);
			this.imageRepository.delete(oldImgArtist);
			this.artistRepository.save(artist);
		}

	}
    
    
	@Transactional
	public void addPicture(Artist artist, MultipartFile image) throws IOException{
		if (this.imageValidator.isImage(image) || image.getSize() < ImageValidator.MAX_IMAGE_SIZE){
			Image artistImg = new Image(image.getBytes());
			this.imageRepository.save(artistImg);
			artist.setPicture(artistImg);
			this.artistRepository.save(artist);
		}

	}

	
    
   


}
