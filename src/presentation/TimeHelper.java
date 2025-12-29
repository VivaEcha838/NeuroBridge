package presentation;
import java.time.LocalTime;

public class TimeHelper {

    public static String getTimeOfDay() {
        int hour = LocalTime.now().getHour();

        if (hour >= 5 && hour < 12) {
            return "morning";
        } 

        else if (hour >= 12 && hour < 17) {
            return "afternoon";
        } 

        else if (hour >= 17 && hour < 21) {
            return "evening";
        } 
        
        else {
            return "night";
        }
    }
}