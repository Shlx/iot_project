package marvin.babyphone.security;

import android.util.Base64;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is used for AES decryption of incoming messages.
 */
public class AesCrypt {

    private final Cipher cipher;
    private final SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    private final static String PASSWORD = "1234567812345678";

    public AesCrypt() throws Exception {
        // hash password with SHA-256 and crop the output to 128-bit for key
        /*MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);*/

        byte[] keybytes = PASSWORD.getBytes("UTF-8");

        cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        key = new SecretKeySpec(keybytes, "AES");
        spec = getIV();
    }

    private AlgorithmParameterSpec getIV() {
        byte[] b = new byte[16];
        new Random().nextBytes(b);

        return new IvParameterSpec(b);
    }

    private void setIV(byte[] iv) throws Exception {
        spec = new IvParameterSpec(iv);
    }

    /**
     * Encrypt plain text using the password.
     *
     * @param plainText The plain text to be encrypted.
     * @return The encrypted text.
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        byte[] bytes = Base64.encode(encrypted, Base64.DEFAULT);

        return new String(bytes, "UTF-8");
    }

    /**
     * Decrypt an encrypted string.
     *
     * @param cryptedText The encrypted text to be decoded.
     * @return The decrypted text.
     * @throws Exception
     */
    public String decrypt(String cryptedText) throws Exception {

        int blockSize = cipher.getBlockSize();

        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        int messageLength = bytes.length - blockSize;

        // Get the IV from the first few bytes of the message
        byte[] iv = new byte[blockSize];
        System.arraycopy(bytes, 0, iv, 0, blockSize);
        setIV(iv);

        // The remaining bytes make up the message itself
        byte[] message = new byte[messageLength];
        System.arraycopy(bytes, blockSize, message, 0, messageLength);

        // Decrypt the message
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(message);

        return new String(decrypted, "UTF-8");
    }
}