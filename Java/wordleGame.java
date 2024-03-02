////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Author: Thomas Preston
//
// wordList.txt is a sublist of https://github.com/dwyl/english-words/blob/master/words_alpha.txt
//
////////////////////////////////////////////////////////////////////////////////////////////////////

// Imports
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

// Tuples for returning 2 values at once
final class LoadTuple {
    public String word;
    public String[] data;

    public LoadTuple(String word, String[] data) {
        this.word = word;
        this.data = data;
    }
}

final class CheckTuple {
    public boolean correct;
    public char[] guessedLetters;

    public CheckTuple(boolean correct, char[] guessedLetters) {
        this.correct = correct;
        this.guessedLetters = guessedLetters;
    }
}


public class wordleGame {
    public static void main(String[] args) {
        boolean correct = false;
        int lives = 6;
        char[] guessedLetters = new char[26];
        Arrays.fill(guessedLetters, ' ');
        instructions();
        LoadTuple loadTuple = loadWord();
        String word = loadTuple.word;
        String[] data = loadTuple.data;
        Scanner getLine = new Scanner(System.in);
        while (correct == false && lives > 0) {
            String guess = getGuess(data, lives, guessedLetters, getLine);
            CheckTuple checkTuple = checkGuess(guess, word, guessedLetters);
            correct = checkTuple.correct;
            guessedLetters = checkTuple.guessedLetters;
            lives--;
        }
        getLine.close();
        if (correct == true) {
            System.out.println("You guessed the word correctly - Well done");
        }
        else {
            System.out.println("You loose - the correct word was " + word);
        }
    }

    public static void instructions() {
        System.out.println("Wordle:");
        System.out.println("After entering your guess, symbols will appear underneath to indicate which letters are correct");
        System.out.println("For example:");
        System.out.println("apple");
        System.out.println("-^  ^");
        System.out.println("This means the 'a' is in the correct spot and the 'p' and 'e' are in the word but not in those spots");
        System.out.println("It is important to note that this shows there is only 1 'p' in the word");
        System.out.println("At any point during the game, you can type in 'info' or 'i' to get number of lives and already guessed letters");
    }

    public static LoadTuple loadWord() {
        int noOfLines = 0;
        try (Scanner scanner = new Scanner(new FileReader("words_alpha.txt"))) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                noOfLines++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String[] data = new String[noOfLines];
        try {
            File file = new File("words_alpha.txt");
            Scanner fileReader = new Scanner(file);
            int counter = 0;
            while (fileReader.hasNextLine()) {
                String word = fileReader.nextLine();
                data[counter] = word;
                counter++;
            }
            fileReader.close();
            Arrays.sort(data);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Random rand = new Random();
        int wordPos = rand.nextInt(noOfLines);
        String word = data[wordPos];

        return new LoadTuple(word, data);
    }

    public static String getGuess(String[] data, int lives, char[] guessedLetters, Scanner getLine) {
        String guess;
        while (true) {
            System.out.println("Enter a 5 letter word:");
            guess = getLine.next().toLowerCase();
            if (guess.equals("info") || guess.equals("i")) {
                info(lives, guessedLetters);
                continue;
            }
            if (guess.length() != 5) {
                System.out.println("Needs to be only 5 letters");
                continue;
            }
            int index = Arrays.binarySearch(data, guess);
            if (index < 0) {
                System.out.println("Word not in word list");
                continue;
            }
            break;
        }
        return guess;
    }

    public static void info(int lives, char[] guessedLetters) {
        System.out.println("Lives: " + String.valueOf(lives));
        System.out.println("abcdefghijklmnopqrstuvwxyz");
        System.out.println(new String(guessedLetters));
        System.out.println("");
    }

    public static CheckTuple checkGuess(String guess, String word, char[] guessedLetters) {
        if (guess.equals(word)) {
            return new CheckTuple(true, guessedLetters);
        }
        char[] guessArray = guess.toCharArray();
        char[] wordArray = word.toCharArray();
        char[] puzzleArray = new char[5];
        Arrays.fill(puzzleArray, ' ');
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            char letter = guessArray[i];
            if (letter == wordArray[i]) {
                puzzleArray[i] = wordArray[i] = guessedLetters[(int)letter - (int)'a'] = '-';
                counter++;
                continue;
            }
            int counter2 = 0;
            boolean finishedLoop = true;
            for (char s : wordArray) {
                if (letter == s) {
                    puzzleArray[i] = wordArray[counter2] = guessedLetters[(int)letter - (int)'a'] = '^';
                    counter++;
                    finishedLoop = false;
                    break;
                }
                counter2++;
            }
            if (finishedLoop == true) {
                if (guessedLetters[(int)letter - (int)'a'] == ' ') {
                    guessedLetters[(int)letter - (int)'a'] = 'X';
                }
            }
        }
        if (counter > 0) {
            System.out.println(new String(puzzleArray));
        }
        else {
            System.out.println("No letters were correct");
        }
        return new CheckTuple(false, guessedLetters);
    }
}