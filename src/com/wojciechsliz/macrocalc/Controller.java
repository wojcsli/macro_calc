package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Ingredient;
import com.wojciechsliz.macrocalc.datamodel.Meal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private ChangeListener<Meal> changeListener = new ChangeListener<Meal>() {
        @Override
        public void changed(ObservableValue<? extends Meal> observableValue, Meal oldMeal, Meal newValue) {
            if (newValue != null) {
                GetAllMealsTask task = new GetAllMealsTask(newValue);
                mealIngredientsTable.itemsProperty().bind(task.valueProperty());
                new Thread(task).start();

            }
        }
    };


    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        initializeMealList(LocalDate.now());


    }

    private void initializeMealList(LocalDate date) {
        mealList.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        mealList.getSelectionModel().selectedItemProperty().addListener(changeListener);
        mealList.setItems((ObservableList<Meal>) Datasource.getInstance().queryMeals(date));
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
            Datasource.getInstance().addMeal(result.get(), datePicker.getValue());
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

    public void handleDateChange(ActionEvent actionEvent) {
        initializeMealList(datePicker.getValue());
    }

    public void handlePreviousDayButton(ActionEvent actionEvent) {
        LocalDate temp = datePicker.getValue();
        temp = temp.minusDays(1);
        datePicker.setValue(temp);
        initializeMealList(temp);
    }

    public void handleNextDayButton(ActionEvent actionEvent) {
        LocalDate temp = datePicker.getValue();
        temp = temp.plusDays(1);
        datePicker.setValue(temp);
        initializeMealList(temp);
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
