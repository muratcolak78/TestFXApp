package org.example.testfx2.model2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tauschkonten {
	private int tauschkontenId;
	private int quartalId;
	private int kundenId;
	private int artikelId;
	private int menge;
}
