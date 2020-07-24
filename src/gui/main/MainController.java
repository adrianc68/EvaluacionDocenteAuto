package gui.main;

import gui.Controller;
import gui.main.extensiontab.ExtensionTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends Controller implements Initializable {
    @FXML private TabPane tabPane;
    @FXML private Tab principalTab;
    @FXML private Tab processesTab;
    @FXML private Tab extensionTab;
    @FXML private ExtensionTabController extensionTabController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        try {
            AnchorPane ProcessTabAnchorPane = loader.load( getClass().getResourceAsStream("/gui/main/processtab/ProcessTabVista.fxml") );
            processesTab.setContent(ProcessTabAnchorPane);
            loader = new FXMLLoader();
            AnchorPane ExtensionTabAnchorPane = loader.load( getClass().getResourceAsStream("/gui/main/extensiontab/ExtensionTabVista.fxml") );
            extensionTab.setContent(ExtensionTabAnchorPane);
            extensionTabController = loader.getController();
        } catch(IOException iex) {
            iex.printStackTrace();
        }
        extensionTabController.setMainController(this);
    }

    public void showStage() {
        loadFXMLFile( getClass().getResource( "/gui/main/MainVista.fxml" ), this );
        stage.showAndWait();
    }

    public void addExtensionToTab(Tab tab) {
        tabPane.getTabs().add(tab);
    }

    public void removeExtentionTab(Tab tab) {
        tabPane.getTabs().remove(tab);
    }

}
