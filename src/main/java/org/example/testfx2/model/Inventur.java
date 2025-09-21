package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventur {
	private int id;
	private int standortId;
	private String status;
	private String abgabeDatum;
	private int abgabePerson;
	private String abnahmeDatum;
	private int abnahmePerson;
	private double summeGueter;
	private int summePaletten;
	private String createdAt;
}