package org.example.testfx2.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.testfx2.model.enums.QuartalArt;
import org.example.testfx2.model.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quartal {
	private int id;
	private int jahr;
	private QuartalArt quartal;
	private Status status;
	private String abnaheBei;
	private String kommentar;
}


