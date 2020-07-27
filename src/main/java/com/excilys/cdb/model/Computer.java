package com.excilys.cdb.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    @JoinColumn(name = "company_id")
    private Company company;

    public Computer(int id, String name, LocalDate introduced, LocalDate discontinued,
            Company company) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
    }

    public Computer() {
    }

    // TODO remove
    public Computer(int id, String name, LocalDate introduced, LocalDate discontinued,
            String companyName, int companyID) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    public static class Builder {
        private int id;
        private String name;
        private LocalDate introduced;
        private LocalDate discontinued;
        private Company company;

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIntroduced(Date introduced) {

            LocalDate introducedLocalDate = introduced == null
                    ? null
                    : Instant.ofEpochMilli(introduced.getTime()).atZone(ZoneId.systemDefault())
                            .toLocalDate();

            this.introduced = introducedLocalDate;
            return this;
        }

        public Builder withDiscontinued(Date discontinued) {

            LocalDate discontinuedLocalDate = discontinued == null
                    ? null
                    : Instant.ofEpochMilli(discontinued.getTime()).atZone(ZoneId.systemDefault())
                            .toLocalDate();
            
            this.discontinued = discontinuedLocalDate;
            return this;
        }

        public Builder withCompany(Company company) {
            this.company = company;
            return this;
        }

        public Computer build() {
            Computer computer = new Computer();
            computer.id = this.id;
            computer.name = this.name;
            computer.introduced = this.introduced;
            computer.discontinued = this.discontinued;
            computer.company = this.company;
            return computer;
        }
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

    public Company getCompany() {
        return company;
    }

    public String getCompanyName() {
        return company == null ? null : company.getName();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("id: ").append(id).append("\t");
        stringBuilder.append("name: ").append(name).append("\t");
        stringBuilder.append("introduced: ").append(introduced).append("\t");
        stringBuilder.append("discontinued: ").append(discontinued).append("\t");
        stringBuilder.append("company: ").append(company == null ? null : company.getName());

        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((company == null) ? 0 : company.hashCode());
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
        if (company == null) {
            if (other.company != null) {
                return false;
            }
        } else if (!company.equals(other.company)) {
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
