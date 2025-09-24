package org.example.testfx2.model2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelOutput {

		private int artikelId;
		private String name;
		private double einkaufPreis;
		private double verkaufPreis;
		private String artikelGroup;
		private String artikelType;


}
