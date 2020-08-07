package com.excilys.cdb.dto;

/**
 * @author Victor Gambier
 *
 */
public class CompanyDTO {

    private String id;
    private String name;

    public static class Builder {
        private String id;
        private String name;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public CompanyDTO build() {
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.id = this.id;
            companyDTO.name = this.name;
            return companyDTO;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
