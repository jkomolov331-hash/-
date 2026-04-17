module com.vosiq.edugames {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    opens com.vosiq.edugames to javafx.fxml;
    opens com.vosiq.edugames.view to javafx.fxml;
    opens com.vosiq.edugames.game to javafx.fxml;
    opens com.vosiq.edugames.model to javafx.fxml, com.google.gson;
    opens com.vosiq.edugames.data to javafx.fxml;
    opens com.vosiq.edugames.util to javafx.fxml;

    exports com.vosiq.edugames;
    exports com.vosiq.edugames.view;
    exports com.vosiq.edugames.game;
    exports com.vosiq.edugames.model;
    exports com.vosiq.edugames.data;
    exports com.vosiq.edugames.util;
}
