package com.excilys.cdb.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Victor Gambier
 *
 */
@Entity
@Table(name = "computer")
public class Computer implements Serializable {

    private static final long serialVersionUID = -772706640304719028L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "introduced")
    private LocalDate introduced;

    @Column(name = "discontinued")
    private LocalDate discontinued;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    // TODO: improve model? 3 separate company attributes
    @Transient
    private int companyID;
    @Transient
    private String companyName;

    public Computer(int id, String name, LocalDate introduced, LocalDate discontinued,
            String companyName, int companyID) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.companyName = companyName;
        this.companyID = companyID;
    }

    public Computer() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getIntroduced() {
        return introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCompanyID() {
        return companyID;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("id: ").append(id).append("\t");
        stringBuilder.append("name: ").append(name).append("\t");
        stringBuilder.append("introduced: ").append(introduced).append("\t");
        stringBuilder.append("discontinued: ").append(discontinued).append("\t");
        stringBuilder.append("company: ").append(companyName);

        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
        result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
        result = prime * result + id;
        result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Computer other = (Computer) obj;
        if (companyName == null) {
            if (other.companyName != null) {
                return false;
            }
        } else if (!companyName.equals(other.companyName)) {
            return false;
        }
        if (discontinued == null) {
            if (other.discontinued != null) {
                return false;
            }
        } else if (!discontinued.equals(other.discontinued)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (introduced == null) {
            if (other.introduced != null) {
                return false;
            }
        } else if (!introduced.equals(other.introduced)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
