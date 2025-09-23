package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Berechnung {
    private int id;
    private String material;
    private String status;
    private double preisProGram;
    private double summe;
    private Date createdAt;
}