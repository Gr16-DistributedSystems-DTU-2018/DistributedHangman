package io.inabsentia.hangman.controller;

import brugerautorisation.data.Bruger;

public interface IUserController {
    void signIn(String userName, String password) throws UserControllerException;
    void signOut() throws UserControllerException;
    boolean isSignedIn();
    Bruger getUser() throws UserControllerException;
}