import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    // Створення першого блоку в блокчейні (генезис блок)
    private Block createGenesisBlock() {
        try {
            return new Block(1, "0", 1, new ArrayList<>());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Отримати останній блок у ланцюгу
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    // Додати новий блок до ланцюга
    public void addBlock(Block newBlock) {
        chain.add(newBlock);
    }

    // Перевірка цілісності блокчейну
    public boolean isChainValid() throws NoSuchAlgorithmException {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getBlockHash().equals(currentBlock.calculateBlockHash())) {
                return false;  // Невірний хеш поточного блоку
            }

            // Використовуємо getPrevHash() для доступу до prevHash
            if (!previousBlock.getBlockHash().equals(currentBlock.getPrevHash())) {
                return false;  // Невірний хеш попереднього блоку
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder blockchain = new StringBuilder();
        for (Block block : chain) {
            blockchain.append(block.toString()).append("\n");
        }
        return blockchain.toString();
    }
}
