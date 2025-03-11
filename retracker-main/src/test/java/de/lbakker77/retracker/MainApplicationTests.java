package de.lbakker77.retracker;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
@Tag("integration")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MainApplicationTests {

	@Test
	void contextLoads() {
		// Basic test to verify the application context loads correctly
	}


	@Test
	void verifyArchitecture() {
		ApplicationModules.of(MainApplication.class).verify();
	}
}
