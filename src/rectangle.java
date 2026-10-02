import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class rectangle extends Application {

    private double length;
    private double width;

    public rectangle() {
        length = 1;
        width = 1;
    }

    public void setLength(double length) {
        if (!Double.isFinite(length) || length <= 0) {
            throw new IllegalArgumentException(
                    "Length must be greater than 0.");
        }

        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public void setWidth(double width) {
        if (!Double.isFinite(width) || width <= 0) {
            throw new IllegalArgumentException(
                    "Width must be greater than 0.");
        }

        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public double calculatePerimeter() {
        return 2 * (length + width);
    }

    public double calculateArea() {
        return length * width;
    }

    @Override
    public void start(Stage stage) {
        TextField length = new TextField();
        TextField width = new TextField();

        length.setPromptText("Enter length");
        width.setPromptText("Enter width");

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(15);
        form.setVgap(15);

        form.addRow(0, new Label("Length:"), length);
        form.addRow(1, new Label("Width:"), width);

        Label result = new Label(
                "Area and perimeter will appear here.");

        result.setWrapText(true);
        result.setAlignment(Pos.CENTER);
        result.setMinHeight(100);
        result.setMaxWidth(410);

        Button calculate = new Button("Calculate");

        calculate.setOnAction(e -> {
            try {
                rectangle rectangle = new rectangle();

                rectangle.setLength(
                        Double.parseDouble(length.getText().trim()));

                rectangle.setWidth(
                        Double.parseDouble(width.getText().trim()));

                result.setText(String.format(
                        "Length: %.2f\n"
                        + "Width: %.2f\n"
                        + "Area: %.2f\n"
                        + "Perimeter: %.2f",
                        rectangle.getLength(),
                        rectangle.getWidth(),
                        rectangle.calculateArea(),
                        rectangle.calculatePerimeter()));

            } catch (NumberFormatException ex) {
                result.setText("Please enter valid numbers.");

            } catch (IllegalArgumentException ex) {
                result.setText(ex.getMessage());
            }
        });

        VBox root = new VBox(
                20, new Label("RECTANGLE CALCULATOR"),
                form, result, calculate);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;");

        stage.setTitle("Rectangle Calculator");
        stage.setScene(new Scene(root, 470, 360));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}