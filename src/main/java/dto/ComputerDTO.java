package dto;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ComputerDTO implements Serializable {

    private static final long serialVersionUID = 991867017251654576L;
    private String id;
    private String name;
    private String introduced;
    private String discontinued;
    private String companyID;
    private String companyName;

    public static class Builder {
        private String id;
        private String name;
        private String introduced;
        private String discontinued;
        private String companyID;
        private String companyName;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withIntroDate(String introduced) {
            this.introduced = introduced;
            return this;
        }

        public Builder withDiscDate(String discontinued) {
            this.discontinued = discontinued;
            return this;
        }

        public Builder withCompanyId(String companyID) {
            this.companyID = companyID;
            return this;
        }

        public Builder withCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public ComputerDTO build() {
            ComputerDTO computerDTO = new ComputerDTO();
            computerDTO.id = this.id;
            computerDTO.name = this.name;
            computerDTO.introduced = this.introduced;
            computerDTO.discontinued = this.discontinued;
            computerDTO.companyID = this.companyID;
            computerDTO.companyName = this.companyName;
            return computerDTO;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduced() {
        return introduced;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    // Spring lifecycle

    @PostConstruct
    public void customInit() {
        System.out.println("Method customInit() invoked...");
    }

    @PreDestroy
    public void customDestroy() {
        System.out.println("Method customDestroy() invoked...");
    }

}
