package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import top.jalva.jalvafx.node.ComboBoxCustomizer;

import java.util.List;

public class DialogController {

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private ComboBox<Ingredient> pickIngredientComboBox;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;


    public void setUpComboBox() {
       // pickIngredientComboBox.getItems().addAll(Datasource.getInstance().queryIngredients());
        List<Ingredient> ingredients = Datasource.getInstance().queryIngredients();
        ComboBoxCustomizer.create(pickIngredientComboBox).autocompleted(ingredients).customize();
    }

}
