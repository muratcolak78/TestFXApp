package org.example.testfx2.model2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Quartal {
    private Integer quartalId;
    private Integer jahr;
    private Integer quartalartId;
    private Integer statusId;
    private String abnahmeBei;
    private String kommentar;

}