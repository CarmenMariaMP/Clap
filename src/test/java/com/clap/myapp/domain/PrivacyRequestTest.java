package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrivacyRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivacyRequest.class);
        PrivacyRequest privacyRequest1 = new PrivacyRequest();
        privacyRequest1.setId("id1");
        PrivacyRequest privacyRequest2 = new PrivacyRequest();
        privacyRequest2.setId(privacyRequest1.getId());
        assertThat(privacyRequest1).isEqualTo(privacyRequest2);
        privacyRequest2.setId("id2");
        assertThat(privacyRequest1).isNotEqualTo(privacyRequest2);
        privacyRequest1.setId(null);
        assertThat(privacyRequest1).isNotEqualTo(privacyRequest2);
    }
}
