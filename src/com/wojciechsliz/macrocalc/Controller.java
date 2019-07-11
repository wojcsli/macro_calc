package com.wojciechsliz.macrocalc;

import com.wojciechsliz.macrocalc.datamodel.Datasource;
import com.wojciechsliz.macrocalc.datamodel.Meal;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;

public class Controller {

    @FXML
    private TableColumn columnName;

    @FXML
    private TableColumn columnWeight;

    @FXML
    private TableColumn columnCarb;

    @FXML
    private TableColumn columnProtein;

    @FXML
    private TableColumn columnFat;

    @FXML
    private TableColumn columnKcal;

    @FXML
    private TableView mealIngredientsTable;

    @FXML
    private ListView<Meal> mealList;

    private ObservableList<Meal> meals = FXCollections.observableArrayList(Datasource.getInstance().queryMeals());

    @FXML
    public void initialize() {
        mealList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Meal>() {
            @Override
            public void changed(ObservableValue<? extends Meal> observableValue, Meal oldMeal, Meal newValue) {
                if (newValue!= null) {
                    Meal meal = mealList.getSelectionModel().getSelectedItem();
                    GetAllMealsTask task = new GetAllMealsTask(newValue);

                    mealIngredientsTable.itemsProperty().bind(task.valueProperty());
                    new Thread(task).start();

                }
            }
        });
        mealList.setItems(meals);
        mealList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mealList.getSelectionModel().selectFirst();


    }


    public void handleMealAdd(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("New meal");
        dialog.setHeaderText("Enter description of your meal");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            int key = Datasource.getInstance().addMeal(result.get());
            meals.add(new Meal(result.get(), key));
            mealList.getSelectionModel().selectLast();



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
