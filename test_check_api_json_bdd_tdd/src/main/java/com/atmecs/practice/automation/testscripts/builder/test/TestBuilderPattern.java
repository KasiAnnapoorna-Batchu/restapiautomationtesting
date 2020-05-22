package com.atmecs.practice.automation.testscripts.builder.test;

import org.testng.annotations.Test;

import com.atmecs.practice.automation.testscripts.design.builder.User;

/**
 * Test Builder Pattern Class with the UserBuilderClass
 */
public class TestBuilderPattern {
	
	
	// Execute.
	@Test
	public  void execute() {
	    User user1 = new User.UserBuilder("Lokesh", "Gupta")
	    .age(30)
	    .phone("1234567")
	    .address("Fake address 1234")
	    .build();
	 
	    System.out.println(user1);
	 
	    User user2 = new User.UserBuilder("Jack", "Reacher")
	    .age(40)
	    .phone("5655")
	    //no address
	    .build();
	 
	    System.out.println(user2);
	 
	    User user3 = new User.UserBuilder("Super", "Man")
	    //No age
	    //No phone
	    //no address
	    .build();
	 
	    System.out.println(user3);
	}
	 

}
