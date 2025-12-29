package presentation;
import java.lang.reflect.Array;
import java.util.*;

import business.MessageBuilder;
import data.*;

public class CaregiverDashboard {
    private String userName;
    private MessageBuilder messageBuilder;
    private UserProfile userProfile;
    private SupportRecommendation supportRecommendation;


    public CaregiverDashboard(String userName, String historyFile) {
        this.userName = userName;
        this.messageBuilder = new MessageBuilder();
        this.userProfile = new UserProfile(userName);
        this.supportRecommendation = new SupportRecommendation();
    }

    public void showTimeOfDay() {
        String timeOfDay = TimeHelper.getTimeOfDay();
        String tips = supportRecommendation.getTimeTips(timeOfDay);
        System.out.println("\n Time Based Caregiver Support Tips - " + timeOfDay.toUpperCase() + ":\n" + tips);
    }

    public void showMessageHistory() {
        //messageBuilder.viewHistory();
    }
    
    public void showCurrentMessage() {
        String currentMessage = messageBuilder.showCurrentMessage();
        System.out.println("Current Message: " + currentMessage);
    }

    public void showUserProfileSummary() {
        System.out.println("\nUser Profile Summary for " + userName + ":\n" + userProfile.getProfileSummary());
        System.out.println("Age" + userProfile.getAge());

        ArrayList<String> commMethods = userProfile.getCommunicationMethods();
        if (!commMethods.isEmpty()) {
            System.out.println("Communication Methods: " + String.join(", ", commMethods));
        } else {
            System.out.println("Communication Methods: None specified.");
        }

        ArrayList<String> sensoryPrefs = userProfile.getSensoryPref();
        if (!sensoryPrefs.isEmpty()) {
            System.out.println("Sensory Preferences: " + String.join(", ", sensoryPrefs)); 
        } else {
            System.out.println("Sensory Preferences: None specified.");
        }

        System.out.println("(Call viewMoreDetails() for full profile details)");

    }

}
