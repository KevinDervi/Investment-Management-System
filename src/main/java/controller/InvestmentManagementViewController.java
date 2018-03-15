package main.java.controller;

import com.zoicapital.stockchartsfx.BarData;
import com.zoicapital.stockchartsfx.CandleStickChart;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.java.controller.customviews.InvestmentHeldListCell;
import main.java.controller.customviews.StockLineGraph;
import main.java.controller.customviews.StockSearchField;
import main.java.logic.InvestmentsHeldLogic;
import main.java.logic.StockDataLogic;
import main.java.logic.UserDetailsLogic;
import main.java.util.*;

import java.math.BigDecimal;
import java.sql.SQLException;
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
    private AnchorPane anchorPaneSearchField;

    @FXML
    private ComboBox<Company> comboBoxStockSearch;

    @FXML
    private Button ButtonDeleteSearchText;

    @FXML
    private ListView<InvestmentHeld> ListViewInvestmentHeld;

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
    private RadioButton radioButton1Min;

    @FXML
    private ToggleGroup toggleGroupInterval;

    @FXML
    private RadioButton radioButton5Min;

    @FXML
    private RadioButton radioButton15Min;

    @FXML
    private RadioButton radioButton30Min;

    @FXML
    private RadioButton radioButton60Min;

    @FXML
    private RadioButton radioButtonDaily;

    @FXML
    private RadioButton radioButtonWeekly;

    @FXML
    private RadioButton radioButtonMonthly;

    @FXML
    private ToggleGroup toggleGroupOutputType;

    @FXML
    private RadioButton radioButtonRealTime;

    @FXML
    private RadioButton radioButtonFullHistory;

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

    @FXML
    private StackPane stackPaneStockDetailsArea;

    // data
    private StockDataLogic.StockDataUpdaterService chartUpdater = new StockDataLogic().new StockDataUpdaterService();


    /**
     * the initialize method is run after the view is created and has access to the FXML widgets while the constructor does now
     */
    @FXML
    public void initialize(){
        // TODO populate users investments held
        // TODO populate stock data views initially with S&P 500 or dow jones
        initialiseGraph();

        //createLineChart("line chart test"); // TODO remove title as paramenter and get title from internal model
        //chartUpdater.setChartTypeToReturn(ChartType.LINE);
        //chartUpdater.restart();

        // TODO make buying and selling a service in the logic layer and have it communicate with the database and when successfull it will return a value which will be binded to the listview



        // update user details
        initialiseUserDetails();

        initialiseStockPricesChangeFields();

        initialiseSearchFeield();

        initialiseInvestmentsHeldList();

        intialiseButtons();

        // TODO make service run in the logic later and make it instanced and bind values by calling to service later instead of creating it in ui layer

        // TODO place all threads in the business logic and let the controller class observe the logic layer

        // TODO check if user card details == null and force user to enter details before allowing them to trade

        // TODO start initial threads here
    }



    // TODO disable sell button if user does not hold any investments in that stock
    // TODO disable buy and sell buttons if market has closed
    // TODO drop down menu on text change in search bar
    // TODO delete all text when "X" button is pressed
    // TODO add stock analysis with KNN
    // TODO add card functionality (withdraw add remove view deposit)

    // TODO execute transactions (or anything that involves multiple tables at once) as batch statements or use

    /**
     * dbConnection.setAutoCommit(false); to start a transaction block.
     * dbConnection.commit(); to end a transaction block
     *
     * this means that all executions between these two statements will either all happen successfully or none will happen
     */


    // TODO maybe use stack pane for pop ups to make the background dark and force user input

    private void displaySAndP500(){

        // set initial values for stock data
        StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_INTRADAY);
        StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T1);
        StockDataLogic.setStockSymbol("SPX"); // SPX = S&P 500 index
        StockDataLogic.setOutputSize(StockOutputSize.REAL_TIME);

        // create the graph
        createCandleStickChart();

    }

    // ===================================== GRAPH METHODS ===============================================

    private void initialiseGraph() {
        // set initial values for chart updater
        chartUpdater.setChartTypeToReturn(ChartType.CANDLESTICK);


        displaySAndP500();

        initialiseListeners();



        // start service
        chartUpdater.start();



        initialiseGraphComboBoxType();

        initialiseIntervalToggleGroup();

        initialiseOutputSizeToggleGroup();
    }

    private void initialiseListeners() {

        chartStockData.dataProperty().addListener(this::updateGraphSize);

        //update the technical details
        chartStockData.dataProperty().addListener(this::updateTechnicalDetailsOnChanged);
    }

    private void setTechnicalDetails(BarData dataPoint) {
        labelOpenValue.setText(Double.toString(dataPoint.getOpen()));
        labelHighValue.setText(Double.toString(dataPoint.getHigh()));
        labelLowValue.setText(Double.toString(dataPoint.getLow()));
        labelCloseValue.setText(Double.toString(dataPoint.getClose()));
        labelVolumeValue.setText(Long.toString(dataPoint.getVolume()));
    }

    private void resetTechnicalDetails() {
        labelOpenValue.setText("");
        labelHighValue.setText("");
        labelLowValue.setText("");
        labelCloseValue.setText("");
        labelVolumeValue.setText("");
    }

    private void initialiseOutputSizeToggleGroup() {
        toggleGroupOutputType.selectToggle(radioButtonRealTime);

        toggleGroupOutputType.selectedToggleProperty().addListener(this::onOutputSizeToggleGroupChanged);
    }

    private void onOutputSizeToggleGroupChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        chartUpdater.cancel();

        // get string of button selected
        RadioButton selectedButton = (RadioButton) (toggleGroupOutputType.getSelectedToggle());
        String selectedValue = selectedButton.getText();

        // update logic layer
        if (selectedValue.equals("Real-time")){
            StockDataLogic.setOutputSize(StockOutputSize.REAL_TIME);
        }else{
            StockDataLogic.setOutputSize(StockOutputSize.FULL_HISTORY);
        }

        // restart service
        chartUpdater.restart();
    }

    private void initialiseIntervalToggleGroup() {

        // make toggle group always have at least 1 value selected by consuming the mouse event if that button is already selected
        toggleGroupInterval.selectToggle(radioButton1Min);
        // setup onclick functionality
        toggleGroupInterval.selectedToggleProperty().addListener(this::onIntervalToggleGroupChanged);
    }

    private void onIntervalToggleGroupChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        // cancel current stock data service
        chartUpdater.cancel();

        // get string of button selected
        RadioButton selectedButton = (RadioButton) (toggleGroupInterval.getSelectedToggle());
        String selectedValue = selectedButton.getText();

        // update the internal model
        if (selectedValue.contains("min")) {
            setIntradayGraph(selectedValue);
        }else{
            setOtherGraph(selectedValue);
        }

        // restart service
        chartUpdater.restart();
    }

    private void setOtherGraph(String selectedValue) {
        switch (selectedValue) {
            case "Daily":

                StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_DAILY);
                break;

            case "Weekly":

                StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_WEEKLY);
                break;

            case "Monthly":

                StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_MONTHLY);
                break;

            default:

                try {
                    throw new Exception("interval selected is not Daily/Weekly/Monthly");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setIntradayGraph(String interval) {
        // convert interval to time series intraday interval enum

        //remove dash from string
        interval = interval.replace("-", "");

        StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_INTRADAY);

        switch (interval) {
            case "1min":

                StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T1);
                break;

            case "5min":
                StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T5);
                break;

            case "15min":

                StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T15);
                break;

            case "30min":

                StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T30);
                break;

            case "60min":

                StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T60);
                break;

            default:

                // else throw an exception if an invalid toggle button is sent to this method
                try {
                    throw new Exception("wrong toggle button selected for intraday time series");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initialiseGraphComboBoxType(){
        // add graph types
        comboBoxGraphType.getItems().addAll("CandleStick", "Line");
        comboBoxGraphType.getSelectionModel().selectFirst();

        // TODO on click listener
        comboBoxGraphType.selectionModelProperty().getValue().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            switch (newValue) {
                case "CandleStick":
                    createCandleStickChart();
                    break;
                case "Line":
                    createLineChart();
                    break;
            }
            chartUpdater.cancel();
            chartUpdater.restart();
        });
    }

    private void createCandleStickChart(){
        // creates candlestick chart with an empty data set
        List<BarData> emptyDataSet = new ArrayList<>();
        BarData dummyData = new BarData(new GregorianCalendar(), 0.0,0.0,0.0,0.0,0L);
        emptyDataSet.add(dummyData);

        chartStockData = new CandleStickChart(StockDataLogic.getCurrentSymbol(), emptyDataSet);

        styleChart(chartStockData);

        addChartToPane(chartStockData);

        attachServiceToChart(chartStockData);

    }

    /**
     * binds the observable properties received from the service to the chart
     * @param chartStockData
     */
    private void attachServiceToChart(XYChart<String, Number> chartStockData) {
        // attach service to the graph
        chartStockData.dataProperty().unbind();
        chartStockData.dataProperty().bind(chartUpdater.lastValueProperty()); // bind the value of the service to the chart //  TODO add to project report that i am using javafx services for concurrency

        // bind title of chart to updater
        chartStockData.titleProperty().unbind();
        chartStockData.titleProperty().bind(chartUpdater.currentStockSymbolProperty());

    }

    private void updateTechnicalDetailsOnChanged(ObservableValue<? extends ObservableList<XYChart.Series<String, Number>>> observable, ObservableList<XYChart.Series<String, Number>> oldValue, ObservableList<XYChart.Series<String, Number>> newValue) {

        if (newValue == null) {
            resetTechnicalDetails();
            return;
        }

        int latestValue = newValue.get(0).getData().size();

        BarData dataPoint = (BarData) newValue.get(0).getData().get(latestValue - 1).getExtraValue();

        setTechnicalDetails(dataPoint);
    }

    private void updateGraphSize(ObservableValue<? extends ObservableList<XYChart.Series<String, Number>>> observable, ObservableList<XYChart.Series<String, Number>> oldValue, ObservableList<XYChart.Series<String, Number>> newValue) {
        int distanceBetweenValues = 20;

        if (newValue == null) {
            chartStockData.setPrefWidth(0);
        } else {
            chartStockData.setPrefWidth(newValue.get(0).getData().size() * distanceBetweenValues); // set size of data
        }
    }

    /**
     * stock data chart is created and added separately from the FXML file due to needing to swap between charts and
     * therefore requires its on setup when newly created
     * @param chartStockData
     */
    private void styleChart(XYChart<String, Number> chartStockData) {

        chartStockData.setLegendVisible(false);

        chartStockData.setAnimated(false);

        chartStockData.setCache(true);

        chartStockData.getStylesheets().add(getClass().getResource("/main/resources//css_styles/blue_mode.css").toExternalForm());
    }

    private void addChartToPane(XYChart<String, Number> chartStockData) {
        // (might not be needed) paneChart.getChildren().clear();// remove any children currently attached to the pane
        paneChart.setContent(chartStockData); // add to the chart pane
    }

    private void createLineChart(){

        chartStockData = new StockLineGraph();

        chartStockData.setTitle(StockDataLogic.getCurrentSymbol());

        styleChart(chartStockData);

        addChartToPane(chartStockData);

        attachServiceToChart(chartStockData);
    }


    // ===================================== USER DETAILS METHODS ========================================

    private void initialiseUserDetails(){
        updateUsername();
        initialiseBalance();
    }

    private void updateUsername(){
        labelUsername.setText(getUsername());
    }

    private String getUsername(){
        return UserDetailsLogic.getUsername();
    }

    private void initialiseBalance(){
        // TODO format balance to two Decimal places
        labelAccountBalance.setText("$" + getBalance());
        UserDetailsLogic.getBalanceProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("balance updated: " + newValue.toString());
            labelAccountBalance.setText("$" + newValue.toString());
        });
    }

    public BigDecimal getBalance(){
        return UserDetailsLogic.getBalance();
    }

    private void modifyBalance(BigDecimal amount){
        try {
            UserDetailsLogic.updateBalance(amount);
        }catch (SQLException e){
            // TODO diaplay an error message
        }


    }




    // ===================================== SEARCH FIELD METHODS ========================================
    private void initialiseSearchFeield(){

        // replace the default combo box with our own custom combo box
        comboBoxStockSearch = new StockSearchField();
        anchorPaneSearchField.getChildren().set(0, comboBoxStockSearch);

        // on click functionality
        comboBoxStockSearch.selectionModelProperty().getValue().selectedItemProperty().addListener(this::onStockSelected);

    }

    private void onStockSelected(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {

        // if null was selected then do nothing
        if(newValue == null){
            return;
        }
        System.out.println(newValue.getSymbol() + " was selected");

        // cancel the current service
        chartUpdater.cancel();

        // update the stock to search for
        StockDataLogic.setStockSymbol(newValue.getSymbol());

        //restart the service
        chartUpdater.restart();

        // TODO deselect an investment held currently selected
        ListViewInvestmentHeld.getSelectionModel().clearSelection();
    }

    // ===================================== INVESTMENTS HELD METHODS ====================================

    private void initialiseInvestmentsHeldList(){
        // setup cell factory to create my custom cell and use its overridden updateItem() method
        ListViewInvestmentHeld.setCellFactory(param -> new InvestmentHeldListCell());

        ListViewInvestmentHeld.itemsProperty().bind(InvestmentsHeldLogic.getObservableInvestmentHeldList());

        // only allow user to select a single investment at a time
        ListViewInvestmentHeld.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        //on selecting an investment
        ListViewInvestmentHeld.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            System.out.println(newValue.getStockSymbol() + "was selected");

            // cancel the current service
            chartUpdater.cancel();

            // update the stock to search for
            StockDataLogic.setStockSymbol(newValue.getStockSymbol());

            //restart the service
            chartUpdater.restart();
        });
    }

    private void updateInvestmentsHeld() {
        // TODO get list from logic layer and add here

    }
    // ===================================== TECHNICAL INDICATORS METHODS ================================


    // ===================================== INVESTMENT ANALYSIS METHODS =================================


    // ===================================== STOCK PRICES CHANGE METHODS =================================
    // TODO remove stock prices changes and substitute for current investment held values
    private void initialiseStockPricesChangeFields(){
        initilaiseStockPriceLabel();
        initialiseStockSymbolLabel();
    }

    private void initilaiseStockPriceLabel(){
        labelCurrentStockPrice.textProperty().bind(chartUpdater.currentStockValueProperty());
    }

    private void initialiseStockSymbolLabel() {
        labelStockSymbol.textProperty().bind(chartUpdater.currentStockSymbolProperty());
    }



    // ===================================== BUY/SELL BUTTON METHODS =====================================
    private void intialiseButtons(){
        labelCurrentStockPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("N/A")){
                ButtonBuyStock.setDisable(true);
            }else{
                ButtonBuyStock.setDisable(false);
            }
        });
    }
    @FXML
    void handleBuyButton(ActionEvent event) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/StockBuyView.fxml"));
        Parent stockBuyView = (Parent) fxmlLoader.load();
        StockBuyVewController controller = fxmlLoader.getController();
        controller.setMainWindowController(this);

        stackPaneStockDetailsArea.getChildren().add(stockBuyView);
    }


    // ===================================== UTILITY METHODS ==============================================

    public void removePopUp() {
        try {
            stackPaneStockDetailsArea.getChildren().remove(1);

        } catch (Exception ignored) {

        }
    }
}