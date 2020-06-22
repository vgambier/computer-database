package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// The type T should be a class from model, e.g.: Computer, Company, etc.

public abstract class Mapper<T> {

    /**
     * @param rs
     *            a ResultSet object, that should come from a query on a table.
     *            Only the first item of the query will be considered.
     * @return an object that modelizes the entry from the database: for
     *         example, a Computer object or a Company object
     * @throws MapperException
     *             - if the input ResultSet had 0 entries
     * @throws SQLException
     */
    public abstract T toModel(ResultSet rs)
            throws SQLException, MapperException;

    /**
     * @param rs
     *            a ResultSet object, that should come from a query on a table.
     * @return a List<T> object that modelizes many entries from the database.
     *         Possibly empty if the ResultSet had 0 entries.
     * @throws SQLException
     */
    public abstract List<T> toModelList(ResultSet rs) throws SQLException;

}
