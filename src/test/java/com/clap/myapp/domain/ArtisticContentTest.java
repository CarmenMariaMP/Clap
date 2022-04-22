package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArtisticContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtisticContent.class);
        ArtisticContent artisticContent1 = new ArtisticContent();
        artisticContent1.setId("id1");
        ArtisticContent artisticContent2 = new ArtisticContent();
        artisticContent2.setId(artisticContent1.getId());
        assertThat(artisticContent1).isEqualTo(artisticContent2);
        artisticContent2.setId("id2");
        assertThat(artisticContent1).isNotEqualTo(artisticContent2);
        artisticContent1.setId(null);
        assertThat(artisticContent1).isNotEqualTo(artisticContent2);
    }
}
