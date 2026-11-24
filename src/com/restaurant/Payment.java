package com.restaurant;

public abstract class Payment {
    protected double amount;
    protected String method;
    protected String transactionId;

    public Payment(double amount, String method) {
        this.amount = amount;
        this.method = method;
        this.transactionId = "TXN" + (System.currentTimeMillis() % 100000);
    }


    public abstract boolean processPayment();

    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getTransactionId() { return transactionId; }
    public abstract String getDetails();
}


class CashPayment extends Payment {
    private double cashGiven;
    private double change;

    public CashPayment(double amount, double cashGiven) {
        super(amount, "Cash");
        this.cashGiven = cashGiven;
    }

    @Override
    public boolean processPayment() {
        if (cashGiven >= amount) {
            this.change = cashGiven - amount;
            return true;
        }
        return false;
    }

    @Override
    public String getDetails() {
        return String.format("Cash: Rs. %.2f | Change: Rs. %.2f", cashGiven, change);
    }
}


class UpiPayment extends Payment {
    private String upiId;

    public UpiPayment(double amount, String upiId) {
        super(amount, "UPI");
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment() {
        return upiId != null && upiId.contains("@");
    }

    @Override
    public String getDetails() {
        return "UPI ID: " + upiId;
    }
}


class CardPayment extends Payment {
    private String cardNumber;
    private String cardHolder;

    public CardPayment(double amount, String cardNumber, String cardHolder) {
        super(amount, "Card");
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public boolean processPayment() {
        String clean = cardNumber.replace(" ", "").replace("-", "");
        return clean.length() == 16;
    }

    @Override
    public String getDetails() {
        String clean = cardNumber.replace(" ", "").replace("-", "");
        String masked = "XXXX-XXXX-XXXX-" + clean.substring(12);
        return "Holder: " + cardHolder + " | Card: " + masked;
    }
}
