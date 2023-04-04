package API;

import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;

public class JDBCImplement {
    public DataSource dataSource(){

        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setUser("postgres");
        pgSimpleDataSource.setPassword("12345");
        pgSimpleDataSource.setDatabaseName("postgres");
       return pgSimpleDataSource;
    }
}
