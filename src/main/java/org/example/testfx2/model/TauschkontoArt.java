package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TauschkontoArt {
    private int id;
    private String artName;
    private Date createdAt;
}