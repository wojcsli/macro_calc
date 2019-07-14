package com.wojciechsliz.macrocalc.datamodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ingredient {
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private SimpleDoubleProperty fat = new SimpleDoubleProperty();
    private SimpleDoubleProperty protein = new SimpleDoubleProperty();
    private SimpleDoubleProperty carb = new SimpleDoubleProperty();
    private SimpleDoubleProperty kcal = new SimpleDoubleProperty();
    private SimpleIntegerProperty ingredientId = new SimpleIntegerProperty();
    private SimpleIntegerProperty mealId = new SimpleIntegerProperty();

    public Ingredient() {
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

    public double getFat() {
        return fat.get();
    }


    public void setFat(double fat) {
        this.fat.set(fat);
    }

    public double getProtein() {
        return protein.get();
    }

    public void setProtein(double protein) {
        this.protein.set(protein);
    }

    public double getCarb() {
        return carb.get();
    }


    public void setCarb(double carb) {
        this.carb.set(carb);
    }

    public double getKcal() {
        return kcal.get();
    }

    public void setKcal(double kcal) {
        this.kcal.set(kcal);
    }

    public int getIngredientId() {
        return ingredientId.get();
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId.set(ingredientId);
    }

    public int getMealId() {
        return mealId.get();
    }

    public void setMealId(int mealId) {
        this.mealId.set(mealId);
    }

    @Override
    public String toString() {
        return name.getValue();

    }
}
