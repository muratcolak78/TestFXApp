package org.example.testfx2.repository;

import org.example.testfx2.db.Database;
import org.example.testfx2.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersRepo {

	public static List<User> getUsers() throws SQLException {
		List<User> userList=new ArrayList<>();
		String sql="select * from users;";
				try{
					Connection connection= Database.connect();
					Statement statement=connection.createStatement();
					ResultSet resultSet=statement.executeQuery(sql);
					while (resultSet.next()){
						userList.add(new User(resultSet.getInt("id"), resultSet.getString("userName")));
					}
				}catch (SQLException e){
					e.printStackTrace();
				}
		return userList;
	}
	public void saveUser(User user){
		String sgl="insert into users (userName) values (?)";
		try (Connection conn = Database.connect();
		    PreparedStatement pstmt = conn.prepareStatement(sgl)) {
			pstmt.setString(1, user.getUserName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
