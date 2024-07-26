package com.tutorialapi.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigTest {
    @Test
    void testGetKey() {
        Assertions.assertEquals("server.keystore.file", ConfigKey.SERVER_KEYSTORE_FILE.getKey());
    }
}
