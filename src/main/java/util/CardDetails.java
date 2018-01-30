package main.java.util;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class CardDetails {

    private final Long id;

    private final CardType cardType;

    private final String cardNumber;

    private final Date expirationDate;

    private final String nameOnCard;

    private final int securityCode;

    private final Date created;


    public CardDetails(Map<String, Object> details){

        id = (Long) details.get("id");
        cardType = (CardType) details.get("cardType");
        expirationDate = (Date) details.get("expirationDate");
        cardNumber = (String) details.get("cardNumber");
        nameOnCard = (String) details.get("nameOnCard");
        securityCode = (int) details.get("securityCode");
        created = new Date((Long) details.get("created"));

    }

    public Long getId() {
        return id;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public Date getCreated() {
        return created;
    }
}
