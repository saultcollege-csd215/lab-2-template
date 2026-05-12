module sales {
    requires java.sql;
    requires javafx.controls;
    requires java.logging;

    exports sales.feature.base.ui;
    exports sales;
}