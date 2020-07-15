package model;

import java.io.Serializable;

/**
 * @author Victor Gambier
 *
 */
public class Company implements Serializable {

    private static final long serialVersionUID = 1474235254628858107L;
    private int id;
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
