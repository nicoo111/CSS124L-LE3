module com.mariechan.le3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mariechan.le3 to javafx.fxml;
    exports com.mariechan.le3;
}
