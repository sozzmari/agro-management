import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class User {
    private String username;
    private String passwordHash;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing error", e);
        }
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
}

class SensorData {
    private String sensorId;
    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;

    public SensorData(String sensorId, double temperature, double humidity) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = LocalDateTime.now();
    }

    public String getSensorId() { return sensorId; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Sensor [%s] | Temperature: %.1f°C | Humidity: %.1f%% | Time: %s",
                sensorId, temperature, humidity, timestamp);
    }
}

class ReportService {
    public void generateReportForPeriod(LocalDate startDate, LocalDate endDate, List<SensorData> allData) {
        long startTime = System.currentTimeMillis();

        if (endDate.isBefore(startDate)) {
            System.out.println("[Error] The end date cannot be earlier than the start date of the period.");
            return;
        }

        List<SensorData> filteredData = allData.stream()
                .filter(d -> {
                    LocalDate dataDate = d.getTimestamp().toLocalDate();
                    return !dataDate.isBefore(startDate) && !dataDate.isAfter(endDate);
                })
                .collect(Collectors.toList());

        if (filteredData.isEmpty()) {
            System.out.println("[Attention] For selected period (" + startDate + " — " + endDate + ") monitoring data is missing.");
            return;
        }

        double avgTemp = filteredData.stream().mapToDouble(SensorData::getTemperature).average().orElse(0.0);
        double avgHumidity = filteredData.stream().mapToDouble(SensorData::getHumidity).average().orElse(0.0);

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("\n[Success] Report for the period " + startDate + " by " + endDate + " successfully formed!");
        System.out.println(String.format(" - Records processed: %d", filteredData.size()));
        System.out.println(String.format(" - Average temperature: %.1f°C", avgTemp));
        System.out.println(String.format(" - Average humidity: %.1f%%", avgHumidity));
        System.out.println(" - Responce generation time: " + responseTime + " мс");
        System.out.println(" - Report file (AgroReport_" + startDate + "_to_" + endDate + ".pdf) is ready for download.");
    }
}

class AgroMonitoringSystem {
    private List<User> users = new ArrayList<>();
    private List<SensorData> sensorDataList = new ArrayList<>();
    private ReportService reportService = new ReportService();

    public AgroMonitoringSystem() {
        users.add(new User("admin", "admin0987", "ADMIN"));
    }

    public boolean login(String username, String password) {
        long startTime = System.currentTimeMillis();
        Optional<User> foundUser = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            boolean success = user.getPasswordHash().equals(new User("", password, "").getPasswordHash());
            long responseTime = System.currentTimeMillis() - startTime;
            if (responseTime > 2000) {
                System.out.println("[Attention] Response time exceeded: " + responseTime + " мс");
            }
            return success;
        }
        return false;
    }

    public void addUser(User adminUser, String newUsername, String newPassword, String role) {
        if (!"ADMIN".equals(adminUser.getRole())) {
            System.out.println("Error: Only admin can add users.");
            return;
        }
        users.add(new User(newUsername, newPassword, role));
        System.out.println("User " + newUsername + " is successfully added to the system.");
    }

    public void addSensorData(String sensorId, double temp, double humidity) {
        sensorDataList.add(new SensorData(sensorId, temp, humidity));
    }

    public void displaySensorData() {
        System.out.println("\nCurrent data from field sensors");
        if (sensorDataList.isEmpty()) {
            System.out.println("No accessible data.");
        } else {
            for (SensorData data : sensorDataList) {
                System.out.println(data);
            }
        }
    }

    public void generateReport(LocalDate start, LocalDate end) {
        reportService.generateReportForPeriod(start, end, sensorDataList);
    }
}

public class Main2 {
    public static void main(String[] args) {
        AgroMonitoringSystem system = new AgroMonitoringSystem();

        boolean isAuthSuccessful = system.login("admin", "admin123");
        System.out.println("Admin authorisation succeed: " + isAuthSuccessful);

        User admin = new User("admin", "admin0987", "ADMIN");

        system.addUser(admin, "agronomist_kaisar", "securePass456", "AGRONOMIST");

        system.addSensorData("Field_Sector_A1", 23.4, 65.0);
        system.addSensorData("Field_Sector_B2", 21.8, 70.2);

        system.displaySensorData();

        LocalDate today = LocalDate.now();
        system.generateReport(today.minusDays(1), today.plusDays(1));
    }
}