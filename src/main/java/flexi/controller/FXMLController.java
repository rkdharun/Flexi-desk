package flexi.controller;
/*
Put header here


 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import flexi.networkio.Sender;
public class FXMLController implements Initializable {
    
    @FXML
    private Label lblOut;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        lblOut.setText("Hello World!");
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
