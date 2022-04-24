import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DESEncryptionExample {
    private static Cipher encryptCipher;
    private static Cipher decryptCipher;
    private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };

    public static void main(String[] args) {
        String clearTextFile = "src/source.txt";
        String cipherTextFile = "src/cipher.txt";
        String clearTextNewFile = "src/source-new.txt";

        try {
            // создаем Секретный ключ с помощью  KeyGenerator
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            // получаем экземпляр Cipher и запускаем в режиме шифрования
            encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            // получаем экземпляр шифра и запускаем в режиме дешифрования
            decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            // шифрование открытого текстового файла в зашифрованный файл
            encrypt(new FileInputStream(clearTextFile), new FileOutputStream(cipherTextFile));

            // расшифровка зашифрованного файла в чистый текстовый файл
            decrypt(new FileInputStream(cipherTextFile), new FileOutputStream(clearTextNewFile));
            System.out.println("DONE");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException | IOException e) {
            e.printStackTrace();
        }

    }

    private static void encrypt(InputStream is, OutputStream os) throws IOException {

        // создаем CipherOutputStream для шифрования данных с помощью encrypt Cipher
        os = new CipherOutputStream(os, encryptCipher);
        writeData(is, os);
    }

    private static void decrypt(InputStream is, OutputStream os) throws IOException {

        // создаем CipherOutputStream для расшифровки данных с помощью decrypt Cipher
        is = new CipherInputStream(is, decryptCipher);
        writeData(is, os);
    }

    // служебный метод для чтения данных из входного потока и записи в выходной поток
    private static void writeData(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        int numRead = 0;
        // операция чтения и записи
        while ((numRead = is.read(buf)) >= 0) {
            os.write(buf, 0, numRead);
        }
        os.close();
        is.close();
    }

}