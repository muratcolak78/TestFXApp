package org.example.testfx2.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventurOutput {
	private String standort;
	private String status;
	private String abgabeBei;
	private String abgabeDatum;
	private String abnahmeBei;
	private double summeGitterBoxPreis;
	private double summePalepreis;
	private double summe;
}
