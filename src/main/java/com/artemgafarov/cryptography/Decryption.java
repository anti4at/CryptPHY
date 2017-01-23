package com.artemgafarov.cryptography;

import com.artemgafarov.cryptography.Exceptions.InitException;
import com.artemgafarov.cryptography.Resources.SecretKey;

/**
 * Created by fada on 22.01.17.
 */
public class Decryption {
    private static String encryptedText;
    private static String key;
    private static Character[][] characters;
    private static Character[] symbols;
    private static Character specCharacter;

    public static void setSpecCharacter(Character specCharacter) {
        Decryption.specCharacter = specCharacter;
    }

    public static void setEncryptedText(String encryptedText) {
        Decryption.encryptedText = new StringBuilder(encryptedText).reverse().toString();
    }

    public static void setKey(String key) {
        Decryption.key = key;
    }

    public static void _init(){
        SecretKey.setKey(key);
        SecretKey.setTextLength(encryptedText.length());
        SecretKey._init();
        SecretKey.generateColumns();
        characters = SecretKey.getTextMatrix();
        symbols = SecretKey.getSymbol();
    }

    public static String decrypt() throws InitException {

        if(characters == null || symbols == null){
            throw new InitException("Initialization Error. Try to use _init()");
        }

        String decryptedData = "";

        int f = 0;
        int b = 0;

        int secretNumber = 0;
        int len = key.length();

        for (int i = 0; i < len; i++){
            for (int j = 0; j < i; j++){
                secretNumber += ((int)key.charAt(i)  + (int)key.charAt(j) + len - i + j);
                f += secretNumber;
            }
        }



        for (int i = 0; i < symbols.length; i++){
            f = 0;
            for (int j = 0; j < key.length(); j++){
                if (symbols[i] == key.toLowerCase().charAt(j)){
                    for (int a = 0; a < characters.length; a++){
                        if(b >= encryptedText.length())
                            break;
                        characters[a][f] = (char)((int)encryptedText.charAt(b) - secretNumber - f - (int)key.charAt(0) - (int)specCharacter);
                        b++;
                    }
                }
                f++;
            }
        }

        for (int i = 0; i < characters.length; i++){
            for (int j = 0; j < characters[i].length; j++){

                if (characters[i][j] == null)
                    continue;

                decryptedData += characters[i][j];
                if (decryptedData.charAt(decryptedData.length() - 1) == specCharacter){
                    decryptedData = decryptedData.substring(0, decryptedData.length()-1);
                    break;
                }
            }
        }

        decryptedData.trim();

        return decryptedData;
    }
}
