import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class Block {
    private String previousHash;
    private String merkleRoot;
    private long timestamp;
    private int nonce;
    private String hash;
    private List<Transaction> transactions;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.transactions = new ArrayList<>();
        this.merkleRoot = calculateMerkleRoot();
        this.hash = calculateBlockHash();
    }

    public void addTransaction(Transaction tx) {
        transactions.add(tx);
        this.merkleRoot = calculateMerkleRoot();
    }

    public String calculateMerkleRoot() {
        StringBuilder builder = new StringBuilder();
        for (Transaction tx : transactions) {
            builder.append(tx.toString());
        }
        return hashSHA256(builder.toString());
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash + merkleRoot + Long.toString(timestamp) + Integer.toString(nonce);
        return hashSHA256(dataToHash);
    }

    public static String hashSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateBlockHash();
        }
        System.out.println("Block mined!!! Hash: " + hash);
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int txIndex = 1;
        builder.append(String.format("Block [previousHash=%s, merkleRoot=%s, hash=%s, timestamp=%d, nonce=%d]\n",
                previousHash, merkleRoot, hash, timestamp, nonce));
        for (Transaction tx : transactions) {
            builder.append("\tTransaction ").append(txIndex++).append(": ").append(tx.toString()).append("\n");
        }
        return builder.toString();
    }
}