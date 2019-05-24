/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DHCP;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author HP
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button Revocarbtn;
    @FXML
    private TextField iptxt;
    @FXML
    private TextArea Registrotxt;

    private DHCPServer server;
    private ArrayList<String> registro = new ArrayList<>();
    @FXML
    private AnchorPane Pane;
    @FXML
    private Button Comenzarbtn;
    @FXML
    private Label Avisolbl;
    
    

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.Registrotxt.setEditable(false);
        this.Revocarbtn.setVisible(false);
        this.Avisolbl.setVisible(false);
        this.iptxt.setVisible(false);
    }

    @FXML
    private void OnClickRevocar(ActionEvent event) {
        try {
            this.server.revocarIP(this.iptxt.getText());
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(String msg) {
        this.Registrotxt.setText(this.Registrotxt.getText() + "\n" + msg);

    }

    @FXML
    private void OnClickComenzar(ActionEvent event) {
        try {
            this.server = new DHCPServer(this);
            this.Revocarbtn.setVisible(true);
            this.Avisolbl.setVisible(true);
            this.iptxt.setVisible(true);
            this.server.start();
            
        } catch (Exception ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
