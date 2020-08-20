package com.excilys.cdb.model;


public class Page {

    private final int nbEntry;
    private int pageLength;
    private int nbPage;
    private int currentPage;

    private String attributeToOrder;
    private String search = "";
    private Boolean ascendingOrder = true;


    public Page(int nbEntry , String attributeToOrder) {
        this(nbEntry,25,attributeToOrder);
    }

    public Page(int nbEntry,int pageLength,String attributeToOrder) {
        this.nbEntry = nbEntry;
        this.pageLength = pageLength;
        this.nbPage = (nbEntry / pageLength) + 1;
        this.currentPage = 1;
        this.attributeToOrder = attributeToOrder;
    }


    public int getPageLength () {
        return pageLength;
    }

    public void setPageLength (int pageLength) {
        this.pageLength = pageLength;
        this.nbPage = ( nbEntry / pageLength ) + 1;
        if ( (currentPage - 1) * pageLength > nbEntry) {
            this.currentPage = this.nbPage;
        }

    }

    public void setPageLength (String pageLength){
        try {
            setPageLength(Integer.parseInt(pageLength));
        }catch (NumberFormatException ignored){}
    }


    public int getNbPage () {
        return nbPage;
    }

    public void setAttributeToOrder(String attributeToOrder) {
        this.attributeToOrder = attributeToOrder;
    }

    public String getAttributeToOrder() {
        return this.attributeToOrder;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        if (search != null) {
            this.search = search;
        }

    }

    public void setAscendingOrder(String ascendingOrder) {
        this.ascendingOrder = !"DESC".equalsIgnoreCase(ascendingOrder);
    }

    public Boolean isAscendingOrder() {
        return ascendingOrder;
    }


    public int getOffset() {
        return (currentPage - 1) * pageLength;
    }

    public String getCurrentOrder () {
        return isAscendingOrder() ? "ASC" : "DESC";
    }

    public void goTo (int pageNumber) {
        if(pageNumber > nbPage ) {
            currentPage = nbPage;
        }else currentPage = Math.max(pageNumber, 1);
    }

    public void goTo (String pageNumber){
        try{
            goTo((Integer.parseInt(pageNumber)));
        }catch (NumberFormatException ignored){}
    }


}
