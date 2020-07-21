package com.excilys.cdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Victor Gambier
 *
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1474235254628858107L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Company() {
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("id: ").append(id).append("\t");
        stringBuilder.append("name: ").append(name);

        return stringBuilder.toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
