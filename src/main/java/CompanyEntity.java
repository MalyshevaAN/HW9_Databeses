import java.time.LocalDate;

public record CompanyEntity(String name, String country, LocalDate creation_date, int stocks_count) {
}
