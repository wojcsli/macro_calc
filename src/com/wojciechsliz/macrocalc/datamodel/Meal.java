package com.wojciechsliz.macrocalc.datamodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Meal {
    private ObservableList<Ingredient> ingredients;

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private SimpleDoubleProperty fatContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty proteinContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty carbohydrateContent = new SimpleDoubleProperty();
    private SimpleDoubleProperty kiloCalories = new SimpleDoubleProperty();

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

    public ObservableList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ObservableList<Ingredient> ingredients) {
        this.ingredients = ingredients;
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

    private void updateWeight() {
        int totalWeight=0;
        for (Ingredient ingredient : ingredients) {
            totalWeight += ingredient.getWeight();
        }
        this.weight.set(totalWeight);
    }

    private void updateFatContent() {
        int totalFat=0;
        for (Ingredient ingredient : ingredients) {
            totalFat += ingredient.getFat();
        }
        this.fatContent.set(totalFat);
    }

    private void updateProteinContent() {
        int totalProtein=0;
        for (Ingredient ingredient : ingredients) {
            totalProtein += ingredient.getProtein();
        }
        this.proteinContent.set(totalProtein);
    }

    private void updateCarbs() {
        int totalCarbs=0;
        for (Ingredient ingredient : ingredients) {
            totalCarbs += ingredient.getCarb();
        }
        this.carbohydrateContent.set(totalCarbs);
    }

    private void updateCalories() {
        int totalKcal=0;
        for (Ingredient ingredient : ingredients) {
            totalKcal += ingredient.getKcal();
        }
        this.kiloCalories.set(totalKcal);
    }

    private void updateMacros(){
        updateWeight();
        updateCarbs();
        updateFatContent();
        updateProteinContent();
        updateCalories();
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}