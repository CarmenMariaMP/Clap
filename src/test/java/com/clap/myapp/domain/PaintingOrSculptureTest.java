package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaintingOrSculptureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaintingOrSculpture.class);
        PaintingOrSculpture paintingOrSculpture1 = new PaintingOrSculpture();
        paintingOrSculpture1.setId("id1");
        PaintingOrSculpture paintingOrSculpture2 = new PaintingOrSculpture();
        paintingOrSculpture2.setId(paintingOrSculpture1.getId());
        assertThat(paintingOrSculpture1).isEqualTo(paintingOrSculpture2);
        paintingOrSculpture2.setId("id2");
        assertThat(paintingOrSculpture1).isNotEqualTo(paintingOrSculpture2);
        paintingOrSculpture1.setId(null);
        assertThat(paintingOrSculpture1).isNotEqualTo(paintingOrSculpture2);
    }
}
