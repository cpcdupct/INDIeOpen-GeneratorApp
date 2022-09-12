package com.cpcd.microservices.app.generator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;
import java.nio.file.Paths;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan({"com.cpcd.microservices.app.servicescommons.models.entity"})
public class GeneratorApplication implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
		File file = new File("./learningunits");
		if (!file.exists()) {
			file.mkdir();
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(GeneratorApplication.class, args);
	}

}
