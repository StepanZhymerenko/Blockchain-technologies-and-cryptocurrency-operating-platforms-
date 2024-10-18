import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        Node node1 = new Node("Node 1", blockchain);
        Node node2 = new Node("Node 2", blockchain);

        // Генеруємо випадкові транзакції для двох вузлів
        Transaction[] transactions1 = generateRandomTransactions(5);
        Transaction[] transactions2 = generateRandomTransactions(3);

        System.out.println("Node 1 is mining the first block...");
        node1.mineBlock(4, transactions1);

        System.out.println("Node 1 has mined a block. Broadcasting to Node 2...");
        Block latestBlock1 = blockchain.getLatestBlock();
        node2.receiveBlock(latestBlock1, 4);

        System.out.println("Node 2 is mining the next block...");
        node2.mineBlock(4, transactions2);

        System.out.println("Node 2 has mined a block. Broadcasting to Node 1...");
        Block latestBlock2 = blockchain.getLatestBlock();
        node1.receiveBlock(latestBlock2, 4);

        System.out.println("Final Blockchain State for Node 1:");
        System.out.println(node1.getBlockchain());

        System.out.println("\nFinal Blockchain State for Node 2:");
        System.out.println(node2.getBlockchain());
    }

    private static Transaction[] generateRandomTransactions(int count) {
        Transaction[] transactions = new Transaction[count];
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            String sender = "User" + rand.nextInt(1000);
            String receiver = "User" + rand.nextInt(1000);
            double amount = rand.nextDouble() * 1000;
            transactions[i] = new Transaction(sender, receiver, amount);
        }
        return transactions;
    }
}