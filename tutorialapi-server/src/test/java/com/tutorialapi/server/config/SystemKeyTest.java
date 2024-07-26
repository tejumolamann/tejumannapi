package com.tutorialapi.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SystemKeyTest {

    @Test
    void testDefaultValues() {
        Assertions.assertEquals("8443", SystemKey.PORT.getDefaultValue());
        Assertions.assertEquals("dev", SystemKey.MODE.getDefaultValue());
    }

    @Test
    void testGetKey() {
        Assertions.assertEquals("port", SystemKey.PORT.getKey());
    }
}
