package presentation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class SupportRecommendation {
    private final Map<String, ArrayList<String>> scenariosMap;

    public SupportRecommendation() {
        scenariosMap = new HashMap<>();
        loadScenarios();
    }

    private void loadScenarios() {
        scenariosMap.put("morning", new ArrayList<>(List.of(
            "Use visual schedules to outline the morning routine.",
            "Incorporate calming sensory activities, such as deep pressure or weighted blankets.",
            "Provide clear and concise instructions for each task.",
            "Allow extra time for transitions between activities."
        )));
        scenariosMap.put("after_school", new ArrayList<>(List.of(
            "Create a quiet space for decompression after school.",
            "Use a visual timer to indicate when the transition will occur.",
            "Offer a choice of calming activities, such as listening to music or drawing.",
            "Communicate expectations clearly and positively."
        )));
        scenariosMap.put("focus_time", new ArrayList<>(List.of(
            "Minimize distractions in the environment.",
            "Use noise-cancelling headphones if needed.",
            "Incorporate movement breaks to help maintain focus.",
            "Provide clear, step-by-step instructions for tasks."
        )));
        scenariosMap.put("transitions", new ArrayList<>(List.of(
            "Use visual timers to prepare for upcoming transitions.",
            "Provide verbal warnings before transitions occur.",
            "Incorporate preferred sensory activities during transitions.",
            "Maintain a consistent routine to reduce anxiety."
        )));
        scenariosMap.put("overload", new ArrayList<>(List.of(
            "Create a safe, quiet space for retreating from overwhelming stimuli.",
            "Use calming sensory tools, such as fidget toys or weighted blankets.",
            "Encourage deep breathing exercises to reduce stress.",
            "Limit exposure to triggering environments when possible."
        )));
        scenariosMap.put("sleep", new ArrayList<>(List.of(
            "Establish a consistent bedtime routine with calming activities.",
            "Use dim lighting and reduce noise in the sleep environment.",
            "Incorporate sensory tools, such as white noise machines or weighted blankets.",
            "Avoid stimulating activities before bedtime."
        )));
    }

    public String scenarioToKey(String scenario) {
        switch (scenario.toLowerCase()) {
            case "morning routine":
                return "morning";
            case "after school transition":
                return "after_school";
            case "focus time":
                return "focus_time";
            case "transitions":
                return "transitions";
            case "overload":
                return "overload";
            case "wind down and sleep":
                return "sleep";
            default:
                return "";
        }
    }

    public List<String> getScenarioTips(String key) {
        ArrayList<String> tips = scenariosMap.get(key);
        if (tips == null) {
            return new ArrayList<>();
        }
        return tips;
    }
}