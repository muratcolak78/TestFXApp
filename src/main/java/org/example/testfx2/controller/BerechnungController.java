package org.example.testfx2.controller;


import org.example.testfx2.views.BerechnungView;

import java.sql.SQLException;

public class BerechnungController {
	public void openForm() throws SQLException {
		new BerechnungView().show();
	}
}
