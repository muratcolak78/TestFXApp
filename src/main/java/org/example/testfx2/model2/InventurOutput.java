package org.example.testfx2.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File:           InventurOutput.java
 * Version:        Revision: 0.1
 * Last changed:   26.09.2025
 * Purpose:
 * Author:         mcol
 * Copyright:      (C) allcompare GmbH 2022
 * Product:        TestFx3
 * Package:        org.example.testfx2.model2
 */
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
