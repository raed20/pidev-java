package controllers;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RssFeedTicker extends Application {

    private static final String[] NEWS_FEED = {
            "Bank of America announces new interest rates.",
            "JP Morgan Chase reports record profits.",
            "Federal Reserve announces new monetary policy.",
            "Wells Fargo introduces new mobile banking app."
    };

    private int newsIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        Label newsLabel = new Label();
        newsLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 5px;");
        newsLabel.setMinWidth(800); // Set the width to match your scene width
        newsLabel.setText(NEWS_FEED[newsIndex]);

        HBox root = new HBox(newsLabel);
        Scene scene = new Scene(root, 800, 100);

        primaryStage.setTitle("Banking News Ticker");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Timeline to update the news ticker
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    newsIndex = (newsIndex + 1) % NEWS_FEED.length;
                    newsLabel.setText(NEWS_FEED[newsIndex]);
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Animation to scroll the news ticker
        TranslateTransition tickerTransition = new TranslateTransition(Duration.seconds(20), newsLabel);
        tickerTransition.setFromX(scene.getWidth());
        tickerTransition.setToX(-newsLabel.getWidth());
        tickerTransition.setInterpolator(Interpolator.LINEAR);
        tickerTransition.setCycleCount(Timeline.INDEFINITE);
        tickerTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
