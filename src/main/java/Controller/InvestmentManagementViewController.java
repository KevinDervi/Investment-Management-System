package main.java.Controller;

import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.ComboBox;
        import javafx.scene.control.Label;
        import javafx.scene.control.ListView;
        import javafx.scene.control.Menu;
        import javafx.scene.control.MenuBar;
        import javafx.scene.control.TextField;
        import javafx.scene.control.ToggleButton;
        import javafx.scene.control.ToggleGroup;
        import javafx.scene.image.ImageView;

public class InvestmentManagementViewController {

    @FXML
    private Label labelUsername;

    @FXML
    private Label labelAccountBalance;

    @FXML
    private Button ButtonDespositWirthdraw;

    @FXML
    private TextField TextFieldStockSearch;

    @FXML
    private Button ButtonDeleteSearchText;

    @FXML
    private ListView<?> ListViewInvestmentHeld;

    @FXML
    private MenuBar menuHelp;

    @FXML
    private Menu menuAccount;

    @FXML
    private Menu menuSettings;

    @FXML
    private Menu MenuAbout;

    @FXML
    private Label labelOpenValue;

    @FXML
    private Label labelHighValue;

    @FXML
    private Label labelLowValue;

    @FXML
    private Label labelCloseValue;

    @FXML
    private Label labelVolumeValue;

    @FXML
    private ToggleButton ToggleButton1Min;

    @FXML
    private ToggleGroup IntervalToggleGroup;

    @FXML
    private ToggleButton ToggleButton5Min;

    @FXML
    private ToggleButton ToggleButton15Min;

    @FXML
    private ToggleButton ToggleButton30Min;

    @FXML
    private ToggleButton ToggleButton60Min;

    @FXML
    private ToggleButton ToggleButtonDaily;

    @FXML
    private ToggleButton ToggleButtonWeekly;

    @FXML
    private ToggleButton ToggleButtonMonthly;

    @FXML
    private ToggleButton ToggleButtonRealTime;

    @FXML
    private ToggleGroup outputSizeToggleGroup;

    @FXML
    private ToggleButton ToggleButtonFullHistory;

    @FXML
    private ComboBox<?> ComboBoxGraphType;

    @FXML
    private Label labelStockSymbol;

    @FXML
    private Label labelCurrentStockPrice;

    @FXML
    private Label labelDailyStockChange;

    @FXML
    private Label labelDailyStockChangePercentage;

    @FXML
    private ImageView imageDailyStockChange;

    @FXML
    private ImageView imageWeeklyStockChange;

    @FXML
    private ImageView imageMonthlyStockChange;

    @FXML
    private ImageView imageYearlyStockChange;

    @FXML
    private Label labelWeeklyStockChange;

    @FXML
    private Label labelMonthlyStockChange;

    @FXML
    private Label labelYearlyStockChange;

    @FXML
    private Label labelMonthlyStockChangePercentage;

    @FXML
    private Label labelYearlyStockChangePercentage;

    @FXML
    private Button ButtonBuyStock;

    @FXML
    private Button ButtonSellStock;

    /**
     * the initialize method is run after the view is created and has access to the FXML widgets while the contructor does now
     */
    @FXML
    public void initialize(){
        // TODO populate users investments held
        // TODO populate stock data views initially with S&P 500 or dow jones

        // TODO check if user card details == null and force user to enter details before allowing them to trade
    }

    // TODO disable sell button if user does not hold any investments in that stock
    // TODO disable buy and sell buttons if market has closed
    // TODO drop down menu on text change in search bar
    // TODO delete all text when "X" button is pressed
    // TODO add stock analysis with KNN
    // TODO add card functionality (withdraw add remove view deposit)

}