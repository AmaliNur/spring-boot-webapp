package com.example.cargo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

@Setter
@Entity
public class Cargo {

    private int ID;
    @Getter
    private String cname;
    @Getter
    private String content;
    @Getter
    private String city1;
    @Getter
    private String city2;
    @Getter
    private String date1;
    @Getter
    private String date2;


    protected Cargo() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getID() {
        return ID;
    }
}
