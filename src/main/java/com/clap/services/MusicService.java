package com.clap.services;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.clap.model.Music;
import com.clap.model.User;
import com.clap.model.dataModels.ArtisticContentData;
import com.clap.repository.MusicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {
    private final MusicRepository musicRepository;

    public Music getMusicContentById(String artistic_content_id) {
		return musicRepository.findMusicContentById(artistic_content_id);
	}

    public Music uploadMusicContent(ArtisticContentData musicUploadData, User owner)
            throws Exception {
                Music musicContent = musicUploadData.toMusicContent();
                musicContent.setOwner(owner);
        return upload(musicContent);
    }

    private Music upload(Music musicContent) throws Exception {
        musicContent.setUploadDate(Date.from(Instant.now()));
        musicContent.setType("MUSIC");
        musicContent.setViewCount(0);
        return musicRepository.save(musicContent);

    }

    public Music updateViewsMusicContent(Music music) {
        music.setViewCount(music.getViewCount()+1);   
        return musicRepository.save(music);
    }
}
