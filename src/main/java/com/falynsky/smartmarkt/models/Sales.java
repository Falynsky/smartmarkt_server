package com.falynsky.smartmarkt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "title")
    public String title;
    @Column(name = "description")
    public String description;
}
