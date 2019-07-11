module MacroCalc {

        requires javafx.fxml;
        requires javafx.controls;
        requires sqlite.jdbc;
        requires java.sql;

        opens com.wojciechsliz.macrocalc;
        opens com.wojciechsliz.macrocalc.datamodel;
        }