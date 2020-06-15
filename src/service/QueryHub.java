package service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHub {

	public static void listComputers(DatabaseConnection dbConnection) {

		String sql = "SELECT * FROM `computer`";

		try {

			PreparedStatement statement = dbConnection.connect()
					.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				String id = resultSet.getString("id");
				String name = resultSet.getString("name");
				System.out.println(id + " " + name);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConnection.disconnect();
		}
	}

}
