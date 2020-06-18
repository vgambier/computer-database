package model;

import java.time.LocalDate;

public class Computer {

	private int id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private int companyID;

	public Computer(int id, String name, LocalDate introduced, LocalDate discontinued, int companyID) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyID = companyID;
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
