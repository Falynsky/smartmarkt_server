package com.falynsky.smartmarkt.models.DTO;

public class LicenceDTO {

    int id;
    String licenceKey;
    String licenceRole;

    public LicenceDTO(int id, String licenceKey, String licenceRole) {
        this.id = id;
        this.licenceKey = licenceKey;
        this.licenceRole = licenceRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
