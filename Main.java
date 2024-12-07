import java.io.IOException;

public class Main {
    private static final String DICTIONARY_FILE = "src/wordlist.10000.txt";
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <password>");
            return;
        }

        String password = args[0];
        System.out.println("Password: " + password);

        try {
            Dictionary dictionary1 = new Dictionary(1000, true, true);
            Dictionary dictionary2 = new Dictionary(1000, false, true);
            Dictionary dictionary3 = new Dictionary(20000, true, false);
            Dictionary dictionary4 = new Dictionary(20000, false, false);

            dictionary1.loadDictionary(DICTIONARY_FILE);
            dictionary2.loadDictionary(DICTIONARY_FILE);
            dictionary3.loadDictionary(DICTIONARY_FILE);
            dictionary4.loadDictionary(DICTIONARY_FILE);

            boolean isStrong = isStrongPassword(password, dictionary1);
            if (isStrong) {
                System.out.println("Password is strong");
            }
            else {
                System.out.println("Password is weak");
            }

            // Comparisons for SC
            System.out.println("Comparisons using Separate Chaining HashTable:");
            System.out.println("Comparisons for earlyHashCode: " + dictionary1.getComparisons(true));
            System.out.println("Comparisons for currentHashCode: " + dictionary1.getComparisons(false));
            System.out.println();

            // Comparisons for LP
            System.out.println("Comparisons using Linear Probing HashTable:");
            System.out.println("Comparisons for earlyHashCode: " + dictionary3.getComparisons(true));
            System.out.println("Comparisons for currentHashCode: " + dictionary4.getComparisons(false));

        } catch (IOException e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }
    }

    private static boolean isStrongPassword(String password, Dictionary dictionary) {
        if (password.length() < 8) return false;
        if (dictionary.contains(password)) return false;
        for (int i = 0; i < 10; i++) {
            if (password.endsWith(String.valueOf(i))) {
                String potentialWord = password.substring(0, password.length() - 1);
                if (dictionary.contains(potentialWord)) return false;
            }
        }
        return true;
    }
}
