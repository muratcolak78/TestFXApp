package org.example.testfx2.repository;

import org.example.testfx2.db.Database;
import org.example.testfx2.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UsersRepo {
	private static List<User> userList = new ArrayList<>();

	// Verileri otomatik yükleyen static blok
	static {
		refreshData();
	}

	public static void refreshData() {
		userList.clear();
		String sql = "SELECT * FROM users";

		try (Connection connection = Database.connect();
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery(sql)) {

			while (resultSet.next()) {
				userList.add(new User(
						resultSet.getInt("id"),
						resultSet.getString("userName")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String getUserById(int userId) {
		return userList.stream()
				.filter(user -> user.getId() == userId)
				.findFirst()
				.map(User::getUserName)
				.orElse("Unknown"); // null yerine "Unknown" döndür
	}
	public static List<User> getUsers(){
		return userList;
	}
}