package org.example.testfx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tauschkonto {
    private int id;
    private int kundeId;
    private int artId;
    private int saldo;
    private String status;
    private Date createdAt;
}