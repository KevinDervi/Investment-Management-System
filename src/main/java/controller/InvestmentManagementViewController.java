package main.java.controller;

import com.zoicapital.stockchartsfx.BarData;
import com.zoicapital.stockchartsfx.CandleStickChart;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.controller.customviews.InvestmentHeldListCell;
import main.java.controller.customviews.StockLineGraph;
import main.java.controller.customviews.StockSearchField;
import main.java.logic.InvestmentsHeldLogic;
import main.java.logic.SignOutLogic;
import main.java.logic.StockDataLogic;
import main.java.logic.UserDetailsLogic;
import main.java.logic.investment_Analysis.AnalysisResult;
import main.java.logic.investment_Analysis.StockAnalysis;
import main.java.util.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The type Investment management view controller.
 */
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
    private MenuBar menuBarMain;

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
    private ComboBox<String> comboBoxPredictionType;

    @FXML
    private Button buttonStockAnalysis;

    @FXML
    private ProgressBar progressBarStockAnalysis;

    @FXML
    private Label labelResult;

    @FXML
    private Label labelAccuracy;

    @FXML
    private Button ButtonBuyStock;

    @FXML
    private Button ButtonSellStock;

    @FXML
    private StackPane stackPaneStockDetailsArea;

    @FXML
    private MenuItem menuItemAccountSignOut;

    // services
    private StockDataLogic.StockDataUpdaterService chartUpdater = new StockDataLogic().new StockDataUpdaterService();
    private Service<AnalysisResult> analysisService;

    /**
     * the initialize method is run after the view is created and has access to the FXML widgets
     */
    @FXML
    public void initialize(){

        showInitialLoadingScreen();


        // initialise all aspects of the main window
        initialiseGraph();

        initialiseUserDetails();

        initialiseStockPricesFields();

        initialiseSearchFeield();

        initialiseInvestmentsHeldList();

        intialiseButtons();

        initialiseStockAnalysis();
    }

    /**
     * displays a loading screen on top of the stock information area (also inhibits user actions while loading)
     */
    private void showInitialLoadingScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/LoadingIndicator.fxml"));
            Parent loadingIcon = (Parent) fxmlLoader.load();
            loadingIcon.setId("loadingIcon");

            stackPaneStockDetailsArea.getChildren().add(loadingIcon);
        }catch (Exception ignored){}

    }

    /**
     * dbConnection.setAutoCommit(false); to start a transaction block.
     * dbConnection.commit(); to end a transaction block
     *
     * this means that all executions between these two statements will either all happen successfully or none will happen
     */




    // ===================================== GRAPH METHODS ===============================================

    /**
     * initialises the main graph that displays the information for the current stock being viewed
     */
    private void initialiseGraph() {
        // set initial values for chart updater
        chartUpdater.setChartTypeToReturn(ChartType.CANDLESTICK);


        displayInitialStock();

        initialiseListeners();



        // start service
        chartUpdater.start();



        initialiseGraphComboBoxType();

        initialiseIntervalToggleGroup();

        initialiseOutputSizeToggleGroup();
    }

    /**
     * currently the initial stock is set to display SPX (S&P 500 index) when user first loads in
     */
    private void displayInitialStock(){

        // set initial values for stock data

        // 1 min interval in real-time
        StockDataLogic.setFunction(StockTimeSeriesType.TIME_SERIES_INTRADAY); // intra day
        StockDataLogic.setInterval(StockTimeSeriesIntradayInterval.T1); // 1 min interval
        StockDataLogic.setStockSymbol("SPX"); // SPX = S&P 500 index
        StockDataLogic.setOutputSize(StockOutputSize.REAL_TIME); // real-time

        // creates the graph
        createCandleStickChart();

    }

    private void initialiseListeners() {

        chartStockData.dataProperty().addListener(this::updateGraphSize);

        //update the technical details
        chartStockData.dataProperty().addListener(this::updateTechnicalDetailsOnChanged);

        chartStockData.dataProperty().addListener(this::showLoadingIconWhenUpdating);

        // update Y axis
        paneChart.hvalueProperty().addListener(this::updateYAxisLocation);
        paneChart.widthProperty().addListener(this::updateYAxisLocation);
    }

    private void updateYAxisLocation(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int numberOfDatapoints;
        try {
            numberOfDatapoints = chartStockData.getData().get(0).getData().size();
        } catch (Exception e) {
            numberOfDatapoints = 0;
        }

        double widthBetweenPoints = 20.0;

        double valueToTranslate = paneChart.getHvalue() * (numberOfDatapoints * widthBetweenPoints - paneChart.getWidth());

        if (valueToTranslate < 0.0) {
            valueToTranslate = 0.0;
        }

        valueToTranslate -= 10.0;

        chartStockData.getYAxis().setTranslateX(valueToTranslate);
    }

    private void showLoadingIconWhenUpdating(ObservableValue<? extends ObservableList<XYChart.Series<String, Number>>> observable, ObservableList<XYChart.Series<String, Number>> oldValue, ObservableList<XYChart.Series<String, Number>> newValue) {
        if (newValue == null) {
            try {
                if(stackPaneStockDetailsArea.getChildren().get(1).getId().equals("loadingIcon")){
                    removePopUp();
                }
            }catch (Exception ignored){}

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/LoadingIndicator.fxml"));
                Parent loadingIcon = (Parent) fxmlLoader.load();


                stackPaneStockDetailsArea.getChildren().add(loadingIcon);
                loadingIcon.setId("loadingIcon");

            }catch (Exception ignored){ }

        } else {
            // only remove loading icon when there is one present
            try {
                if(stackPaneStockDetailsArea.getChildren().get(1).getId().equals("loadingIcon")){
                    removePopUp();
                }
            }catch (Exception ignored){}


        }
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

        chartStockData = new CandleStickChart(null, emptyDataSet);

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
        chartStockData.dataProperty().bind(chartUpdater.lastValueProperty()); // bind the value of the service to the chart

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

        if (oldValue == null){
            Platform.runLater(() -> {
                paneChart.hvalueProperty().setValue(1.0); // send the bar to the end of the screen
            });
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

        chartStockData.getStylesheets().clear();
        chartStockData.getStylesheets().add(getClass().getResource("/main/resources/css_styles/blue_mode.css").toExternalForm());
    }

    private void addChartToPane(XYChart<String, Number> chartStockData) {
        // (might not be needed) paneChart.getChildren().clear();// remove any children currently attached to the pane
        paneChart.setContent(chartStockData); // add to the chart pane

        //chartStockData.getYAxis().translateXProperty().bind(paneChart.hvalueProperty());
        chartStockData.getXAxis().setTranslateY(10.0);


    }

    private void createLineChart(){

        chartStockData = new StockLineGraph();

        //chartStockData.setTitle(StockDataLogic.getCurrentSymbol());

        styleChart(chartStockData);

        addChartToPane(chartStockData);

        attachServiceToChart(chartStockData);
    }


    // ===================================== USER DETAILS METHODS ========================================

    private void initialiseUserDetails(){
        updateUsername();
        initialiseBalance();
    }

    /**
     * Handle deposit withdraw button.
     *
     * @param event the event
     * @throws Exception the exception
     */
    @FXML
    void handleDepositWithdrawButton(ActionEvent event)throws Exception {
        removePopUp();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/BalanceModificationView.fxml"));
        Parent BalanceModification = (Parent) fxmlLoader.load();
        BalanceModificationController controller = fxmlLoader.getController();
        controller.setMainController(this);

        stackPaneStockDetailsArea.getChildren().add(BalanceModification);
    }

    private void updateUsername(){
        labelUsername.setText(getUsername());
    }

    private String getUsername(){
        return UserDetailsLogic.getUsername();
    }

    private void initialiseBalance(){
        labelAccountBalance.setText("$" + getBalance());
        UserDetailsLogic.getBalanceProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("balance updated: " + newValue.toString());
            labelAccountBalance.setText("$" + newValue.toString());
        });
    }

    /**
     * Get balance big decimal.
     *
     * @return the big decimal
     */
    public BigDecimal getBalance(){
        return UserDetailsLogic.getBalance();
    }


    // ===================================== SEARCH FIELD METHODS ========================================
    private void initialiseSearchFeield(){

        // replace the default combo box with our own custom combo box
        comboBoxStockSearch = new StockSearchField();
        anchorPaneSearchField.getChildren().set(0, comboBoxStockSearch);

        // on click functionality
        comboBoxStockSearch.selectionModelProperty().getValue().selectedItemProperty().addListener(this::onStockSelected);

    }

    /**
     * Handle button delete search text.
     *
     * @param event the event
     */
    @FXML
    void handleButtonDeleteSearchText(ActionEvent event){
        comboBoxStockSearch.getEditor().setText(null);
    }

    private void onStockSelected(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {

        // if null was selected then do nothing
        if(newValue == null){
            return;
        }

        removePopUp();

        ButtonSellStock.setDisable(true);

        System.out.println(newValue.getSymbol() + " was selected");

        // cancel the current service
        chartUpdater.cancel();

        // update the stock to search for
        StockDataLogic.setStockSymbol(newValue.getSymbol());

        //restart the service
        chartUpdater.restart();

        // clear any investment currently selected
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
        ListViewInvestmentHeld.getSelectionModel().selectedItemProperty().addListener(this::onInvestmentSelected);

        // update the view when we get a new latest value
        labelCurrentStockPrice.textProperty().addListener((observable, oldValue, newValue) -> ListViewInvestmentHeld.refresh());

    }

    private void onInvestmentSelected(ObservableValue<? extends InvestmentHeld> observable, InvestmentHeld oldValue, InvestmentHeld newValue) {
        if (newValue == null) {
            ButtonSellStock.setDisable(true);
            return;
        }
        removePopUp();

        ButtonSellStock.setDisable(false);

        System.out.println(newValue.getStockSymbol() + "was selected");

        // cancel the current service
        chartUpdater.cancel();

        // update the stock to search for
        StockDataLogic.setStockSymbol(newValue.getStockSymbol());

        //restart the service
        chartUpdater.restart();
    }

    /**
     * Select last investment held.
     */
    public void selectLastInvestmentHeld() {
        ListViewInvestmentHeld.getSelectionModel().selectLast();
    }

    // ===================================== TECHNICAL INDICATORS METHODS ================================


    // ===================================== INVESTMENT ANALYSIS METHODS =================================


    private void initialiseStockAnalysis(){
        initialisePredictionComboBox();

        labelStockSymbol.textProperty().addListener(this::terminateStockAnalysisOnStockChange);
    }


    private void terminateStockAnalysisOnStockChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (analysisService != null) {
            analysisService.cancel();
        }
        analysisService = null;

        resetStockAnalysis();
    }

    private void resetStockAnalysis() {
        labelResult.setText("");
        labelAccuracy.setText("");
    }

    /**
     * Handle stock analysis button.
     *
     * @param event the event
     */
    @FXML
    void handleStockAnalysisButton(ActionEvent event){
        String predictionType = comboBoxPredictionType.getSelectionModel().getSelectedItem();

         analysisService = new StockAnalysis(predictionType).getStockAnalysisService();


        // add listener for what happens on succeed
        analysisService.valueProperty().addListener((observable, oldValue, newValue) -> {
            String result;

            if (newValue.isClassification()){
                result = "Buy";
                labelResult.setTextFill(Color.LIGHTGREEN);
            }else {
                result = "Sell";
                labelResult.setTextFill(Color.rgb(255, 100, 100)); // light red
            }

            labelResult.setText(result);

            //format accuracy to 2 decimal places
            labelAccuracy.setText(String.format("%.2f", newValue.getAccuracy()) + "%");

//            comboBoxPredictionType.setDisable(false);
//            buttonStockAnalysis.setDisable(false);
        });

        // bind progress property to loading bar
        progressBarStockAnalysis.progressProperty().unbind();
        progressBarStockAnalysis.progressProperty().bind(analysisService.progressProperty());
        progressBarStockAnalysis.visibleProperty().unbind();
        progressBarStockAnalysis.visibleProperty().bind(analysisService.runningProperty());

        //  disable dropdown menu if analysis is currently running
        comboBoxPredictionType.disableProperty().unbind();
        comboBoxPredictionType.disableProperty().bind(analysisService.runningProperty());

        // disable analysis button if analysis is currently running
        buttonStockAnalysis.disableProperty().unbind();
        buttonStockAnalysis.disableProperty().bind(analysisService.runningProperty());


        //start the analysis
        analysisService.start();

    }

    private void initialisePredictionComboBox(){
        comboBoxPredictionType.getItems().addAll("1-Day", "1-Week", "1-Month");
        comboBoxPredictionType.getSelectionModel().selectFirst();
    }


    // ===================================== STOCK PRICES METHODS =================================
    private void initialiseStockPricesFields(){
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
        // initially disable stock sell button
        ButtonSellStock.setDisable(true);

        // disable buy button if current stock has no value
        labelCurrentStockPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("N/A")){
                ButtonBuyStock.setDisable(true);
            }else{
                ButtonBuyStock.setDisable(false);
            }
        });
    }

    /**
     * Handle buy button.
     *
     * @param event the event
     * @throws Exception the exception
     */
    @FXML
    void handleBuyButton(ActionEvent event) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/StockBuyView.fxml"));
        Parent stockBuyView = (Parent) fxmlLoader.load();
        StockBuyVewController controller = fxmlLoader.getController();
        controller.setMainWindowController(this);

        stackPaneStockDetailsArea.getChildren().add(stockBuyView);
    }

    /**
     * Handle sell button.
     *
     * @param event the event
     * @throws Exception the exception
     */
    @FXML
    void handleSellButton(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/StockSellView.fxml"));

        Parent stockSellView = (Parent) fxmlLoader.load();

        StockSellViewController controller = fxmlLoader.getController();
        controller.setMainController(this);
        controller.setInvestmentSelected(ListViewInvestmentHeld.getSelectionModel().getSelectedItem());



        stackPaneStockDetailsArea.getChildren().add(stockSellView);
    }

    // ===================================== SIGN OUT METHODS =============================================

    /**
     * Handle sign out.
     *
     * @param event the event
     * @throws Exception the exception
     */
    @FXML
    void handleSignOut(ActionEvent event) throws Exception{
        SignOutLogic.signOut();

        // try to cancel any current tasks
        try {
            chartUpdater.cancel();
            analysisService.cancel();
        }catch (Exception ignored){}

        // then destroy the services
        analysisService = null;
        chartUpdater = null;

        // close the current window
        Stage stage = (Stage) menuBarMain.getScene().getWindow();
        stage.close();


        // create new window for Sign In Screen
        Stage newStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignInView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        newStage.setScene(new Scene(root));
        newStage.setResizable(false);

        // finally display the sign in screen
        newStage.show();


        // request garbage collection
        System.gc();
    }


    // ===================================== UTILITY METHODS ==============================================

    /**
     * Remove pop up.
     */
    public void removePopUp() {
        try {
            //stackPaneStockDetailsArea.getChildren().remove(1);

            // remove all popups
            stackPaneStockDetailsArea.getChildren().remove(1, stackPaneStockDetailsArea.getChildren().size());

        } catch (Exception ignored) {

        }
    }



}