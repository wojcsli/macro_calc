package com.wojciechsliz.macrocalc.datamodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Meal {
    private ObservableList<Ingredient> ingredients;

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private SimpleDoubleProperty fatContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty proteinContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty carbohydrateContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty kiloCalories = new SimpleDoubleProperty();
    private SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty<>(this, "date");

    public Meal() {
    }

    public Meal(String name) {
        this.name.setValue(name);
        this.weight.setValue(0);
        this.fatContent.setValue(0);
        this.proteinContent.setValue(0);
        this.carbohydrateContent.setValue(0);
        this.kiloCalories.setValue(0);
    }

    public Meal(String name, int id) {
        this.name.setValue(name);
        this.id.setValue(id);
        this.weight.setValue(0);
        this.fatContent.setValue(0);
        this.proteinContent.setValue(0);
        this.carbohydrateContent.setValue(0);
        this.kiloCalories.setValue(0);
    }


    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObservableList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ObservableList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient){
        if (ingredient!=null){
            ingredients.add(ingredient);
        } else {
            System.out.println("null ingredient");
        }
    }


    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public double getWeight() {
        return weight.get();
    }


    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public double getFatContent() {
        return fatContent.get();
    }


    public void setFatContent(double fatContent) {
        this.fatContent.set(fatContent);
    }

    public double getProteinContent() {
        return proteinContent.get();
    }


    public void setProteinContent(double proteinContent) {
        this.proteinContent.set(proteinContent);
    }

    public double getCarbohydrateContent() {
        return carbohydrateContent.get();
    }


    public void setCarbohydrateContent(double carbohydrateContent) {
        this.carbohydrateContent.set(carbohydrateContent);
    }

    public double getKiloCalories() {
        return kiloCalories.get();
    }


    public void setKiloCalories(double kiloCalories) {
        this.kiloCalories.set(kiloCalories);
    }

    public void updateWeight() {
        int totalWeight=0;
        for (Ingredient ingredient : ingredients) {
            totalWeight += ingredient.getWeight();
        }
        this.weight.set(totalWeight);
    }

    public void updateFatContent() {
        int totalFat=0;
        for (Ingredient ingredient : ingredients) {
            totalFat += ingredient.getFat();
        }
        this.fatContent.set(totalFat);
    }

    public void updateProteinContent() {
        int totalProtein=0;
        for (Ingredient ingredient : ingredients) {
            totalProtein += ingredient.getProtein();
        }
        this.proteinContent.set(totalProtein);
    }

    public void updateCarbs() {
        int totalCarbs=0;
        for (Ingredient ingredient : ingredients) {
            totalCarbs += ingredient.getCarb();
        }
        this.carbohydrateContent.set(totalCarbs);
    }

    public void updateCalories() {
        int totalKcal=0;
        for (Ingredient ingredient : ingredients) {
            totalKcal += ingredient.getKcal();
        }
        this.kiloCalories.set(totalKcal);
    }

    public void updateMacros(){
        updateWeight();
        updateCarbs();
        updateFatContent();
        updateProteinContent();
        updateCalories();
    }

    public boolean lookForIngredientWithId(int ingredientId) {
        ingredients = FXCollections.observableArrayList(Datasource.getInstance().queryMealIngredients(this));
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getIngredientId() == ingredientId){
                return true;
            }
        }
        return false;
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
