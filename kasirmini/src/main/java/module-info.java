module com.kasirmini.kasirmini {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kasirmini.kasirmini to javafx.fxml;
    exports com.kasirmini.kasirmini;
}
