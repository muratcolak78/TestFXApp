package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artikel {
	private int id;
	private String artikelNr;
	private String artikelName;
	private int artikelType;
	private int qualitaetId;
	private String groesse;
	private double einkaufPreis;
	private double verkaufPreis;
	private double gewicht;
	private double maxBelastung;
	private Date createdAt;


}
