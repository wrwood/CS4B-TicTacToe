module cs4b {
    requires javafx.controls;
    requires javafx.fxml;

    opens cs4b to javafx.fxml;
    exports cs4b;
}
