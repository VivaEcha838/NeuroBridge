package presentation;

import business.MessageBuilderNew;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;


public class MessageHistoryWindow {
    private MessageBuilderNew messageBuilder;
    private Stage historyStage;
    private ListView<String> historyListView;
    private Label counterLabel;

    public MessageHistoryWindow(MessageBuilderNew messageBuilder) {
        this.messageBuilder = messageBuilder;
        //createWindow();
    }
}
