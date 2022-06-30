module TheJavaGraph1_0 {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.controlsfx.controls;
	requires com.jfoenix;
	
	opens application to javafx.graphics, javafx.fxml;
}
