package com.clap.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clap.myapp.IntegrationTest;
import com.clap.myapp.domain.Music;
import com.clap.myapp.domain.enumeration.MusicGenreType;
import com.clap.myapp.repository.MusicRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link MusicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicResourceIT {

    private static final MusicGenreType DEFAULT_GENRES = MusicGenreType.CLASSICAL;
    private static final MusicGenreType UPDATED_GENRES = MusicGenreType.POP;

    private static final String ENTITY_API_URL = "/api/music";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private MockMvc restMusicMockMvc;

    private Music music;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createEntity() {
        Music music = new Music().genres(DEFAULT_GENRES);
        return music;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createUpdatedEntity() {
        Music music = new Music().genres(UPDATED_GENRES);
        return music;
    }

    @BeforeEach
    public void initTest() {
        musicRepository.deleteAll();
        music = createEntity();
    }

    @Test
    void createMusic() throws Exception {
        int databaseSizeBeforeCreate = musicRepository.findAll().size();
        // Create the Music
        restMusicMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isCreated());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeCreate + 1);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getGenres()).isEqualTo(DEFAULT_GENRES);
    }

    @Test
    void createMusicWithExistingId() throws Exception {
        // Create the Music with an existing ID
        music.setId("existing_id");

        int databaseSizeBeforeCreate = musicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMusic() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        // Get all the musicList
        restMusicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].genres").value(hasItem(DEFAULT_GENRES.toString())));
    }

    @Test
    void getMusic() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        // Get the music
        restMusicMockMvc
            .perform(get(ENTITY_API_URL_ID, music.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.genres").value(DEFAULT_GENRES.toString()));
    }

    @Test
    void getNonExistingMusic() throws Exception {
        // Get the music
        restMusicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewMusic() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music
        Music updatedMusic = musicRepository.findById(music.getId()).get();
        updatedMusic.genres(UPDATED_GENRES);

        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusic.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void putNonExistingMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, music.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic.genres(UPDATED_GENRES);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void fullUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        int databaseSizeBeforeUpdate = musicRepository.findAll().size();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic.genres(UPDATED_GENRES);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
        Music testMusic = musicList.get(musicList.size() - 1);
        assertThat(testMusic.getGenres()).isEqualTo(UPDATED_GENRES);
    }

    @Test
    void patchNonExistingMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, music.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMusic() throws Exception {
        int databaseSizeBeforeUpdate = musicRepository.findAll().size();
        music.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(music))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMusic() throws Exception {
        // Initialize the database
        musicRepository.save(music);

        int databaseSizeBeforeDelete = musicRepository.findAll().size();

        // Delete the music
        restMusicMockMvc
            .perform(delete(ENTITY_API_URL_ID, music.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Music> musicList = musicRepository.findAll();
        assertThat(musicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
