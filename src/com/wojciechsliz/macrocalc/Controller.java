package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import com.wojciechsliz.macrocalc.datamodel.Meal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class Controller {

    @FXML
    BorderPane mainBorderPane;

    @FXML
    DatePicker datePicker;

    @FXML
    private TableView<Ingredient> mealIngredientsTable;

    @FXML
    private ListView<Meal> mealList;


    @FXML
    public void initialize() {
        initializeMealList();


    }

    private void initializeMealList() {
        mealList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldMeal, newValue) -> {
            if (newValue!= null) {
                GetAllMealsTask task = new GetAllMealsTask(newValue);

                mealIngredientsTable.itemsProperty().bind(task.valueProperty());
                new Thread(task).start();

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
        dialog.setTitle("Add new ingredient");
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
            Meal meal = mealList.getSelectionModel().getSelectedItem();
            Ingredient ingredient = controller.processInput();
            try {
                int addMealIngredientResult = Datasource.getInstance().addMealIngredient(meal, ingredient);
                if (addMealIngredientResult == -1) {
                    addErrorDialog("Ingredient already exist");
                } else if (addMealIngredientResult == -2) {
                    addErrorDialog("Invalid Ingredient");
                }
                refreshMealList();
            } catch (NullPointerException e) {
                addErrorDialog("Invalid weight");
            }

        }
    }



    public void handleMealRemoval() {
        Meal meal = mealList.getSelectionModel().getSelectedItem();
        if (meal != null) {
            Datasource.getInstance().removeMeal(meal);
        }
    }

    public void handleMealIngredientRemoval() {
        Meal meal = mealList.getSelectionModel().getSelectedItem();
        Ingredient ingredient = mealIngredientsTable.getSelectionModel().getSelectedItem();
        if ((meal != null) && (ingredient != null)) {
            Datasource.getInstance().removeMealIngredient(meal, ingredient);
            refreshMealList();
        }
    }

    private void refreshMealList() {
        Meal m = mealList.getSelectionModel().getSelectedItem();
        mealList.getSelectionModel().clearSelection();
        mealList.getSelectionModel().select(m);
    }

    private void addErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error ");
        alert.setContentText(errorMessage);
        alert.showAndWait();
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
