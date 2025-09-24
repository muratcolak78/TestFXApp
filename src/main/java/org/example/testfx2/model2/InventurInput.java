package org.example.testfx2.model2;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class InventurInput {
    @NotNull
    private Integer quartalId;
    private Integer standortId;
    private Integer artikelId;
    @Min(1)
    private Integer menge;
    private String abnahmeBei;
    private Integer statusId;

 }