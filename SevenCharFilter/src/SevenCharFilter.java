import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class SevenCharFilter {
    public static void main(String[] args) {
        String url = "https://gist.githubusercontent.com/Konstantinusz/f9517357e46fa827c3736031ac8d01c7/raw/fc98429a6357d1c4fcc644e1b70c2431bd046cf0/magyar-szavak.txt";

        Set<Character> validCharacters = new HashSet<>(Arrays.asList('a', 'á', 'b', 'e', 'é', 's', 'z', 'g', 'o', 'ó', 'i', 'í', 'l'));
        List<String> sevenCharacterWords = getSevenCharacterWords(url, validCharacters);

        if (sevenCharacterWords.isEmpty()) {
            System.out.println("No seven-character words with the specified criteria found.");
        } else {
            List<String> randomWords = getRandomSubset(sevenCharacterWords, 100);
            displayWordsInUpperCaseWithoutAccentsWithReplacements(randomWords);
        }
    }

    public static List<String> getSevenCharacterWords(String url, Set<Character> validCharacters) {
        List<String> sevenCharacterWords = new ArrayList<>();
        try {
            URL wordListUrl = new URL(url);
            Scanner scanner = new Scanner(wordListUrl.openStream());

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                if (word.length() == 7 && (validCharacters.contains(word.charAt(2)) && validCharacters.contains(word.charAt(3)))) {
                    sevenCharacterWords.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sevenCharacterWords;
    }

    public static List<String> getRandomSubset(List<String> words, int count) {
        List<String> randomSubset = new ArrayList<>();
        Random random = new Random();

        while (randomSubset.size() < count && words.size() > 0) {
            String word = words.get(random.nextInt(words.size()));
            randomSubset.add(word);
            words.remove(word);
        }

        return randomSubset;
    }

    public static void displayWordsInUpperCaseWithoutAccentsWithReplacements(List<String> words) {
        Map<Character, Character> replacements = createReplacements();
        for (String word : words) {
            String modifiedWord = removeAccents(word).toUpperCase();
            char thirdChar = modifiedWord.charAt(2);
            char fourthChar = modifiedWord.charAt(3);

            if (replacements.containsKey(thirdChar)) {
                modifiedWord = modifiedWord.substring(0, 2) + replacements.get(thirdChar) + modifiedWord.substring(3);
            }
            if (replacements.containsKey(fourthChar)) {
                modifiedWord = modifiedWord.substring(0, 3) + replacements.get(fourthChar) + modifiedWord.substring(4);
            }

            System.out.println(modifiedWord);
        }
    }

    public static Map<Character, Character> createReplacements() {
        Map<Character, Character> replacements = new HashMap<>();
        replacements.put('O', '0');
        replacements.put('I', '1');
        replacements.put('B', '8');
        replacements.put('E', '3');
        replacements.put('G', '6');
        replacements.put('L', '1');
        replacements.put('Z', '5');
        replacements.put('S', '2');
        replacements.put('A', '4');
        return replacements;
    }

    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}