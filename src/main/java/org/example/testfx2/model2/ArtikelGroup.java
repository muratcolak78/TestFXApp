package org.example.testfx2.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelGroup {
	private int artikelGroupId;
	private String name;
	private int typeId;
}
