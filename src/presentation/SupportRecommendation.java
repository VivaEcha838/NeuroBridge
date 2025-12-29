package presentation;
import java.util.*;

public class SupportRecommendation {

    private Map<String, ArrayList<String>> supportMap;

    public SupportRecommendation() {
        supportMap = new HashMap<>();
        loadRecommendations();
    }

    private void loadRecommendations() {
        ArrayList<String> morningRecs = new ArrayList<>();

        morningRecs.add("Sensory: Start the morning off with dim, calm lighting and a soft greeting. Avoid anything loud early in the day.");
        morningRecs.add("Emotional: Use a visual schedule to help child feel more calm and to show the process of getting ready.");
        morningRecs.add("Behavioral: Offer simple choices to reduce resistance and increase involvement");
        morningRecs.add("Cognitive: Break tasks into small, baby steps, using persistent verbal cues/reminders throughout the process.");

        ArrayList<String> afternoonRecs = new ArrayList<>();

        afternoonRecs.add("Sensory: Allow decompresison after school with play time or sensory toy.");
        afternoonRecs.add("Emotional: Let child relax and play before asking about school day or giving tasks.");
        afternoonRecs.add("Behavioral: Follow a routine after school, such as break --> homework --> play.");
        afternoonRecs.add("Cognitive: Use visual timers and intermittent breaks to support more focus during homework time.");

        ArrayList<String> eveningRecs = new ArrayList<>();

        eveningRecs.add("Sensory: Allow decompresison after school with play time or sensory toy.");
        eveningRecs.add("Emotional: Let child relax and play before asking about school day or giving tasks.");
        eveningRecs.add("Behavioral: Follow a routine after school, such as break --> homework --> play.");
        eveningRecs.add("Cognitive: Use visual timers and intermittent breaks to support more focus during homework time.");

        ArrayList<String> nightRecs = new ArrayList<>();

        nightRecs.add("Sensory: Allow decompresison after school with play time or sensory toy.");
        nightRecs.add("Emotional: Let child relax and play before asking about school day or giving tasks.");
        nightRecs.add("Behavioral: Follow a routine after school, such as break --> homework --> play.");
        nightRecs.add("Cognitive: Use visual timers and intermittent breaks to support more focus during homework time.");

        supportMap.put("morning", morningRecs);
        supportMap.put("afternoon", afternoonRecs);
        supportMap.put("evening", eveningRecs);
        supportMap.put("night", nightRecs);

    }


    public ArrayList<String> getTips(String timeOfDay) {
        String time = timeOfDay.toLowerCase();

        if (supportMap.containsKey(time)) {
            return supportMap.get(time);
        }
        else {
            return new ArrayList<String>();
        }
    }

    public String getTimeTips(String timeOfDay) {
        ArrayList<String> tips = getTips(timeOfDay);
        String result = "Caregiver Support Tips for " + timeOfDay.toUpperCase();

        for (int i = 0; i < tips.size(); i++) {
            result += (i + 1) + ". " + tips.get(i) + "\n";
        }

        return result;
    }



    public static void main(String[] args) {
        SupportRecommendation engine = new SupportRecommendation();

        System.out.println(engine.getTimeTips("morning"));
        System.out.println(engine.getTimeTips("afternoon"));
        System.out.println(engine.getTimeTips("evening"));
        System.out.println(engine.getTimeTips("night"));
    }
}