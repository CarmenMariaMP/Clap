package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContentCreatorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentCreator.class);
        ContentCreator contentCreator1 = new ContentCreator();
        contentCreator1.setId("id1");
        ContentCreator contentCreator2 = new ContentCreator();
        contentCreator2.setId(contentCreator1.getId());
        assertThat(contentCreator1).isEqualTo(contentCreator2);
        contentCreator2.setId("id2");
        assertThat(contentCreator1).isNotEqualTo(contentCreator2);
        contentCreator1.setId(null);
        assertThat(contentCreator1).isNotEqualTo(contentCreator2);
    }
}
