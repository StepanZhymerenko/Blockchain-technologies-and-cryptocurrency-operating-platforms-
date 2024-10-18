import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block("0");
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock, int difficulty) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int blockIndex = 1;
        for (Block block : chain) {
            builder.append("Block ").append(blockIndex++).append(":\n");
            builder.append(block.toString()).append("\n");
        }
        return builder.toString();
    }
}