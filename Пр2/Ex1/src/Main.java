import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        try {
            // Генерація ключових пар для користувачів
            KeyPair aliceKeyPair = generateKeyPair();
            KeyPair bobKeyPair = generateKeyPair();
            KeyPair charlieKeyPair = generateKeyPair();

            // Створюємо блокчейн
            Blockchain bitcoin = new Blockchain();

            // Створюємо транзакції з кількома вихідними адресами
            List<String> outputs1 = Arrays.asList("Bob", "Charlie");
            Transaction tx1 = new Transaction("Alice", outputs1, 50);
            String signature1 = signData(tx1.getTxHash(), aliceKeyPair.getPrivate());
            tx1.setSignature(signature1);

            List<String> outputs2 = Arrays.asList("Charlie");
            Transaction tx2 = new Transaction("Bob", outputs2, 30);
            String signature2 = signData(tx2.getTxHash(), bobKeyPair.getPrivate());
            tx2.setSignature(signature2);

            // Додаємо транзакції до списку
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(tx1);
            transactions.add(tx2);

            // Виводимо інформацію про транзакції
            System.out.println("Транзакції:");
            for (Transaction tx : transactions) {
                System.out.println(tx);
                System.out.println("Чи транзакція валідна? " + tx.verifyTransaction());
                System.out.println("Чи підпис валідний? " + verifySignature(tx.getInput(), tx.getTxHash(), tx.getSignature()));
                System.out.println();
            }

            // Створюємо новий блок
            Block newBlock = new Block(1, bitcoin.getLatestBlock().getBlockHash(), 4, transactions);
            newBlock.mineBlock(newBlock.getDifficultyTarget());  // Майним блок зі складністю

            // Підписуємо блок
            String blockSignature = signData(newBlock.getBlockHash(), aliceKeyPair.getPrivate());
            newBlock.setSignature(blockSignature);

            // Верифікуємо блок перед додаванням до блокчейну
            if (newBlock.verifyBlock() && verifySignature(newBlock.getPrevHash(), newBlock.getBlockHash(), newBlock.getSignature())) {
                // Додаємо блок до блокчейну
                bitcoin.addBlock(newBlock);
                System.out.println("Блок додано до блокчейну.");
            } else {
                System.out.println("Блок не пройшов верифікацію і не буде доданий до блокчейну.");
            }

            // Виводимо блокчейн
            System.out.println("\nБлокчейн:");
            System.out.println(bitcoin);

            // Перевірка валідності блокчейну
            System.out.println("Чи блокчейн валідний? " + bitcoin.isChainValid());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для генерації ключової пари (RSA)
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    // Метод для підпису даних приватним ключем
    public static String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signer.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Метод для верифікації підпису даних публічним ключем
    public static boolean verifySignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return verifier.verify(signatureBytes);
    }

    // Перевантажений метод для спрощеної верифікації підпису транзакції
    public static boolean verifySignature(String sender, String data, String signature) throws Exception {
        // В реальній системі ми б отримали публічний ключ за адресою відправника
        // Для прикладу, ми припустимо, що sender відповідає певному ключу
        // Тут ми просто повернемо true для демонстрації
        // Ми можемо розширити цю частину відповідно до іншої логіки управління ключами
        return true;
    }
}
