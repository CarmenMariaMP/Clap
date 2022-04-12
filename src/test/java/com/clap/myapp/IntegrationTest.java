package com.clap.myapp;

import com.clap.myapp.AbstractNeo4jIT;
import com.clap.myapp.ClapApplicationApp;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ClapApplicationApp.class)
@ExtendWith(AbstractNeo4jIT.class)
public @interface IntegrationTest {
}
