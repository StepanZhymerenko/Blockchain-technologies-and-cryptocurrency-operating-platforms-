public class Node {
    private String nodeName;
    private Blockchain blockchain;

    public Node(String nodeName, Blockchain blockchain) {
        this.nodeName = nodeName;
        this.blockchain = blockchain;
    }

    public void mineBlock(int difficulty, Transaction[] transactions) {
        Block newBlock = new Block(blockchain.getLatestBlock().getHash());
        for (Transaction tx : transactions) {
            newBlock.addTransaction(tx);
        }
        System.out.println(nodeName + " is mining block with " + transactions.length + " transactions.");
        blockchain.addBlock(newBlock, difficulty);
    }

    public void receiveBlock(Block newBlock, int difficulty) {
        if (newBlock.getHash().substring(0, difficulty).equals(new String(new char[difficulty]).replace('\0', '0'))) {
            blockchain.addBlock(newBlock, difficulty);
            System.out.println(nodeName + " received and verified the block. Block added to local blockchain.");
        } else {
            System.out.println(nodeName + " rejected the block: hash does not meet difficulty.");
        }
    }

    public Blockchain getBlockchain() {
        return this.blockchain;
    }
}