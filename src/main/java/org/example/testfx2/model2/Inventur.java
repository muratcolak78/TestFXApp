package org.example.testfx2.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventur {
	private int inventurId;
	private int quartalId;
	private int standortId;
	private int artikelId;
	private int menge;
	private String abnahmeBei;
	private int status;
}
