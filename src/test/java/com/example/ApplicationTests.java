package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class) // if not specified, it'll use JUnit

// ITs
@SpringApplicationConfiguration(classes = Application.class) // use this to load context for ITs
// @IntegrationTest --> spins up a web server
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
