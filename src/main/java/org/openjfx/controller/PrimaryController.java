package org.openjfx.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import org.openjfx.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
