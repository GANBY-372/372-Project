package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.application.Application;

import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXDriver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader;
        Parent root;

        if (UserManager.userExists()) {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
            primaryStage.setTitle("User Login");
        } else {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateUserView.fxml"));
            primaryStage.setTitle("Create User");
        }

        root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void launchMainApp(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXDriver.class.getClassLoader().getResource("FXAPP-View.fxml"));
        Parent root = loader.load();
        stage.setTitle("Vehicle Tracking System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
