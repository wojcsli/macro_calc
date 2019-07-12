package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import com.wojciechsliz.macrocalc.datamodel.Meal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import top.jalva.jalvafx.node.ComboBoxCustomizer;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    BorderPane mainBorderPane;

    @FXML
    private TableView<Meal> mealIngredientsTable;

    @FXML
    private ListView<Meal> mealList;


    @FXML
    public void initialize() {
        mealList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Meal>() {
            @Override
            public void changed(ObservableValue<? extends Meal> observableValue, Meal oldMeal, Meal newValue) {
                if (newValue!= null) {
                    GetAllMealsTask task = new GetAllMealsTask(newValue);

                    mealIngredientsTable.itemsProperty().bind(task.valueProperty());
                    new Thread(task).start();

                }
            }
        });
        mealList.setItems(Datasource.getInstance().getMeals());
        mealList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mealList.getSelectionModel().selectFirst();


    }

    @FXML
    public void handleMealAdd() {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("New meal");
        dialog.setHeaderText("Enter description of your meal");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            Datasource.getInstance().addMeal(result.get());
            mealList.getSelectionModel().selectLast();
        }
    }

    @FXML
    public void handleMealIngredientAdd() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addToMealDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            System.out.println(e.getMessage());
            return;
        }
        DialogController controller = fxmlLoader.getController();
        controller.setUpComboBox();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {


        }

    }
}

class GetAllMealsTask extends Task {
    private Meal meal;
    public GetAllMealsTask(Meal m) {
        this.meal = m;
    }

    @Override
    protected Object call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().queryMealIngredients(meal));
    }
}
