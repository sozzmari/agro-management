import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        return String.format("Sensor [%s] | Temperature: %.1f°C | Humidity: %.1f%% | Time: %s",
                sensorId, temperature, humidity, timestamp);
    }
}

class ReportGenerator {
    public String generateContent(List<SensorData> sensorDataList) {
        StringBuilder report = new StringBuilder();
        report.append("Agromonitoring report\n");
        report.append("Date of the generation: ").append(LocalDateTime.now()).append("\n");
        report.append("--------------------------------------------------\n");
        
        if (sensorDataList.isEmpty()) {
            report.append("No data for the selected period.\n");
        } else {
            for (SensorData data : sensorDataList) {
                report.append(data.toString()).append("\n");
            }
        }
        report.append("==================================================\n");
        return report.toString();
    }
}

interface NotificationService {
    void sendNotification(String recipient, String subject, String message);
}

class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String subject, String message) {
        System.out.println("[Email Service] Connection to the SMTP-server...");
        System.out.println("[Email Service] Receiver: " + recipient);
        System.out.println("[Email Service] Theme: " + subject);
        System.out.println("[Email Service] Sent successfully!\n");
    }
}

class FileStorageManager {
    public void saveReportToFile(String fileName, String content) {
        System.out.println("[File Storage] Saving file to a disk: " + fileName);
        System.out.println("[File Storage] Data successfully written (size: " + content.length() + " symbols).\n");
    }
}

class ReportFacade {
    private final ReportGenerator generator;
    private final FileStorageManager fileStorage;
    private final NotificationService notificationService;

    public ReportFacade(ReportGenerator generator, 
                        FileStorageManager fileStorage, 
                        NotificationService notificationService) {
        this.generator = generator;
        this.fileStorage = fileStorage;
        this.notificationService = notificationService;
    }

    public void processAndDistributeReport(List<SensorData> data, String fileName, String emailRecipient) {
        String reportContent = generator.generateContent(data);

        fileStorage.saveReportToFile(fileName, reportContent);

        notificationService.sendNotification(
                emailRecipient, 
                "Daily agromonitoring report", 
                reportContent
        );
    }
}

public class Main3 {
    public static void main(String[] args) {
        List<SensorData> mockData = new ArrayList<>();
        mockData.add(new SensorData("Field_A1", 24.5, 62.0));
        mockData.add(new SensorData("Field_B2", 22.1, 68.5));

        ReportGenerator generator = new ReportGenerator();
        FileStorageManager storageManager = new FileStorageManager();
        NotificationService emailService = new EmailNotificationService(); 

        ReportFacade reportFacade = new ReportFacade(generator, storageManager, emailService);

        System.out.println("Initiating the report generation process...\n");
        reportFacade.processAndDistributeReport(
                mockData, 
                "AgroReport_2026-11-11.txt", 
                "agronomist@farm.com"
        );
    }
}