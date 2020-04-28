package ru.sbrf.lab.model;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "users", schema = "apl_security")
public class User {
    @Id
    Long id;
    @Column (name = "sber_pdi")
    String sber_pdi;
    @Column (name = "ora_sau")
    String ora_sau;
    @Column (name = "first_name")
    String first_name;
    @Column (name = "last_name")
    String last_name;
    @Column (name = "patronymic")
    String patronymic;

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public User(){}

    public User(Long id, String sber_pdi, String ora_sau, String first_name, String last_name)
    {
        this.id = id;
        this.sber_pdi = sber_pdi;
        this.ora_sau = ora_sau;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSber_pdi() {
        return sber_pdi;
    }

    public void setSber_pdi(String sber_pdi) {
        this.sber_pdi = sber_pdi;
    }

    public String getOra_sau() {
        return ora_sau;
    }

    public void setOra_sau(String ora_sau) {
        this.ora_sau = ora_sau;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    //public String getOra_sau(){return ""}

}
