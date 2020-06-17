package model;

import java.sql.Date;

public class Computer {

	private int id;
	private String name;
	private Date introduced;
	private Date discontinued;
	private int companyID; // TODO: should this become Company company?

	public Computer(int id, String name, Date introduced, Date discontinued, int companyID) {

		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyID = companyID;
	}

	public Computer(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Computer() {
	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("id: ").append(id).append("\t");
		stringBuilder.append("name: ").append(name).append("\t");
		stringBuilder.append("introduced: ").append(introduced).append("\t");
		stringBuilder.append("discontinued: ").append(discontinued).append("\t");
		stringBuilder.append("companyID: ").append(companyID);

		return stringBuilder.toString();
	}

}
