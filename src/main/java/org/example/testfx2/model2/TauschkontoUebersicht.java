package org.example.testfx2.model2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TauschkontoUebersicht {
    private String art;
    private int saldo;
    private double summe;
}