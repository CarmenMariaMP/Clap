package com.clap.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.clap.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationConfiguration.class);
        NotificationConfiguration notificationConfiguration1 = new NotificationConfiguration();
        notificationConfiguration1.setId("id1");
        NotificationConfiguration notificationConfiguration2 = new NotificationConfiguration();
        notificationConfiguration2.setId(notificationConfiguration1.getId());
        assertThat(notificationConfiguration1).isEqualTo(notificationConfiguration2);
        notificationConfiguration2.setId("id2");
        assertThat(notificationConfiguration1).isNotEqualTo(notificationConfiguration2);
        notificationConfiguration1.setId(null);
        assertThat(notificationConfiguration1).isNotEqualTo(notificationConfiguration2);
    }
}
