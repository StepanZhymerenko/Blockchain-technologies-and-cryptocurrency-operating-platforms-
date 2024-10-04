import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class Transaction {
    private String input;  // вхідна адреса відправника
    private List<String> outputs;  // вихідні адреси отримувачів
    private double amount;
    private long txTimestamp;
    private String txHash;
    private String signature;

    public Transaction(String input, List<String> outputs, double amount) throws NoSuchAlgorithmException {
        this.input = input;
        this.outputs = outputs;
        this.amount = amount;
        this.txTimestamp = System.currentTimeMillis();  // поточний час
        this.txHash = calculateTxHash();  // генеруємо хеш транзакції
    }

    // Геттери та сеттери
    public String getInput() {
        return input;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public double getAmount() {
        return amount;
    }

    public long getTxTimestamp() {
        return txTimestamp;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    // Обчислення хешу транзакції
    public String calculateTxHash() throws NoSuchAlgorithmException {
        String data = input + outputs + amount + txTimestamp;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "input='" + input + '\'' +
                ", outputs=" + outputs +
                ", amount=" + amount +
                ", txTimestamp=" + txTimestamp +
                ", txHash='" + txHash + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    // Метод для перевірки транзакції
    public boolean verifyTransaction() throws NoSuchAlgorithmException {
        return calculateTxHash().equals(this.txHash);
    }
}
