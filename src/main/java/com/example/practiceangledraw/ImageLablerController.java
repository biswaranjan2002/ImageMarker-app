package com.example.practiceangledraw;

import com.example.practiceangledraw.AngleDrawer.AngleDrawer;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;

public class ImageLablerController {

    //=======================
    //-----initializing panes and angledrawer
    @FXML
    public Pane canvasPane;
    private AngleDrawer drawer;

    //=============================================
    //------------------enable button
    @FXML
    private Button enableBtn;
    @FXML
    private Button clearBtn;


    @FXML
    private void handleEnableButton() {
        if (drawer != null) {
            boolean currentlyDisabled = drawer.isDisable(); // check status
            drawer.setDisable(!currentlyDisabled); // toggle status
            enableBtn.setText(currentlyDisabled ? "Disable" : "Enable"); // toggle label
        }
    }

    //===================================================
    //---------------clear button

    @FXML private Button hideBtn;
    @FXML
    public void handleHideAllButton() {
        if (drawer != null) {
            if (drawer.isCurrentlyHidden()) {
                drawer.showAll();
                hideBtn.setText("Hide All");
            } else {
                drawer.hideAll();
                hideBtn.setText("Unhide All");
            }
        }
    }


    @FXML
    public void handleClearButton() {
        if (drawer != null) {
            drawer.clearAll();
        }
    }

    //===============================================================
    //-------------open button
    @FXML private ImageView imageView;
    @FXML private Button openBtn;

    @FXML
    private void handleOpenButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        // Set filter: allow only image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"
        );
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show open dialog
        File selectedFile = fileChooser.showOpenDialog(canvasPane.getScene().getWindow());

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            double canvasWidth = canvasPane.getWidth();
            double canvasHeight = canvasPane.getHeight();

            double imgWidth = image.getWidth();
            double imgHeight = image.getHeight();

            double scale = Math.min(canvasWidth / imgWidth, canvasHeight / imgHeight);
            double fitWidth = imgWidth * scale;
            double fitHeight = imgHeight * scale;


            // Center the image
            imageView.setLayoutX((canvasWidth - fitWidth) / 2);
            imageView.setLayoutY((canvasHeight - fitHeight) / 2);
        }
    }

    //===================================================
    //---------------------------------select button

    @FXML
    private Button selectBtn;
    private boolean selectionMode = false;

    @FXML
    private void handleSelectButton() {
        selectionMode = !selectionMode;

        drawer.setSelectionMode(selectionMode);  // 👈 AngleDrawer ke setter se toggle karo

        if (selectionMode) {
            selectBtn.setText("Select Off");  // ✅ Corrected
        } else {
            selectBtn.setText("Select");            // ✅ Corrected
            drawer.deselectAll();
        }
    }

//=====================================================================
    //------------------------Delete Button

    @FXML
    private Button deleteBtn;

    @FXML
    private void handleDeleteButton() {
        drawer.deleteSelectedAngles();
    }




    @FXML
    public void initialize() {
        if (imageView == null || canvasPane == null) {
            System.out.println("FXML loading issue: imageView or canvasPane is null");
            return;
        }

        drawer = new AngleDrawer(imageView);
        canvasPane.getChildren().add(drawer);   // 🟢 Add to canvas
        drawer.setDisable(true);

        enableBtn.setCursor(Cursor.HAND);
        hideBtn.setCursor(Cursor.HAND);
        selectBtn.setCursor(Cursor.HAND);
        deleteBtn.setCursor(Cursor.HAND);
        openBtn.setCursor(Cursor.HAND);
        clearBtn.setCursor(Cursor.HAND);

        setupButtonHoverEffects(enableBtn);
        setupButtonHoverEffects(openBtn);
        setupButtonHoverEffects(selectBtn);
        setupButtonHoverEffects(deleteBtn);
        setupButtonHoverEffects(clearBtn);
        setupButtonHoverEffects(hideBtn);
    }

    private void setupButtonHoverEffects(Button button) {
        // Set cursor to HAND on hover
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle("-fx-background-color: #F57C00; -fx-text-fill: black; -fx-background-radius: 30;");
        });

        // Restore on exit
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #eb0e9a; -fx-text-fill: white; -fx-background-radius: 30;");
        });
    }
}