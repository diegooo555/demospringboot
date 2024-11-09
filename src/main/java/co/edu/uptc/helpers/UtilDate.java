package co.edu.uptc.helpers;

import java.time.LocalDate;
import java.time.Period;

public class UtilDate {
     public static int calculateAge(LocalDate birthday) {
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(birthday, hoy);
        return periodo.getYears();
    }
}
