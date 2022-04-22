package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhotographyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Photography.class);
        Photography photography1 = new Photography();
        photography1.setId("id1");
        Photography photography2 = new Photography();
        photography2.setId(photography1.getId());
        assertThat(photography1).isEqualTo(photography2);
        photography2.setId("id2");
        assertThat(photography1).isNotEqualTo(photography2);
        photography1.setId(null);
        assertThat(photography1).isNotEqualTo(photography2);
    }
}
