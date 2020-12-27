package com.falynsky.smartmarkt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "licences")
public class Licence {

    @Id
    private Integer id;
    @Column(name = "licence_key")
    private String licenceKey;
    @Column(name = "licence_role")
    private String licenceRole;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicenceKey() {
        return licenceKey;
    }

    public void setLicenceKey(String licenceKey) {
        this.licenceKey = licenceKey;
    }

    public String getLicenceRole() {
        return licenceRole;
    }

    public void setLicenceRole(String licenceRole) {
        this.licenceRole = licenceRole;
    }
}