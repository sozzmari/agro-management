import java.util.List;

public class SensorService {

    public String analyzeHumidity(double humidity) {
	if (humidity < 30.0) {
		return “Суару кажет (кауыпты денгей)”;
	} else if (humidity <= 75.0) {
		return “Калыпты жагдай”;
	} else {
		return “Ылгалдылык шамадан тыс”;
	}
}

public double calculateAverageTemp(List<Double> readingsList) {
	if (readingsList == null || readingsList.isEmpty()) {
		return 0.0;
	}
	double total = 0.0;
	for (double temp : readingList) {
		total += temp;
	}
	return total / readingsList.size();
}

public boolean isValidHumidity(double humidity) {
	return humidity >= 0.0 && humidity <= 100.0;
}

}