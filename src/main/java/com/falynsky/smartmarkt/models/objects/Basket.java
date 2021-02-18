package com.falynsky.smartmarkt.models.objects;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "baskets")
public class Basket {

    public Basket(int id, String name, User userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    @Id
    @Column(name = "id")
    public int id;
    @Column(name = "name")
    public String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User userId;

}

