import java.util.UUID;

public class Transaction {
    private String id;
    private String sender;
    private String receiver;
    private double amount;

    public Transaction(String sender, String receiver, double amount) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("Transaction [id=%s, sender=%s, receiver=%s, amount=%.2f]", id, sender, receiver, amount);
    }
}