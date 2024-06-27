module net.programa.sklep.programsklep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens net.programa.sklep.programsklep to javafx.fxml;
    exports net.programa.sklep.programsklep;
}