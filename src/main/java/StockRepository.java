import java.sql.*;
import java.util.Map;
import java.util.Optional;

public class StockRepository {
    Map<String, String> env = System.getenv();
    String POSTGRES_PASSWORD = env.get("POSTGRES_PASSWORD");
    String POSTGRES_USER = env.get("POSTGRES_USER");
    public StockRepository(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String createDB = """
                    CREATE TABLE IF NOT EXISTS stock(
                    id SERIAL PRIMARY KEY,
                    name TEXT ,
                    cost BIGINT,
                    company_id BIGINT,
                    FOREIGN KEY(company_id) REFERENCES company(id) ON DELETE CASCADE
                    );
                    """;
            try(PreparedStatement preparedStatement = con.prepareStatement(createDB)){
                preparedStatement.execute();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void create(StockEntity stock){
        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String createEntity = """
                    INSERT INTO stock (name, cost, company_id)
                    VALUES (?,?,?);
                    """;
            try (PreparedStatement preparedStat = con.prepareStatement(createEntity)){
                preparedStat.setString(1,stock.name());
                preparedStat.setInt(2,stock.cost());
                preparedStat.setInt(3,stock.company_id());
                preparedStat.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public Optional<StockEntity> read(int id){
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try(Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String readEntity = """
                    SELECT * FROM  stock WHERE id = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(readEntity)){
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    resultSet.next();
                    StockEntity stock = new StockEntity(resultSet.getString("name"), resultSet.getInt("cost"), resultSet.getInt("company_id"));
                    return Optional.of(stock);
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public void update (int id, StockEntity stock){
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)) {
            String updateEntity = """
                    UPDATE stock 
                    SET name = ?, cost = ?, company_id = ?
                    WHERE id = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(updateEntity)) {
                preparedStatement.setString(1, stock.name());
                preparedStatement.setInt(2, stock.cost());
                preparedStatement.setInt(3, stock.company_id());
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }

    public void delete(StockEntity stock){
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:7432/stocks", POSTGRES_USER, POSTGRES_PASSWORD)){
            String deleteEntity = """
                    DELETE FROM stock WHERE name = ? AND cost = ? AND company_id = ?;
                    """;
            try (PreparedStatement preparedStatement = con.prepareStatement(deleteEntity)){
                preparedStatement.setString(1, stock.name());
                preparedStatement.setInt(2, stock.cost());
                preparedStatement.setInt(3, stock.company_id());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
