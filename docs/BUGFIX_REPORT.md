Қате түрі	Сипаттамасы	Қай кезеңде анықталады?	agro-management-system (Java) мысалы
1. Компиляция қатесі (Compile-time / Syntax Error)	Java синтаксисінің немесе типтер сәйкестігінің бұзылуы (нүктелі үтір қалуы, String орнына double беру).	Кодты жазу немесе javac компиляциялау кезінде (IDE бірден қызылмен көрсетеді).	if (humidity < 30) соңында ашылатын фигуралық жақшаның { немесе ; белгісінің қалып кетуі.
2. Орындалу уақытындағы қате (Runtime Exception)	Компиляция сәтті өтеді, бірақ бағдарлама жұмыс істеп тұрғанда күтпеген жағдайдан тоқтап қалады (Exception).	Бағдарламаны іске қосқанда (тестілеу немесе қолдану кезінде).	NullPointerException (жоқ объектіге жүгіну) немесе IndexOutOfBoundsException / нөлге бөлу.
3. Логикалық қате (Logic Error)	Бағдарлама құламайды және қате хабарлама бермейді, бірақ есептеу немесе шешім қабылдау нәтижесі қате шығады.	Тек тестілік сценарийлер (Test Cases) мен шекаралық мәндерді тексергенде.	humidity = 29.9 болғанда «Суару қажет» орнына «Қалыпты жағдай» деп қайтаруы.


//fixed code

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

Тест ID	Тексерілетін әдіс және сценарий	Кіріс дерегі	Түзетуге дейінгі нәтиже	Түзетуден кейінгі нәтиже	Статус (PASS / FAIL)
REG-01	analyzeHumidity (BUG-001 тексерісі)	29.9	"Қалыпты жағдай" (Қате)	"Суару қажет (Қауіпті деңгей)"	PASS
REG-02	analyzeHumidity (Көршілес шекара)	30.0	"Қалыпты жағдай"	"Қалыпты жағдай"	PASS
REG-03	calculateAverageTemp (BUG-002 тексерісі)	List.of() (бос)	Double.NaN (Қате мән)	0.0	PASS
REG-04	isValidHumidity (BUG-003 тексерісі)	150.0	true (Қате)	false	PASS
