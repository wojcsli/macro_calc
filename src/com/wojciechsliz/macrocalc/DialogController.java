package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.TextFields;

public class DialogController {

    @FXML
    private TextField weightField;

    @FXML
    private ComboBox<Ingredient> pickIngredientComboBox;



    public void setUpComboBox() {
        pickIngredientComboBox.getItems().addAll(Datasource.getInstance().queryIngredients());
        pickIngredientComboBox.setEditable(true);
        TextFields.bindAutoCompletion(pickIngredientComboBox.getEditor(), pickIngredientComboBox.getItems());
    }

    public Ingredient processInput() {
        Ingredient ingredient = pickIngredientComboBox.getSelectionModel().getSelectedItem();
        System.out.println(pickIngredientComboBox.getSelectionModel().getSelectedItem().getName());
        ingredient.setWeight(Integer.parseInt(weightField.getText()));
        return ingredient;

    }

}
