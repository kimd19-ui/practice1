import java.time.DateTimeException;
import java.time.LocalDate;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class date extends Application {

    private date date;
    private int month;
    private int day;
    private int year;

    public date() {
        this(1, 1, LocalDate.now().getYear());
    }

    public date(int month, int day, int year) {
        if (year < 1) {
            throw new IllegalArgumentException(
                    "Year must be greater than 0.");
        }

        // Validates the calendar date, including leap years.
        LocalDate.of(year, month, day);

        this.month = month;
        this.day = day;
        this.year = year;
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    public void nextDay() {
        LocalDate next =
                LocalDate.of(year, month, day).plusDays(1);

        month = next.getMonthValue();
        day = next.getDayOfMonth();
        year = next.getYear();
    }

    @Override
    public void start(Stage stage) {
        TextField month = new TextField();
        TextField day = new TextField();
        TextField year = new TextField();

        month.setPromptText("1 to 12");
        day.setPromptText("Day");
        year.setPromptText("e.g. 2026");

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(15);
        form.setVgap(15);

        form.addRow(0, new Label("Month:"), month);
        form.addRow(1, new Label("Day:"), day);
        form.addRow(2, new Label("Year:"), year);

        Label result = new Label("Enter a date above.");
        result.setWrapText(true);
        result.setAlignment(Pos.CENTER);
        result.setMinHeight(75);
        result.setMaxWidth(410);

        Button display = new Button("Display Date");
        Button nextDay = new Button("Next Day");

        nextDay.setDisable(true);

        display.setOnAction(e -> {
            try {
                date newDate = new date(
                        Integer.parseInt(month.getText().trim()),
                        Integer.parseInt(day.getText().trim()),
                        Integer.parseInt(year.getText().trim()));

                date = newDate;
                result.setText("Date: " + date.toString());
                nextDay.setDisable(false);

            } catch (NumberFormatException ex) {
                result.setText("Please enter whole numbers.");
                nextDay.setDisable(true);

            } catch (DateTimeException ex) {
                result.setText("Please enter a valid date.");
                nextDay.setDisable(true);

            } catch (IllegalArgumentException ex) {
                result.setText(ex.getMessage());
                nextDay.setDisable(true);
            }
        });

        nextDay.setOnAction(e -> {
            try {
                date.nextDay();
                result.setText("Date: " + date.toString());

                String[] parts = date.toString().split("/");
                month.setText(parts[0]);
                day.setText(parts[1]);
                year.setText(parts[2]);

            } catch (DateTimeException ex) {
                result.setText(
                        "Cannot advance beyond the supported date range.");

                nextDay.setDisable(true);
            }
        });

        HBox buttons = new HBox(15, display, nextDay);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(
                20, new Label("DATE PROGRAM"),
                form, result, buttons);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;");

        stage.setTitle("Date Program");
        stage.setScene(new Scene(root, 470, 380));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}