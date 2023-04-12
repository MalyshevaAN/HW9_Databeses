import java.time.LocalDate;
import java.util.Optional;


public class TestDB {
    public static void main(String[] args) {
        CompanyRepository newDBCompanies = new CompanyRepository();
        CompanyEntity company = new CompanyEntity("son", "Russia", LocalDate.of(2023, 1, 1), 1000);
        //newDBCompanies.create(company);
        StockRepository newDBStocks = new StockRepository();
        StockEntity stock = new StockEntity("hello", 1000, 1);
        //newDBStocks.create(stock);
        //newDBStocks.create(stock);
        //newDBStocks.delete(stock);
        StockEntity stock1 = new StockEntity("hi", 10, 1);
        newDBStocks.update(3, stock1);
        Optional<StockEntity> stock2 = newDBStocks.read(4);
        stock2.ifPresent(stockEntity -> System.out.println(stockEntity.name()));

        Optional<CompanyEntity> company1 = newDBCompanies.read(1);
        company1.ifPresent(companyEntity -> System.out.println(companyEntity.name()));
        CompanyEntity company2 = new CompanyEntity("somnus", "International", LocalDate.of(2023, 1, 11), 10000);
        newDBCompanies.update(1,company2);
        newDBCompanies.delete(company);
    }
}
