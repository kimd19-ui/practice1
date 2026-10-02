import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
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

public class healthProfile extends Application {

    private String firstName;
    private String lastName;
    private String gender;
    private int birthMonth;
    private int birthDay;
    private int birthYear;
    private double heightInInches;
    private double weightInPounds;

    
    public healthProfile() {
    }

    public healthProfile(String firstName, String lastName, String gender,
            int birthMonth, int birthDay, int birthYear,
            double heightInInches, double weightInPounds) {

        validateBirthDate(birthMonth, birthDay, birthYear);

        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.birthYear = birthYear;

        setheightInInches(heightInInches);
        setweightInPounds(weightInPounds);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setBirthMonth(int month) {
        validateBirthDate(month, birthDay, birthYear);
        birthMonth = month;
    }

    public int getBirthMonth() {
        return birthMonth;
    }

    public void setBirthDay(int day) {
        validateBirthDate(birthMonth, day, birthYear);
        birthDay = day;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthYear(int year) {
        validateBirthDate(birthMonth, birthDay, year);
        birthYear = year;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setheightInInches(double height) {
        if (!Double.isFinite(height) || height <= 0) {
            throw new IllegalArgumentException(
                    "Height must be greater than 0.");
        }

        heightInInches = height;
    }

    public double getheightInInches() {
        return heightInInches;
    }

    public void setweightInPounds(double weight) {
        if (!Double.isFinite(weight) || weight <= 0) {
            throw new IllegalArgumentException(
                    "Weight must be greater than 0.");
        }

        weightInPounds = weight;
    }

    public double getweightInPounds() {
        return weightInPounds;
    }

    public int calculateAge() {
        LocalDate birthday = LocalDate.of(
                birthYear, birthMonth, birthDay);

        return Period.between(birthday, LocalDate.now()).getYears();
    }

    // Returns the target range: 50% to 85% of estimated maximum.
    public double[] calculateMaximumHeartRateRange() {
        double maximumHeartRate = 220 - calculateAge();

        if (maximumHeartRate <= 0) {
            throw new IllegalArgumentException(
                    "Age is too high for this heart-rate formula.");
        }

        return new double[] {
                maximumHeartRate * 0.50,
                maximumHeartRate * 0.85
        };
    }

    public double calculateBMI() {
        return weightInPounds
                / (heightInInches * heightInInches) * 703;
    }

    private void validateBirthDate(int month, int day, int year) {
        if (year < 1) {
            throw new IllegalArgumentException(
                    "Birth year must be greater than 0.");
        }

        LocalDate birthday = LocalDate.of(year, month, day);

        if (birthday.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Birthday cannot be in the future.");
        }
    }

    @Override
    public void start(Stage stage) {
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        TextField gender = new TextField();
        TextField month = new TextField();
        TextField day = new TextField();
        TextField year = new TextField();
        TextField height = new TextField();
        TextField weight = new TextField();

        month.setPromptText("1 to 12");
        day.setPromptText("Day of birth");
        year.setPromptText("e.g. 2006");
        height.setPromptText("Total inches, e.g. 68");
        weight.setPromptText("Pounds, e.g. 150");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        form.addRow(0, new Label("First name:"), firstName);
        form.addRow(1, new Label("Last name:"), lastName);
        form.addRow(2, new Label("Gender:"), gender);
        form.addRow(3, new Label("Birth month:"), month);
        form.addRow(4, new Label("Birth day:"), day);
        form.addRow(5, new Label("Birth year:"), year);
        form.addRow(6, new Label("Height (inches):"), height);
        form.addRow(7, new Label("Weight (pounds):"), weight);

        Label result = new Label("Enter your information above.");
        result.setWrapText(true);
        result.setAlignment(Pos.CENTER);
        result.setMinHeight(160);
        result.setMaxWidth(490);

        Button calculate = new Button("Calculate");

        calculate.setOnAction(e -> {
            try {
                if (firstName.getText().trim().isEmpty()
                        || lastName.getText().trim().isEmpty()
                        || gender.getText().trim().isEmpty()) {

                    result.setText("Please enter your name and gender.");
                    return;
                }

                healthProfile profile = new healthProfile(
                        firstName.getText().trim(),
                        lastName.getText().trim(),
                        gender.getText().trim(),
                        Integer.parseInt(month.getText().trim()),
                        Integer.parseInt(day.getText().trim()),
                        Integer.parseInt(year.getText().trim()),
                        Double.parseDouble(height.getText().trim()),
                        Double.parseDouble(weight.getText().trim()));

                int age = profile.calculateAge();
                double[] range =
                        profile.calculateMaximumHeartRateRange();

                result.setText(String.format(
                        "Name: %s %s\n"
                        + "Gender: %s\n"
                        + "Birthday: %d/%d/%d\n"
                        + "Age: %d years\n"
                        + "Estimated maximum heart rate: %d bpm\n"
                        + "Target heart-rate range: %.1f to %.1f bpm\n"
                        + "BMI: %.2f",
                        profile.getFirstName(),
                        profile.getLastName(),
                        profile.getGender(),
                        profile.getBirthMonth(),
                        profile.getBirthDay(),
                        profile.getBirthYear(),
                        age,
                        220 - age,
                        range[0],
                        range[1],
                        profile.calculateBMI()));

            } catch (NumberFormatException ex) {
                result.setText(
                        "Enter valid numbers for birthday, height, and weight.");

            } catch (DateTimeException ex) {
                result.setText("Please enter a valid birthday.");

            } catch (IllegalArgumentException ex) {
                result.setText(ex.getMessage());
            }
        });

        VBox root = new VBox(
                15, new Label("HEALTH PROFILE"),
                form, result, calculate);

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;");

        stage.setTitle("Health Profile");
        stage.setScene(new Scene(root, 540, 620));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}