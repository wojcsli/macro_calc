package com.wojciechsliz.macrocalc.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "foods.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Projekty\\MacroCalc\\" + DB_NAME;

    public static final String TABLE_FOOD = "food";
    public static final String COLUMN_FOOD_ID = "_id";
    public static final String COLUMN_FOOD_NAME = "name";
    public static final String COLUMN_FOOD_CARB = "carb";
    public static final String COLUMN_FOOD_PROTEIN = "protein";
    public static final String COLUMN_FOOD_FAT = "fat";
    public static final String COLUMN_FOOD_KCAL = "kcal";

    public static final String TABLE_MEAL = "meal";
    public static final String COLUMN_MEAL_ID = "_id";
    public static final String COLUMN_MEAL_NAME = "name";
    public static final String COLUMN_MEAL_CARB = "carb";
    public static final String COLUMN_MEAL_PROTEIN = "protein";
    public static final String COLUMN_MEAL_FAT = "fat";
    public static final String COLUMN_MEAL_KCAL = "kcal";

    public static final String TABLE_MEAL_INGREDIENT = "meal_ingredient";
    public static final String COLUMN_MEAL_INGREDIENT_MEAL_ID = "meal_id";
    public static final String COLUMN_MEAL_INGREDIENT_INGREDIENT_ID = "ingredient_id";
    public static final String COLUMN_MEAL_INGREDIENT_WEIGHT = "weight";
    public static final String COLUMN_MEAL_INGREDIENT_CARB = "carb";
    public static final String COLUMN_MEAL_INGREDIENT_PROTEIN = "protein";
    public static final String COLUMN_MEAL_INGREDIENT_FAT = "fat";
    public static final String COLUMN_MEAL_INGREDIENT_KCAL = "kcal";



    public static final String QUERY_INGREDIENTS = "SELECT * FROM " + TABLE_FOOD;

    public static final String QUERY_MEALS = "SELECT * FROM " + TABLE_MEAL;

    public static final String QUERY_MEAL_INGREDIENTS = "SELECT " + TABLE_FOOD + "." + COLUMN_FOOD_NAME + ", " +
            TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_WEIGHT + ", " + TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_CARB + ", " +
            TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_PROTEIN + ", " + TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_FAT + ", " +
            TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_KCAL + " FROM " + TABLE_MEAL_INGREDIENT + " INNER JOIN " + TABLE_FOOD + " ON " +
            TABLE_MEAL_INGREDIENT + "." + COLUMN_MEAL_INGREDIENT_INGREDIENT_ID + " = " + TABLE_FOOD + "." + COLUMN_FOOD_ID + " WHERE " +
            COLUMN_MEAL_INGREDIENT_MEAL_ID + " = ?";

    public static final String ADD_MEAL_STRING = "INSERT INTO " + TABLE_MEAL + " (" + COLUMN_MEAL_NAME + ") VALUES (?)";

    private Connection connection;

    private PreparedStatement queryMealIngredients;
    private PreparedStatement addMealStatement;

    private static Datasource instance = new Datasource();

    public static Datasource getInstance() {
        return instance;
    }



    private ObservableList<Meal> meals;

    private Datasource(){
    }

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            queryMealIngredients = connection.prepareStatement(QUERY_MEAL_INGREDIENTS);
            addMealStatement = connection.prepareStatement(ADD_MEAL_STRING, Statement.RETURN_GENERATED_KEYS);
            meals = FXCollections.observableArrayList(queryMeals());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Meal> getMeals() {
        return meals;
    }

    public List<Meal> queryMeals() {
        List<Meal> meals = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_MEALS)) {

            while (resultSet.next()) {
                Meal meal = new Meal();
                meal.setId(resultSet.getInt(COLUMN_MEAL_ID));
                meal.setName(resultSet.getString(COLUMN_MEAL_NAME));
                meal.setCarbohydrateContent(resultSet.getDouble(COLUMN_MEAL_CARB));
                meal.setProteinContent(resultSet.getDouble(COLUMN_MEAL_PROTEIN));
                meal.setFatContent(resultSet.getDouble(COLUMN_MEAL_FAT));
                meal.setKiloCalories(resultSet.getDouble(COLUMN_MEAL_KCAL));
                meals.add(meal);
            }
            return meals;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<Ingredient> queryIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(QUERY_INGREDIENTS)) {
            while (results.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(results.getString(COLUMN_FOOD_NAME));
                ingredient.setFat(results.getDouble(COLUMN_FOOD_FAT));
                ingredient.setProtein(results.getDouble(COLUMN_FOOD_PROTEIN));
                ingredient.setCarb(results.getDouble(COLUMN_FOOD_CARB));
                ingredient.setKcal(results.getDouble(COLUMN_FOOD_KCAL));
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (SQLException e) {
            System.out.println("Couldn't add Ingredient");
            e.printStackTrace();
        }
        return null;
    }

    public List<Ingredient> queryMealIngredients(Meal meal) {
        try {
            queryMealIngredients.setInt(1,meal.getId());
            ResultSet results = queryMealIngredients.executeQuery();

            List<Ingredient> ingredients = new ArrayList<>();
            while (results.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(results.getString(COLUMN_FOOD_NAME));
                ingredient.setWeight(results.getDouble(COLUMN_MEAL_INGREDIENT_WEIGHT));
                ingredient.setCarb(results.getDouble(COLUMN_MEAL_INGREDIENT_CARB));
                ingredient.setProtein(results.getDouble(COLUMN_MEAL_INGREDIENT_PROTEIN));
                ingredient.setFat(results.getDouble(COLUMN_MEAL_INGREDIENT_FAT));
                ingredient.setKcal(results.getDouble(COLUMN_MEAL_INGREDIENT_KCAL));
                ingredients.add(ingredient);
            }
            return ingredients;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public boolean addMeal(String name) {
        if (!name.isEmpty()) {
            try {
                addMealStatement.setString(1, name);
                addMealStatement.executeUpdate();
                ResultSet generatedKey = addMealStatement.getGeneratedKeys();
                meals.add(new Meal(name, generatedKey.getInt(1)));
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("enter valid description");
        return false;
    }

    public boolean addMealIngredient(Meal meal) {
        return true;
    }

}
