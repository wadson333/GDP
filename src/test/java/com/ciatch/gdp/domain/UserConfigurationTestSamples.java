package com.ciatch.gdp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserConfigurationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserConfiguration getUserConfigurationSample1() {
        return new UserConfiguration().id(1L).twoFactorSecret("twoFactorSecret1");
    }

    public static UserConfiguration getUserConfigurationSample2() {
        return new UserConfiguration().id(2L).twoFactorSecret("twoFactorSecret2");
    }

    public static UserConfiguration getUserConfigurationRandomSampleGenerator() {
        return new UserConfiguration().id(longCount.incrementAndGet()).twoFactorSecret(UUID.randomUUID().toString());
    }
}
