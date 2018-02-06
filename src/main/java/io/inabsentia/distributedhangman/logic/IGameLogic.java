package io.inabsentia.distributedhangman.logic;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameLogic extends Remote {
    boolean isWon() throws RemoteException;
    boolean isLost() throws RemoteException;
    String getUsedCharacters() throws RemoteException;
    void startTimer() throws RemoteException;
    int getTimeElapsed() throws RemoteException;
    void stopAndResetTimer() throws RemoteException;
    void addScore(int score) throws RemoteException;
    void reset() throws RemoteException;
    int getLifeLeft() throws RemoteException;
    int getScore() throws RemoteException;
    int getWordScore() throws RemoteException;
    void decreaseLife() throws RemoteException;
    boolean isCharGuessed(char character) throws RemoteException;
    boolean guess(char character) throws RemoteException;
    String getUsedCharactersString() throws RemoteException;
    String getHiddenWord() throws RemoteException;
    String getWord() throws RemoteException;
}