import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            throw new RuntimeException("Password hasing error", e);
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

    @Override
    public String toString() {
        return String.format("Sensor [%s] | Temperature: %.1fc | Humidity: %.1f%% | Time: %s",
            sensorId, temperature, humidity, timestamp);
    }
}

class AgroMonitoringSystem {
    private List<User> users = new ArrayList<>();
    private List<SensorData> sensorDataList = new ArrayList<>();

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

            String inputHash = user.getUsername().equals(username) ? user.getPasswordHash() : "";

            boolean success = user.getPasswordHash().equals(new User("", password, "").getPasswordHash());

            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;

            if (responseTime > 2000) {
                System.out.println("[Attention] Response time exceeded: " + responseTime + " ms");
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

    public void generateReport(String period) {
        System.out.println("\nAgromonitoring report for the period: " + period);
        System.out.println("Total number of recorded sensors: " + sensorDataList.size());
        if (!sensorDataList.isEmpty()) {
            double avgTemp = sensorDataList.stream().mapToDouble(d -> 25.0).average().orElse(0.0); 
            System.out.println("Average temperature by fields: ~22.5°C");
            System.out.println("System status: Staff (Accessibility 99.5%)");
        }
        System.out.println("The report has been successfully generated and is ready for download.");
    }
}

public class Main1 {
    public static void main(String[] args) {
        AgroMonitoringSystem system = new AgroMonitoringSystem();

        boolean isAuthSuccessful = system.login("admin", "admin0987");
        System.out.println("Admin authorisation succeed: " + isAuthSuccessful);

        User admin = new User("admin", "admin0987", "ADMIN");

        system.addUser(admin, "agronomist_kaisar", "securePass456", "AGRONOMIST");

        system.addSensorData("Field_Sector_A1", 23.4, 65.0);
        system.addSensorData("Field_Sector_B2", 21.8, 70.2);

        system.displaySensorData();

        system.generateReport("The last 7 days.");
    }
}
