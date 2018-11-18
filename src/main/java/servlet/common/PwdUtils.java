package servlet.common;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PwdUtils {

    private static final String strongPwdRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static boolean isPwdStrong(String password) {
        return password.matches(strongPwdRegex);
    }

    public static boolean isPwdInDictionary(String password) {
        Set<String> dictionary = new HashSet<>();
        try (Scanner scan = new Scanner(PwdUtils.class.getClassLoader().getResourceAsStream("pwd_dictionary.txt"))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                dictionary.add(line);
            }
        }
        return dictionary.contains(password);
    }
}
