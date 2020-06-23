package model;

public class Company {

    private int id;
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("id: ").append(id).append("\t");
        stringBuilder.append("name: ").append(name);

        return stringBuilder.toString();
    }

}
