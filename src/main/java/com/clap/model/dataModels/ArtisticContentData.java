package com.clap.model.dataModels;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clap.model.Cinema;
import com.clap.model.Dance;
import com.clap.model.General;
import com.clap.model.Music;
import com.clap.model.PaintingOrSculpture;
import com.clap.model.Photography;
import com.clap.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtisticContentData {
    String id;

    String type;
	
    String title;

    String contentUrl;
	
	String description;

    Integer viewCount;

    String materials;

    String place;

	String size;

	String techniques;

    String camera;

    String music;

    List<String> genres;

	User owner;

    MultipartFile multipartFile;

    public General toGeneralContent() {
		General general = new General();
		general.setId(getId());
		general.setTitle(getTitle());
		general.setDescription(getDescription());
		general.setContentUrl(getContentUrl());
		general.setViewCount(getViewCount());
		general.setOwner(getOwner());
		return general;
	}

    public PaintingOrSculpture toPaintingOrSculptureUploadDataContent() {
		PaintingOrSculpture paintingOrSculpture = new PaintingOrSculpture();
		paintingOrSculpture.setId(getId());
		paintingOrSculpture.setTitle(getTitle());
		paintingOrSculpture.setDescription(getDescription());
		paintingOrSculpture.setMaterials(getMaterials());
		paintingOrSculpture.setPlace(getPlace());
		paintingOrSculpture.setSize(getSize());
		paintingOrSculpture.setTechniques(getTechniques());
		paintingOrSculpture.setContentUrl(getContentUrl());
		paintingOrSculpture.setViewCount(getViewCount());
		paintingOrSculpture.setOwner(getOwner());
		return paintingOrSculpture;
	}

    public Photography toPhotographyContent() {
		Photography photography = new Photography();
		photography.setId(getId());
		photography.setTitle(getTitle());
		photography.setDescription(getDescription());
		photography.setCamera(getCamera());
		photography.setTechniques(getTechniques());
		photography.setSize(getSize());
		photography.setPlace(getPlace());
		photography.setContentUrl(getContentUrl());
		photography.setViewCount(getViewCount());
		photography.setOwner(getOwner());
		return photography;
	}

    public Cinema toCinemaContent() {
		Cinema cinema = new Cinema();
		cinema.setId(getId());
		cinema.setTitle(getTitle());
		cinema.setDescription(getDescription());
        cinema.setGenres(getGenres());
		cinema.setContentUrl(getContentUrl());
		cinema.setViewCount(getViewCount());
		cinema.setOwner(getOwner());
		return cinema;
	}

    public Dance toDanceContent() {
		Dance dance = new Dance();
		dance.setId(getId());
		dance.setTitle(getTitle());
		dance.setDescription(getDescription());
		dance.setMusic(getMusic());
		dance.setGenres(getGenres());
		dance.setContentUrl(getContentUrl());
		dance.setViewCount(getViewCount());
		dance.setOwner(getOwner());
		return dance;
	}

    public Music toMusicContent() {
		Music music = new Music();
		music.setId(getId());
		music.setTitle(getTitle());
		music.setDescription(getDescription());
        music.setGenres(getGenres());
		music.setContentUrl(getContentUrl());
		music.setViewCount(getViewCount());
		music.setOwner(getOwner());
		return music;
	}
}
