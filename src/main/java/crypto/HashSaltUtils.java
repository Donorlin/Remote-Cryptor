package crypto;

import jdk.nashorn.internal.runtime.arrays.IteratorAction;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class HashSaltUtils {

    private static int SALT_BYTE_LENGTH = 8;
    private static int PBKDF2_ITERATION_COUNT = 500000;
    private static String HASH_TRANSFORMATION = "PBKDF2WithHmacSHA256";

    private static SecureRandom RANDOM = new SecureRandom();


    public static byte[] getNextSalt() {
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] getPasswordSaltHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        long start = System.nanoTime();
        char[] pw = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(pw, salt, PBKDF2_ITERATION_COUNT, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_TRANSFORMATION);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        long end = System.nanoTime();
        System.out.println("Hashed in " + ((end - start) / 1_000_000_000.0));
        return hash;
    }

}
