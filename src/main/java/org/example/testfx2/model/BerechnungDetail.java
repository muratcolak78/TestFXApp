package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BerechnungDetail {
    private int id;
    private int berechnungId;
    private String rechnungsnummer;
    private double menge;
    private String mass;
    private double kosten;
    private double summe;
    private Date createdAt;
}