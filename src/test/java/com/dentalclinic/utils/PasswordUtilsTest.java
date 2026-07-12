package com.dentalclinic.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class PasswordUtilsTest {

    @Test
    void testHashAndCheckPassword() {
        String password = "securePassword123";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertTrue(PasswordUtils.checkPassword(password, hashedPassword));
        assertFalse(PasswordUtils.checkPassword("wrongPassword", hashedPassword));
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<PasswordUtils> constructor = PasswordUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(exception.getCause() instanceof UnsupportedOperationException);
    }
}
