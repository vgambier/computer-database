package dto;

/**
 * @author Victor Gambier
 *
 */
public class CompanyDTO {

    private String id;
    private String name;

    public CompanyDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
