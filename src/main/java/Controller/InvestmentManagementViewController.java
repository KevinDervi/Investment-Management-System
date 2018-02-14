package main.java.Controller;

import com.zoicapital.stockchartsfx.BarData;
import com.zoicapital.stockchartsfx.CandleStickChart;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import main.java.logic.StockDataLogic;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesIntradayInterval;
import main.java.util.StockTimeSeriesType;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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
    private ComboBox<String> comboBoxGraphType;

    @FXML
    private ScrollPane paneChart;

    private XYChart<String, Number> chartStockData;

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
     * the initialize method is run after the view is created and has access to the FXML widgets while the constructor does now
     */
    @FXML
    public void initialize(){
        // TODO populate users investments held
        // TODO populate stock data views initially with S&P 500 or dow jones

        displaySAndP500();


        // TODO check if user card details == null and force user to enter details before allowing them to trade
    }

    // TODO disable sell button if user does not hold any investments in that stock
    // TODO disable buy and sell buttons if market has closed
    // TODO drop down menu on text change in search bar
    // TODO delete all text when "X" button is pressed
    // TODO add stock analysis with KNN
    // TODO add card functionality (withdraw add remove view deposit)


    // TODO maybe use stack pane for pop ups to make the background dark and force user input

    private void displaySAndP500(){

        // set initial values for stock data
        StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_INTRADAY);
        StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T1);
        StockDataLogic.setStockSymbol("SPX"); // SPX = S&P 500 index
        StockDataLogic.setOutputSize(StockOutputSize.REAL_TIME);

        // create and populate the graph
        updateGraph();

        // add chart to the graph pane
        addChartToPane(chartStockData);

        // add graph types
        comboBoxGraphType.getItems().addAll("CandleStick", "Line");
        comboBoxGraphType.getSelectionModel().selectFirst();

    }

    // ===================================== GRAPH METHODS ===============================================

    private void updateGraph(){
        try {
            StockDataLogic.updateChartData();
        } catch (Exception e) {
            System.out.println("unable to update chart data");
            e.printStackTrace();
        }
        String currentSymbol = StockDataLogic.getCurrentSymbol();

        // TODO check what type of chart it is (candlestick or line)

        createCandleStickChart(currentSymbol);
        updateCandleStickChart();


    }

    private void updateCandleStickChart() {
        ObservableList<XYChart.Series<String, Number>> data = StockDataLogic.getCandleStickChartData();
        chartStockData.setData(data);
        chartStockData.setPrefWidth(data.get(0).getData().size() * 20);
    }

    private void updateLineChart(){
        ObservableList<XYChart.Series<String, Number>> data = StockDataLogic.getLineChartData();
        chartStockData.setData(data);
    }

    private void createCandleStickChart(String title){
        // creates candlestick chart with an empty data set
        List<BarData> emptyDataSet = new ArrayList<>();
        BarData dummyData = new BarData(new GregorianCalendar(), 0.0,0.0,0.0,0.0,0L);
        emptyDataSet.add(dummyData);

        chartStockData = new CandleStickChart(title, emptyDataSet);

        styleChart(chartStockData);

    }

    /**
     * stock data chart is created and added separately from the FXML file due to needing to swap between charts and
     * therefore requires its on setup when newly created
     * @param chartStockData
     */
    private void styleChart(XYChart<String, Number> chartStockData) {

        chartStockData.setLegendVisible(false);

        chartStockData.getStylesheets().add(getClass().getResource("/main/resources//css_styles/blue_mode.css").toExternalForm());
    }

    private void addChartToPane(XYChart<String, Number> chartStockData) {
        // (might not be needed) paneChart.getChildren().clear();// remove any children currently attached to the pane
        paneChart.setContent(chartStockData); // add to the chart pane
    }

    private void createLineChart(String title){
        // creates line chart with empty data set
        ObservableList<XYChart.Series<String, Number>> emptyDataSet = FXCollections.observableArrayList(new XYChart.Series<>());

        chartStockData = new LineChart<>(new CategoryAxis(), new NumberAxis(), emptyDataSet);

        styleChart(chartStockData);
    }


    // ===================================== USER DETAILS METHODS ========================================



    // ===================================== SEARCH FIELD METHODS ========================================


    // ===================================== INVESTMENTS HELD METHODS ====================================

    // ===================================== TECHNICAL INDICATORS METHODS ================================


    // ===================================== INVESTMENT ANALYSIS METHODS =================================


    // ===================================== STOCK PRICES CHANGE METHODS =================================


    // ===================================== BUY/SELL BUTTON METHODS =====================================

}