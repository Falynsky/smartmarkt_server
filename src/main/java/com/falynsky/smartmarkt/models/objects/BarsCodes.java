package com.falynsky.smartmarkt.models.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bars_codes")
public class BarsCodes {

    @Id
    @Column(name = "id", nullable = false)
    public int id;
    @Column(name = "code", nullable = false)
    public int code;
    @Column(name = "product_id", nullable = false)
    public int productId;

}
