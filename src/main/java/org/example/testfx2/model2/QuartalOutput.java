package org.example.testfx2.model2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

 public class QuartalOutput {
	private int jahr;
	private int quartalId;
	private String quartal;
	private String status;
	private String abnahmeBei;
	private String kommentar;
}
