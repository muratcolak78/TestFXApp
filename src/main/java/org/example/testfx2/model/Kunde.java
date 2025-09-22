package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kunde {
    private int id;
    private String kundenNr;
    private String name;
    private Date createdAt;
}