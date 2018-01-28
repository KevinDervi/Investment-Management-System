package main.java.data.account_information;

import main.java.util.CardDetails;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * where the users details will be stored during their current session
 */

public class UserDetails {
    private static Long id;
    private static String username;
    private static String password;
    private static String firstname;
    private static String surname;
    private static String email;
    private static BigDecimal balance;

    private static CardDetails cardBeingUsed;
    private static ArrayList<CardDetails> cards;


    private UserDetails(){}

    /**
     * reset values on logout
     */
    public static void reset(){
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

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        UserDetails.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserDetails.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserDetails.password = password;
    }

    public static String getFirstname() {
        return firstname;
    }

    public static void setFirstname(String firstname) {
        UserDetails.firstname = firstname;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        UserDetails.surname = surname;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserDetails.email = email;
    }

    public static BigDecimal getBalance() {
        return balance;
    }

    public static void setBalance(BigDecimal balance) {
        UserDetails.balance = balance;
    }

    public static CardDetails getCardBeingUsed() {
        return cardBeingUsed;
    }

    public static void setCardBeingUsed(CardDetails cardBeingUsed) {
        UserDetails.cardBeingUsed = cardBeingUsed;
    }

    public static ArrayList<CardDetails> getCards() {
        return cards;
    }

    public static void setCards(ArrayList<CardDetails> cards) {
        UserDetails.cards = cards;
    }

    public static void addCard(CardDetails card){
        UserDetails.cards.add(card);
    }

    public static void removeCard(Long cardId){
        //TODO remove card
    }
}
