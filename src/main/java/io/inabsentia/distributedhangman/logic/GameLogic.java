package io.inabsentia.distributedhangman.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {

    public static final int MAXIMUM_LIFE = 7;

    private String word;
    private String hiddenWord;

    private int life = MAXIMUM_LIFE;
    private int score = 0;

    private List<String> wordList;
    private List<Character> usedLettersList;

    private static GameLogic instance;

    static {
        try {
            instance = new GameLogic();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton instance!");
        }
    }

    private GameLogic() {
        wordList = new ArrayList<>();
        usedLettersList = new ArrayList<>();
        initLogic();
    }

    public static synchronized GameLogic getInstance() {
        return instance;
    }

    private void initLogic() {
        initWordList();
        word = getRandomWord();
        hiddenWord = createHiddenWord();

    }

    private void initWordList() {
        wordList.add("river");
        wordList.add("baseball");
        wordList.add("intelligence");
        wordList.add("cat");
        wordList.add("currency");
        wordList.add("dollar");
        wordList.add("food");
        wordList.add("meal");
    }

    private String getRandomWord() {
        if (wordList == null)
            initWordList();

        return wordList.get(new Random().nextInt(wordList.size()));
    }

    private String createHiddenWord() {
        if (word == null)
            word = getRandomWord();

        for (int i = 0; i < word.length(); i++)
            hiddenWord += "*";

        return hiddenWord;
    }

    private void useLetter(char letter) {
        if (usedLettersList.size() == 0) {
            usedLettersList.add(letter);
            return;
        }

        for (int i = 0; i < usedLettersList.size(); i++)
            if (usedLettersList.get(i) == letter)
                return;

        usedLettersList.add(letter);
    }

    private void removeLetter(char letter) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                char[] charArray = hiddenWord.toCharArray();
                charArray[i] = letter;
                hiddenWord = new String(charArray);
            }
        }
    }

    public boolean isWon() {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    public boolean isDead() {
        return life == 0;
    }

    public String getUsedLetters() {
        String usedLettersString = "";
        for (int i = 0; i < usedLettersList.size(); i++) {
            if (i + 1 < usedLettersList.size())
                usedLettersString += usedLettersList.get(i) + ", ";
            else
                usedLettersString += usedLettersList.get(i);
        }
        return usedLettersString;
    }

    public void startTimer() {

    }

    public int getTime() {
        return 0;
    }

    public void stopAndResetTimer() {

    }

    public int getLifeLeft() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public String getUsedLettersString() {
        StringBuilder sb = new StringBuilder();
        for (Character s : usedLettersList)
            sb.append(s);
        return sb.toString();
    }

    public String getHiddenWord() {
        return hiddenWord;
    }


}