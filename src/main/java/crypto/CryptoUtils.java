package crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtils {
    private static String SYMMETRIC_ALGORITHM = "AES";
    private static Integer SECRET_KEY_LENGTH_BITS = 256;
    private static Integer SECRET_KEY_LENGTH_BYTES = SECRET_KEY_LENGTH_BITS / 8;
    private static Integer IV_LENGTH_BYTES = 16;
    private static String SYMMETRIC_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private static String ASYMMETRIC_ALGORITHM = "RSA";
    private static String ASYMMETRIC_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private static Key getRandomSymetricKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
        keyGen.init(SECRET_KEY_LENGTH_BITS);
        return keyGen.generateKey();
    }

    private static IvParameterSpec getRandomIV() {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    private static PublicKey getPublicKey(File publicKeyFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(publicKeyFile);
        byte[] inputBytes = new byte[(int) publicKeyFile.length()];
        inputStream.read(inputBytes);
        inputStream.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(inputBytes);
        KeyFactory kf = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
        return kf.generatePublic(spec);
    }

    private static PrivateKey getPrivateKey(File privateKeyFile) throws Exception {
        FileInputStream inputStream = new FileInputStream(privateKeyFile);
        byte[] inputBytes = new byte[(int) privateKeyFile.length()];
        inputStream.read(inputBytes);
        inputStream.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(inputBytes);
        KeyFactory kf = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
        return kf.generatePrivate(spec);
    }


    public static void encrypt(File inputFile, File outputFile, File publicKeyFile) throws Exception {
        long start = System.nanoTime();
        // System.out.println("ENCRYPTION");
        try {
            // init symmetric cipher, key, iv
            Key secretKey = getRandomSymetricKey();
            IvParameterSpec ivSpec = getRandomIV();
            Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
            symmetricCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            // init asymmetric cipher, key
            PublicKey publicKey = getPublicKey(publicKeyFile);
            Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
            asymmetricCipher.init(Cipher.PUBLIC_KEY, publicKey);

            // encrypt iv, key with asymmetric cipher
            byte[] iv = ivSpec.getIV();
            byte[] key = secretKey.getEncoded();
            byte[] ivAndKey = new byte[IV_LENGTH_BYTES + SECRET_KEY_LENGTH_BYTES];
            System.arraycopy(iv, 0, ivAndKey, 0, IV_LENGTH_BYTES);
            System.arraycopy(key, 0, ivAndKey, IV_LENGTH_BYTES, SECRET_KEY_LENGTH_BYTES);
            // System.out.println("IVKEY - raw = " + ivAndKey.length);
            byte[] encryptedIvAndKey = asymmetricCipher.doFinal(ivAndKey);
            // System.out.println("IVKEY - encrypted = " + encryptedIvAndKey.length);

            // create header string containing byte size of iv and key, + add line separator
            Integer encryptedIvAndKeyLength = encryptedIvAndKey.length;
            String encryptedIvAndKeyLengthString = encryptedIvAndKeyLength.toString() + "\n";
            // System.out.println(encryptedIvAndKeyLengthString);
            // System.out.println("Header - bytes = " + encryptedIvAndKeyLengthString.getBytes(StandardCharsets.UTF_8).length);


            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(encryptedIvAndKeyLengthString.getBytes(StandardCharsets.UTF_8));
            outputStream.write(encryptedIvAndKey);

            // encrypt file in chunks
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] buffer = new byte[8192];
            int count;
            int sum = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                sum += count;
                byte[] encrypted = symmetricCipher.update(buffer, 0, count);
                outputStream.write(encrypted);
            }
            // System.out.println("OUTPUT - encrypted file size = " + sum);
            byte[] encrypted = symmetricCipher.doFinal();
            outputStream.write(encrypted);

            inputStream.close();
            outputStream.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException ex) {
            throw ex;
        }
        // System.out.println("END - ENCRYPTION");
        long end = System.nanoTime();
        System.out.println("Encrypted in " + ((end - start) / 1_000_000_000.0));
    }

    private static Integer getEncryptedIvAndKeyByteLength(File inputFile) throws Exception {
        Integer encryptedIvAndKeyByteLength = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line = br.readLine();
            encryptedIvAndKeyByteLength = Integer.parseInt(line);
        }
        return encryptedIvAndKeyByteLength;
    }

    public static void decrypt(File inputFile, File outputFile, File privateKeyFile) throws Exception {
        long start = System.nanoTime();
        System.out.println("DECRYPTION");
        try {
            // init asymmetric cipher, key
            PrivateKey privateKey = getPrivateKey(privateKeyFile);
            Cipher asymmetricCipher = Cipher.getInstance(ASYMMETRIC_TRANSFORMATION);
            asymmetricCipher.init(Cipher.PRIVATE_KEY, privateKey);

            // get byte length of iv and key from header
            Integer encryptedIvAndKeyLengthBytes = getEncryptedIvAndKeyByteLength(inputFile);
            // System.out.println("HEADER = encryptedIvAndKeyLengthBytes = " + encryptedIvAndKeyLengthBytes);

            // compute offset (header length in bytes = iv, key, line separator)
            Integer OFFSET = (encryptedIvAndKeyLengthBytes + "\n").getBytes(StandardCharsets.UTF_8).length;
            // System.out.println("HEADER = bytes = " + OFFSET);

            // read encrypted file
            FileInputStream inputStream = new FileInputStream(inputFile);

            byte[] trashOffset = new byte[OFFSET];
            inputStream.read(trashOffset);

            byte[] encryptedIvAndKey = new byte[encryptedIvAndKeyLengthBytes];
            inputStream.read(encryptedIvAndKey);
            byte[] decryptedIvAndKey = asymmetricCipher.doFinal(encryptedIvAndKey);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            byte[] key = new byte[SECRET_KEY_LENGTH_BYTES];
            System.arraycopy(decryptedIvAndKey, 0, iv, 0, IV_LENGTH_BYTES);
            System.arraycopy(decryptedIvAndKey, IV_LENGTH_BYTES, key, 0, SECRET_KEY_LENGTH_BYTES);
            // System.out.println("IV - decrypted = " + iv.length);
            // System.out.println("KEY - decrypted = " + key.length);
            decryptedIvAndKey = null;

            Key secretKey = new SecretKeySpec(key, SYMMETRIC_ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher symmetricCipher = Cipher.getInstance(SYMMETRIC_TRANSFORMATION);
            symmetricCipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[8192];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                byte[] decrypted = symmetricCipher.update(buffer, 0, count);
                outputStream.write(decrypted);
            }
            byte[] decrypted = symmetricCipher.doFinal();
            outputStream.write(decrypted);

            inputStream.close();
            outputStream.close();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException ex) {
            throw ex;
        }
        // System.out.println("END - DECRYPTION");
        long end = System.nanoTime();
        System.out.println("Decrypted in " + ((end - start) / 1_000_000_000.0));
    }
}