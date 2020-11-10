/**
 * Springboot server
 * @author Maxime Hutinet <maxime@hutinet.ch>
 * @author Justin Foltz <justin.foltz@gmail.com>
 */

package com.waterloggedorganisation.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
