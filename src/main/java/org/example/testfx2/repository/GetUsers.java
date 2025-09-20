package org.example.testfx2.repository;

import org.example.testfx2.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetUsers {
	public static List<User> userList;
	public static List<User> getUsers(){
		userList=new ArrayList<>(List.of(
				new User(1,"olgar"),
				new User(2, "hasan"),
				new User(3,"max"),
				new User(4,"maria")

		));
		return userList;
	}
}
