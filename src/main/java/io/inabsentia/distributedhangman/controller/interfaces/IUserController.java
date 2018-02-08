package io.inabsentia.distributedhangman.controller.interfaces;

import brugerautorisation.data.Bruger;
import io.inabsentia.distributedhangman.controller.UserController;
import io.inabsentia.distributedhangman.controller.exceptions.UserControllerException;

public interface IUserController {
    void signIn(String userName, String password) throws UserControllerException;
    void signOut() throws UserControllerException;
    boolean isSignedIn();
    Bruger getUser() throws UserControllerException;
    String getUserField(String username, String password, String userFieldKey) throws UserControllerException;
    void setUserField(String username, String password, String userFieldKey, String value) throws UserControllerException;
    boolean isHighScore(int score) throws UserControllerException;
}