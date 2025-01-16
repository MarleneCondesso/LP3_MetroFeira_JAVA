package com.subway2feira.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 * Reutilização do código da página.
 * 
 * @author howtodoinjava.com
 */
public class PasswordEncryption {

    /**
     * Função para encriptar palavra pass com salt gerado
     * 
     * @param pw   palavra
     * @param salt salt
     * @return devolve a pass encriptada
     */
    public String getEncryptionPw(String pw, byte[] salt) {
        String encryptedPassword = null;
        try {

            // iniciacao da instancia para encriptar em SHA-256

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // adicionar salt
            md.update(salt);

            byte[] bytes = md.digest(pw.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }

    /**
     * funcao para gerar salt
     * 
     * @return salt gerado
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

        byte[] salt = new byte[16];

        sr.nextBytes(salt);

        return salt;
    }

    /**
     * 
     * Converte salt de byte[] em String
     * 
     * @param salt salt
     * @return Salt em String
     */
    public String saltToString(byte[] salt) {
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 
     * Convert salt String em byte[]
     * 
     * @param saltString string salt
     * @return Salt em byte[]
     */
    public byte[] stringToSalt(String saltString) {
        return Base64.getDecoder().decode(saltString);
    }
}
