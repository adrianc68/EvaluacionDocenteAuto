package gui.main.extensiontab;

import gui.main.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import util.classloader.ClassImport;
import util.extension.Extension;
import util.extension.TypeExtension;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExtensionTabController implements Initializable {
    private Extension selectedExtension;
    private ObservableList<Extension> extensionObservableList;
    private MainController mainController;
    @FXML private AnchorPane extensionAnchorPane;
    @FXML private TableView<Extension> extensionTableView;
    @FXML private TableColumn<Extension, Boolean> loadedExtensionTableColumn;
    @FXML private TableColumn<Extension, TypeExtension> typeExtensionTableColumn;
    @FXML private TableColumn<Extension, String> nameExtensionTableColumn;
    @FXML private TextArea loggerTextArea;
    @FXML private TextField directoryLibraryTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableView();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void addExtensionButtonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog( extensionAnchorPane.getScene().getWindow() );
        if(file != null ) {
            ClassImport classImport = new ClassImport();
            Extension extension = new Extension();
            try {
                extension.setTab( (Tab) classImport.loadJAR( file.toURI().toURL(), "getTab" ) );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            extension.setLoaded( extension.getTab() != null );
            extension.setName( file.getName() );
            extension.setType( TypeExtension.JAVA );
            extensionObservableList.add(extension);
            mainController.addExtensionToTab( extension.getTab() );
        }
    }

    @FXML
    void positionDownButtonPressed(ActionEvent event) {
        if( selectedExtension != null ) {
            int indexOfSelectedExtension = extensionObservableList.indexOf(selectedExtension);
            int indexOfBelowExtension = indexOfSelectedExtension + 1;
            if( extensionObservableList.size() -1 != extensionObservableList.indexOf(selectedExtension) ) {
                Extension extensionBelowPosition = extensionObservableList.get(indexOfBelowExtension);
                extensionObservableList.remove(extensionBelowPosition);
                extensionObservableList.remove(selectedExtension);
                extensionObservableList.add(indexOfSelectedExtension, extensionBelowPosition);
                extensionObservableList.add(indexOfBelowExtension, selectedExtension);
                selectedExtension = null;
            }
        }
    }

    @FXML
    void positionUpButtonPressed(ActionEvent event) {
        if( selectedExtension != null ) {
            int indexOfSelectedExtension = extensionObservableList.indexOf(selectedExtension);
            int indexOfAboveExtension = indexOfSelectedExtension - 1;
            if( 0 != extensionObservableList.indexOf(selectedExtension) ) {
                Extension extensionAbovePosition = extensionObservableList.get(indexOfAboveExtension);
                extensionObservableList.remove(selectedExtension);
                extensionObservableList.add(indexOfAboveExtension, selectedExtension);
                extensionObservableList.remove(extensionAbovePosition);
                extensionObservableList.add(indexOfSelectedExtension, extensionAbovePosition);
                selectedExtension = null;
            }
        }
    }

    @FXML
    void removeExtensionButtonPressed(ActionEvent event) {
        if( selectedExtension != null ) {
            extensionObservableList.remove(selectedExtension);
            mainController.removeExtentionTab( selectedExtension.getTab() );
            selectedExtension = null;
        }
    }

    @FXML
    void selectFileDirectoryButtonPressed(ActionEvent event) {

    }



    private void configureTableView() {
        loadedExtensionTableColumn.setCellValueFactory( new PropertyValueFactory<>("loaded") );
        typeExtensionTableColumn.setCellValueFactory( new PropertyValueFactory<>("type") );
        nameExtensionTableColumn.setCellValueFactory( new PropertyValueFactory<>("name") );
        extensionObservableList = FXCollections.observableArrayList();
        extensionTableView.setItems(extensionObservableList);
        setListenerToTableView();
    }

    private void setListenerToTableView() {
        extensionTableView.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if(!newValue) {
                selectedExtension = extensionTableView.getSelectionModel().getSelectedItem();
            }
        });
    }

}
