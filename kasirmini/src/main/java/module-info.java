module com.kasirmini.kasirmini {
    requires javafx.controls;
    requires javafx.fxml;
    
    requires java.sql;
    requires javafx.base;
    
    opens com.kasirmini.kasirmini to javafx.fxml, java.base;
    
    exports com.kasirmini.kasirmini;
}
