package com.workOUTcoach;

import com.workOUTcoach.utility.DatabaseConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkOuTcoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkOuTcoachApplication.class, args);
		//DatabaseConnector databaseConnector = new DatabaseConnector();
	}
}
