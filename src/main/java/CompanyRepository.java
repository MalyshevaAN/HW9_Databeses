import java.sql.*;
import java.util.Map;
import java.util.Optional;

public class CompanyRepository {
    Map<String, String> env = System.getenv();
    String POSTGRES_PASSWORD = env.get("POSTGRES_PASSWORD");
    String POSTGRES_USER = env.get("POSTGRES_USER");
    public CompanyRepository(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String createDB = """
                    CREATE TABLE IF NOT EXISTS company(
                    id SERIAL PRIMARY KEY,
                    name TEXT ,
                    country TEXT,
                    creation_date DATE,
                    stocks_count BIGINT
                    );
                    """;
            try(PreparedStatement preparedStatement = con.prepareStatement(createDB)){
                preparedStatement.execute();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void create(CompanyEntity company){
        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String createEntity = """
                    INSERT INTO COMPANY (name, country, creation_date, stocks_count)
                    VALUES (?,?,?,?);
                    """;
            try (PreparedStatement preparedStat = con.prepareStatement(createEntity)){
                preparedStat.setString(1,company.name());
                preparedStat.setString(2,company.country());
                preparedStat.setDate(3, Date.valueOf(company.creation_date()));
                preparedStat.setInt(4,company.stocks_count());
                preparedStat.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public Optional<CompanyEntity> read(int id){
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String readEntity = """
                    SELECT * FROM  company WHERE id = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(readEntity)){
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    resultSet.next();
                    CompanyEntity company = new CompanyEntity(resultSet.getString("name"), resultSet.getString("country"), resultSet.getDate("creation_date").toLocalDate(), resultSet.getInt("stocks_count"));
                    return Optional.of(company);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public void update (int id, CompanyEntity company){
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)) {
            String updateEntity = """
                    UPDATE company 
                    SET name = ?, country = ?, creation_date = ?, stocks_count = ?
                    WHERE id = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(updateEntity)) {
                preparedStatement.setString(1, company.name());
                preparedStatement.setString(2, company.country());
                preparedStatement.setDate(3, Date.valueOf(company.creation_date()));
                preparedStatement.setInt(4, company.stocks_count());
                preparedStatement.setInt(5, id);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }

    public void delete(CompanyEntity company){
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String deleteEntity = """
                    DELETE FROM company WHERE name = ? AND country = ? AND creation_date = ? AND stocks_count = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(deleteEntity)){
                preparedStatement.setString(1, company.name());
                preparedStatement.setString(2, company.country());
                preparedStatement.setDate(3, Date.valueOf(company.creation_date()));
                preparedStatement.setInt(4, company.stocks_count());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
