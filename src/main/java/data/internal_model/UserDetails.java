package main.java.data.internal_model;

import main.java.data.database.UserDAO;
import main.java.util.CardDetails;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * where the users details will be stored during their current session
 */
// TODO make singleton class
public class UserDetails {
    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String surname;
    private String email;
    private BigDecimal balance;

    private CardDetails cardBeingUsed;
    private ArrayList<CardDetails> cards;

    private static UserDetails instance;

    private UserDetails(){}

    public static UserDetails getInstance() {
        if (instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    public void initialiseUserDetails(String username){
        UserDAO.getUser(username);

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
        balance = null;
        cardBeingUsed = null;
        cards = null;
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
        return balance;
    }

    private void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        //TODO remove card
    }

    // TODO have update methods for Username, Password, and email that does logic and interacts with the DAO
}
