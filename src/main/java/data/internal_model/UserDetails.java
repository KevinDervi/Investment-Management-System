package main.java.data.internal_model;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import main.java.data.database.MoneyTransferDAO;
import main.java.data.database.UserDAO;
import main.java.util.CardDetails;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * where the users details will be stored during their current session
 */
public class UserDetails {
    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String email;
    private SimpleObjectProperty<BigDecimal> balance;

    private CardDetails cardBeingUsed;
    private ArrayList<CardDetails> cards;

    private static UserDetails instance;

    private UserDetails() {
        balance = new SimpleObjectProperty<>();
    }

    public static UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    public void initialiseUserDetails(String username){
        Map<String, Object> details = UserDAO.getUser(username);
        id = (long) details.get("id");
        this.username = (String) details.get("username");
        firstname = (String) details.get("firstname");
        surname = (String) details.get("surname");
        email = (String) details.get("email");
        balance.setValue((BigDecimal) details.get("balance"));



    }

    /**
     * reset values on logout
     */
    public void reset(){
        id = null;
        username = null;
        password = null;
        firstname = null;
        surname = null;
        email = null;
        balance.setValue(null);
        cardBeingUsed = null;
        cards = null;
    }

    public boolean authenticateUser(String username, String password) throws Exception{
        return UserDAO.authenticateUser(username, password);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    private void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    private void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getBalance() {
        return balance.getValue();
    }

    private void setBalance(BigDecimal balance) {
        this.balance.setValue(balance);
    }

    public SimpleObjectProperty<BigDecimal> balanceProperty() {
        return balance;
    }

    public CardDetails getCardBeingUsed() {
        return cardBeingUsed;
    }

    private void setCardBeingUsed(CardDetails cardBeingUsed) {
        this.cardBeingUsed = cardBeingUsed;
    }

    public ArrayList<CardDetails> getCards() {
        return cards;
    }

    private void setCards(ArrayList<CardDetails> cards) {
        this.cards = cards;
    }

    public void addCard(CardDetails card){
        this.cards.add(card);
    }

    public void removeCard(Long cardId){
    }


    public void updateBalance(BigDecimal byAmount) throws SQLException {
        UserDAO.modifyBalanceBy(byAmount);

        // if error is thrown and the database was unable to update then the internal model will not update
        setBalance((BigDecimal) UserDAO.getUser(username).get("balance"));
    }

    public void desposit(BigDecimal amount){
        MoneyTransferDAO.despoitToAccount(amount);
        setBalance((BigDecimal) UserDAO.getUser(username).get("balance"));
    }

    public void withdraw(BigDecimal amount){
        MoneyTransferDAO.withdrawFromAccount(amount);
        setBalance((BigDecimal) UserDAO.getUser(username).get("balance"));
    }

    public void updateBalance(){
        System.out.println("updating balance");
        BigDecimal newBalance = (BigDecimal) UserDAO.getUser(username).get("balance");

        // run later required to update UI
        Platform.runLater(() -> setBalance(newBalance));

    }

    public void signOut() {
        instance = null;
    }
}
