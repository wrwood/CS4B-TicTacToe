module cs4b {
    requires javafx.controls;
    requires javafx.fxml;

    opens Game to javafx.fxml;
    opens GameServer to javafx.fxml;
    exports Game;
    exports Game.Controller;
    exports GameServer;
    exports GameServer.Controller;
    opens Game.Controller to javafx.fxml;
    opens GameServer.Controller to javafx.fxml;
    exports Game.Controller.Board;
    opens Game.Controller.Board to javafx.fxml;
}
