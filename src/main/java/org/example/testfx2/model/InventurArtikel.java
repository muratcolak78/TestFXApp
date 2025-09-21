package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventurArtikel {
	private int id;
	private int inventurId;
	private int artikelId;
	private int menge;
	private double gesamtwert;
}