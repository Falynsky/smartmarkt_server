package com.falynsky.smartmarkt.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.Objects;

@Entity
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

    public void setLicenceKey(String licence_key) {
        this.licenceKey = licence_key;
    }

    public String getLicenceRole() {
        return licenceRole;
    }

    public void setLicenceRole(String licence_role) {
        this.licenceRole = licence_role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Licence licence = (Licence) o;
        return id.equals(licence.id) &&
                licenceKey.equals(licence.licenceKey) &&
                licenceRole.equals(licence.licenceRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licenceKey, licenceRole);
    }

    @Override
    public String toString() {
        return "Licence{" +
                "id=" + id +
                ", licenceKey='" + licenceKey + '\'' +
                ", licenceRole='" + licenceRole + '\'' +
                '}';
    }
}