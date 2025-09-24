package org.example.testfx2.model2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelPreis {
	private int artikelId;
	private double einkaufPreis;
	private double verkaufpreis;

}
