package io.inabsentia.distributedhangman.logic;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameLogic extends UnicastRemoteObject implements IGameLogic {

    public static final int MAXIMUM_LIFE = 6;

    private String word;
    private String hiddenWord;

    private int time = 0;
    private int life = MAXIMUM_LIFE;
    private int score = 0;

    private List<String> wordList;
    private List<Character> usedCharactersList;

    public GameLogic() throws RemoteException {
        wordList = new ArrayList<>();
        usedCharactersList = new ArrayList<>();
        reset();
        try {
            System.out.println("GameLogic registered: " + RemoteServer.getClientHost());
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }

    private void initWordList() {
        if (!wordList.isEmpty())
            return;

        wordList.add("river");
        wordList.add("baseball");
        wordList.add("intelligence");
        wordList.add("cat");
        wordList.add("currency");
        wordList.add("dollar");
        wordList.add("food");
        wordList.add("meal");
        wordList.add("actual");
        wordList.add("artificial");
        wordList.add("art");
        wordList.add("egg");
        wordList.add("music");
        wordList.add("giant");
        wordList.add("magic");
        wordList.add("horizontal");
        wordList.add("vertical");
        wordList.add("dragon");
        wordList.add("video");
        wordList.add("games");
        wordList.add("active");
        wordList.add("make");
        wordList.add("freezer");
        wordList.add("color");
        wordList.add("bottle");
        wordList.add("continent");
        wordList.add("britain");
        wordList.add("chess");
        wordList.add("system");
        wordList.add("mathematics");
        wordList.add("parallel");
        wordList.add("degree");
        wordList.add("school");
    }

    private String getRandomWord() {
        if (wordList == null)
            initWordList();

        return wordList.get(new Random().nextInt(wordList.size()));
    }

    private String createHiddenWord() {
        if (word == null)
            word = getRandomWord();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++)
            sb.append("*");
        hiddenWord = sb.toString();

        return hiddenWord;
    }

    private void useCharacter(char letter) {
        if (usedCharactersList.size() == 0) {
            usedCharactersList.add(letter);
            return;
        }

        for (int i = 0; i < usedCharactersList.size(); i++)
            if (usedCharactersList.get(i) == letter)
                return;

        usedCharactersList.add(letter);
    }

    private void removeCharacter(char letter) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                char[] charArray = hiddenWord.toCharArray();
                charArray[i] = letter;
                hiddenWord = new String(charArray);
            }
        }
    }

    @Override
    public boolean isWon() {
        if (word == null || hiddenWord == null)
            return false;

        for (int i = 0; i < word.length(); i++)
            if (word.charAt(i) != hiddenWord.charAt(i))
                return false;

        return true;
    }

    @Override
    public boolean isLost() {
        return life == 0;
    }

    @Override
    public String getUsedCharacters() {
        String usedCharactersString = "";
        for (int i = 0; i < usedCharactersList.size(); i++) {
            if (i + 1 < usedCharactersList.size())
                usedCharactersString += usedCharactersList.get(i) + ", ";
            else
                usedCharactersString += usedCharactersList.get(i);
        }
        return usedCharactersString;
    }

    @Override
    public void startTimer() {

    }

    @Override
    public int getTimeElapsed() {
        return time;
    }

    @Override
    public void stopAndResetTimer() {
        time = 0;
    }

    @Override
    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public void reset() {
        initWordList();
        word = getRandomWord();
        hiddenWord = createHiddenWord();
        stopAndResetTimer();
        life = MAXIMUM_LIFE;
        score = 0;
        usedCharactersList = new ArrayList<>();
    }

    @Override
    public int getLifeLeft() {
        return life;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getWordScore() {
        return hiddenWord.length();
    }

    @Override
    public String getUsedCharactersString() {
        StringBuilder sb = new StringBuilder();
        for (Character s : usedCharactersList)
            sb.append(s);
        return sb.toString();
    }

    @Override
    public String getHiddenWord() {
        return hiddenWord;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public void decreaseLife() {
        life -= 1;
    }

    @Override
    public boolean isCharGuessed(char character) {
        for (Character c : usedCharactersList)
            if (c == character) return true;
        return false;
    }

    @Override
    public boolean guess(char character) {
        useCharacter(character);

        if (word.contains(Character.toString(character))) {
            removeCharacter(character);
            return true;
        } else {
            return false;
        }
    }

}