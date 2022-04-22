package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dance.class);
        Dance dance1 = new Dance();
        dance1.setId("id1");
        Dance dance2 = new Dance();
        dance2.setId(dance1.getId());
        assertThat(dance1).isEqualTo(dance2);
        dance2.setId("id2");
        assertThat(dance1).isNotEqualTo(dance2);
        dance1.setId(null);
        assertThat(dance1).isNotEqualTo(dance2);
    }
}
