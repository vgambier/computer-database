package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Company;

/* This class uses the Singleton pattern */

public class CompanyMapper extends Mapper<Company> {

	private static CompanyMapper INSTANCE = null;

	private CompanyMapper() {
	}

	public static CompanyMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CompanyMapper();
		}
		return INSTANCE;
	}

	@Override
	public Company toModel(ResultSet rs) throws SQLException, MapperException {
		// TODO: Auto-generated method stub - currently not needed
		return null;
	}

	@Override
	public List<Company> toModelList(ResultSet rs) throws SQLException {

		List<Company> companies = new ArrayList<Company>();

		while (rs.next())
			companies.add(new Company(rs.getInt("id"), rs.getString("name")));

		return companies;
	}
}
