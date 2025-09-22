package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KundeSaldo {
    private String kundeName;
    private int saldo;
    private String status;
}