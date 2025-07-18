package com.ciatch.gdp.domain;

import static com.ciatch.gdp.domain.UserConfigurationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ciatch.gdp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserConfiguration.class);
        UserConfiguration userConfiguration1 = getUserConfigurationSample1();
        UserConfiguration userConfiguration2 = new UserConfiguration();
        assertThat(userConfiguration1).isNotEqualTo(userConfiguration2);

        userConfiguration2.setId(userConfiguration1.getId());
        assertThat(userConfiguration1).isEqualTo(userConfiguration2);

        userConfiguration2 = getUserConfigurationSample2();
        assertThat(userConfiguration1).isNotEqualTo(userConfiguration2);
    }
}
