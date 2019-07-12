package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;

public class DialogController {

    @FXML
    private TextField weightField;

    @FXML
    private ComboBox pickIngredientComboBox;



    public void setUpComboBox() {
        pickIngredientComboBox.getItems().addAll(Datasource.getInstance().queryIngredients());
        pickIngredientComboBox.setEditable(true);
        TextFields.bindAutoCompletion(pickIngredientComboBox.getEditor(), pickIngredientComboBox.getItems());
    }

    public Ingredient processInput() {
        String string = pickIngredientComboBox.getSelectionModel().getSelectedItem().toString();
        System.out.println(string);
        Ingredient ingredient = new Ingredient();
        ingredient.setName(string);
        ingredient.setWeight(Integer.parseInt(weightField.getText()));

        return ingredient;

    }

}
