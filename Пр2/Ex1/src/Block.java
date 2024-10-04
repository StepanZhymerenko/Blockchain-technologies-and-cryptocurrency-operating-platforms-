import java.security.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class Block {
    private int version;
    private String prevHash;
    private long timestamp;
    private int difficultyTarget;
    private long nonce;
    private String merkleRoot;
    private List<Transaction> transactions;
    private String blockHash;
    private String signature;

    // Конструктор для створення нового блоку
    public Block(int version, String prevHash, int difficultyTarget, List<Transaction> transactions) throws NoSuchAlgorithmException {
        this.version = version;
        this.prevHash = prevHash;
        this.timestamp = System.currentTimeMillis();  // поточний час
        this.difficultyTarget = difficultyTarget;
        this.transactions = transactions;
        this.merkleRoot = calculateMerkleRoot(transactions);
        this.nonce = 0;  // Початкове значення
        this.blockHash = calculateBlockHash();
    }

    // Геттери та сеттери
    public int getVersion() {
        return version;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getDifficultyTarget() {
        return difficultyTarget;
    }

    public long getNonce() {
        return nonce;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    // Обчислення Merkle Root на основі хешів усіх транзакцій
    public String calculateMerkleRoot(List<Transaction> transactions) throws NoSuchAlgorithmException {
        List<String> hashes = new ArrayList<>();
        for (Transaction tx : transactions) {
            hashes.add(tx.getTxHash());
        }
        return hashMerkleRoot(hashes);
    }

    private String hashMerkleRoot(List<String> hashes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String combinedHash = String.join("", hashes);
        byte[] hash = digest.digest(combinedHash.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    // Обчислення хешу блоку
    public String calculateBlockHash() throws NoSuchAlgorithmException {
        String data = version + prevHash + timestamp + difficultyTarget + nonce + merkleRoot;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    // Майнінг блоку з певною складністю
    public void mineBlock(int difficulty) throws NoSuchAlgorithmException {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!blockHash.substring(0, difficulty).equals(target)) {
            nonce++;
            blockHash = calculateBlockHash();
        }
        System.out.println("Block mined: " + blockHash);
    }

    // Метод для верифікації блока
    public boolean verifyBlock() throws NoSuchAlgorithmException {
        return calculateBlockHash().equals(blockHash);
    }

    @Override
    public String toString() {
        return "Block{" +
                "version=" + version +
                ", prevHash='" + prevHash + '\'' +
                ", timestamp=" + timestamp +
                ", difficultyTarget=" + difficultyTarget +
                ", nonce=" + nonce +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
