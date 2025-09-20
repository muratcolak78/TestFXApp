package org.example.testfx2.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.testfx2.model.Quartal;
import org.example.testfx2.model.enums.Status;
import org.example.testfx2.model.User;
import org.example.testfx2.model.enums.QuartalArt;

import java.util.ArrayList;
import java.util.List;
public class GetJarhlichData {
	private static ObservableList<Quartal> mainTable;

	public static ObservableList<Quartal> getMainTable() {
		if (mainTable == null) {
			List<User> userList = GetUsers.getUsers();
			mainTable = FXCollections.observableArrayList(
					new Quartal(1, 2024, QuartalArt.Q1, Status.ABGENOMMEN, userList.get(0).getUserName(), "kommnetar1"),
					new Quartal(2, 2025, QuartalArt.Q2, Status.ABGENOMMEN, userList.get(2).getUserName(), "kommnetar6"),
					new Quartal(3, 2024, QuartalArt.Q2, Status.ABGENOMMEN, userList.get(1).getUserName(), "kommnetar2"),
					new Quartal(4, 2024, QuartalArt.Q3, Status.ABGENOMMEN, userList.get(3).getUserName(), "kommnetar3"),
					new Quartal(5, 2024, QuartalArt.Q4, Status.ABGENOMMEN, userList.get(3).getUserName(), "kommnetar4"),
					new Quartal(6, 2025, QuartalArt.Q1, Status.IN_BEARBEITUNG, userList.get(0).getUserName(), "kommnetar5")
			);
		}
		return mainTable;
	}

	public static void addQuartal(Quartal q) {
		getMainTable().add(q);
	}
}
