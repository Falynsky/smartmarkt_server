package com.falynsky.smartmarkt.models;

import com.falynsky.smartmarkt.models.DTO.UsersDTO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Users {
    private int id;
    private String login;
    private String password;

    public Users() {
    }

    public Users(UsersDTO usersDTO) {
        this.id = usersDTO.getId();
        this.login = usersDTO.getLogin();
        this.password = usersDTO.getPassword();
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return id == users.id &&
                Objects.equals(login, users.login) &&
                Objects.equals(password, users.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
