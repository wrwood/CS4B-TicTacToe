module cs4b {
    requires javafx.controls;
    requires javafx.fxml;

    opens cs4b to javafx.fxml;
    exports cs4b;
    exports cs4b.Controller;
    opens cs4b.Controller to javafx.fxml;
}
