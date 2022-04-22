package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MusicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Music.class);
        Music music1 = new Music();
        music1.setId("id1");
        Music music2 = new Music();
        music2.setId(music1.getId());
        assertThat(music1).isEqualTo(music2);
        music2.setId("id2");
        assertThat(music1).isNotEqualTo(music2);
        music1.setId(null);
        assertThat(music1).isNotEqualTo(music2);
    }
}
