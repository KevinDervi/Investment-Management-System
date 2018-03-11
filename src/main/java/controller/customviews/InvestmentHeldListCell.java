package main.java.controller.customviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import main.java.logic.InvestmentsHeldLogic;
import main.java.util.InvestmentHeld;

import java.io.File;
import java.math.BigDecimal;

public class InvestmentHeldListCell extends ListCell<InvestmentHeld> {


    @FXML
    private Label labelStockSymbol;

    @FXML
    private Label labelCurrentPriceValue;

    @FXML
    private Label labelPriceBoughtValue;

    @FXML
    private Label labelPriceDifferenceValue;

    @FXML
    private Label labelQuantityownedValue;

    @FXML
    private ImageView imageArrow;

    @FXML
    private GridPane grid;


    public InvestmentHeldListCell() {
        super();

        // load fxml when creating cell
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/InvestmentHeldCell.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("unable to load FXMl when creating a List Cell");
        }
        System.out.println("create list cell");

        // add placeholder at index 1 so style class can be modified later
        labelPriceDifferenceValue.getStyleClass().add("empty");

    }

    @Override
    protected void updateItem(InvestmentHeld investmentHeld, boolean empty) {

        super.updateItem(investmentHeld, empty);

        System.out.println("update item");
        // standard required for the cell
        if(empty || investmentHeld == null){
            setGraphic(null);
            setText(null);
        }else{
            // update the cell here

            labelStockSymbol.setText(investmentHeld.getStockSymbol());

            BigDecimal currentPrice = InvestmentsHeldLogic.getCurrentPrice(investmentHeld.getStockSymbol());
            labelCurrentPriceValue.setText(currentPrice.toString());

            BigDecimal priceBought = investmentHeld.getIndividualPriceBought();
            labelPriceBoughtValue.setText(priceBought.toString());

            BigDecimal priceDifference = currentPrice.subtract(priceBought);

            // if value is positive then colour will be green otherwise red
            if (priceDifference.compareTo(BigDecimal.ZERO) > 0){
                labelPriceDifferenceValue.getStyleClass().set(1, "label-colour-green");
                Image positiveIndicator = new Image("file:src/main/resources/images/green_up_triangle.png");
                imageArrow.setImage(positiveIndicator);
            }else{
                labelPriceDifferenceValue.getStyleClass().set(1, "label-colour-red");
                Image negativeIndicator = new Image("file:src/main/resources/images/red_down_triangle.png");
                imageArrow.setImage(negativeIndicator);
            }
            labelPriceDifferenceValue.setText(priceDifference.toString());

            labelQuantityownedValue.setText(investmentHeld.getQuantityLeft().toString());

            // finally set graphic as grid

            setGraphic(grid);
            setText(null);

        }


    }
}
