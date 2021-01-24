package com.falynsky.smartmarkt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "licences")
public class Licences {

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "mail")
    public String mail;
    @Column(name = "type")
    public String type;

}
