package main.java.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import main.java.util.InvestmentHeld;

import java.io.IOException;

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


    InvestmentHeldListCell() {
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

    }

    @Override
    protected void updateItem(InvestmentHeld investmentHeld, boolean empty) {

        super.updateItem(investmentHeld, empty);

        System.out.println("update item");
        // standard required for the cell
        if(empty){
            setGraphic(null);
            setText(null);
        }else{
            // update the cell here
            //setText("test1");

            labelStockSymbol.setText(investmentHeld.getStockSymbol());

            // finally set graphic as grid

            setGraphic(grid);
            setText(null);

        }


    }
}
