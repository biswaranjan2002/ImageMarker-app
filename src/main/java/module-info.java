module com.example.practiceangledraw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.practiceangledraw to javafx.fxml;
    exports com.example.practiceangledraw;
}