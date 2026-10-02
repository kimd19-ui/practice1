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

public class savingsAccount extends Application {

    private savingsAccount account;
    private int months;
    private double annualInterestRate;
    private double savingsBalance;

    public savingsAccount() {
        this(0);
    }

    public savingsAccount(double initialBalance) {
        if (!Double.isFinite(initialBalance) || initialBalance < 0) {
            throw new IllegalArgumentException(
                    "Balance must be 0 or greater.");
        }

        savingsBalance = initialBalance;
    }

    public void calculateMonthlyInterest() {
        double monthlyInterest =
                savingsBalance * annualInterestRate / 12;

        savingsBalance += monthlyInterest;
    }

    // 0.04 means an annual rate of 4%.
    public void modifyInterestRate(double newRate) {
        if (!Double.isFinite(newRate) || newRate < 0) {
            throw new IllegalArgumentException(
                    "Interest rate must be 0 or greater.");
        }

        annualInterestRate = newRate;
    }

    public double getSavingsBalance() {
        return savingsBalance;
    }

    @Override
    public void start(Stage stage) {
        TextField balance = new TextField();
        TextField rate = new TextField();

        balance.setPromptText("e.g. 2000");
        rate.setPromptText("e.g. 4 means 4%");

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(15);
        form.setVgap(15);

        form.addRow(0, new Label("Initial balance:"), balance);
        form.addRow(1, new Label("Annual rate (%):"), rate);

        Label result = new Label("Create an account to begin.");
        result.setWrapText(true);
        result.setAlignment(Pos.CENTER);
        result.setMinHeight(120);
        result.setMaxWidth(430);

        Button create = new Button("Create / Reset Account");
        Button addInterest = new Button("Add 1 Month");

        addInterest.setDisable(true);

        create.setOnAction(e -> {
            try {
                double initialBalance =
                        Double.parseDouble(balance.getText().trim());

                double annualRate =
                        Double.parseDouble(rate.getText().trim()) / 100;

                savingsAccount newAccount =
                        new savingsAccount(initialBalance);

                newAccount.modifyInterestRate(annualRate);

                account = newAccount;
                months = 0;
                addInterest.setDisable(false);

                result.setText(String.format(
                        "Balance: %.2f\nMonths added: 0",
                        account.getSavingsBalance()));

            } catch (NumberFormatException ex) {
                result.setText("Please enter valid numbers.");

            } catch (IllegalArgumentException ex) {
                result.setText(ex.getMessage());
            }
        });

        addInterest.setOnAction(e -> {
            try {
                account.modifyInterestRate(
                        Double.parseDouble(rate.getText().trim()) / 100);

                double previousBalance =
                        account.getSavingsBalance();

                account.calculateMonthlyInterest();
                months++;

                result.setText(String.format(
                        "Interest this month: %.2f\n"
                        + "New balance: %.2f\n"
                        + "Months added: %d",
                        account.getSavingsBalance() - previousBalance,
                        account.getSavingsBalance(),
                        months));

            } catch (NumberFormatException ex) {
                result.setText(
                        "Please enter a valid annual interest rate.");

            } catch (IllegalArgumentException ex) {
                result.setText(ex.getMessage());
            }
        });

        HBox buttons = new HBox(10, create, addInterest);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(
                20, new Label("SAVINGS ACCOUNT"),
                form, result, buttons);

        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold;");

        stage.setTitle("Savings Account");
        stage.setScene(new Scene(root, 510, 380));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}