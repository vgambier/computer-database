package com.excilys.cdb.dto;

public class RestDTO {

    private String searchTerm;
    private String orderBy;
    private String nbEntries;
    private String id;


    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getNbEntries() {
        return nbEntries;
    }

    public void setNbEntries(String nbEntries) {
        this.nbEntries = nbEntries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
