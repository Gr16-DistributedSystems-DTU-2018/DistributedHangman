package io.inabsentia.distributedhangman.controller;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import io.inabsentia.distributedhangman.controller.exceptions.UserControllerException;
import io.inabsentia.distributedhangman.controller.interfaces.IUserController;
import io.inabsentia.distributedhangman.util.Utils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class UserController implements IUserController {

    private Brugeradmin rmiController;
    private Bruger currentUser;

    private static IUserController instance;

    static {
        try {
            instance = new UserController();
        } catch (Exception e) {
            throw new RuntimeException("Fatal error creating Singleton instance!");
        }
    }

    private UserController() throws UserControllerException {
        if (rmiController == null) {
            try {
                rmiController = (Brugeradmin) Naming.lookup(Utils.RMI_STUB_URL);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                throw new UserControllerException("Failed initializing RMI stub: " + Utils.RMI_STUB_URL);
            }
        }
    }

    public static synchronized IUserController getInstance() {
        return instance;
    }

    @Override
    public void signIn(String userName, String password) throws UserControllerException {
        try {
            currentUser = rmiController.hentBruger(userName, password);
        } catch (Exception e) {
            throw new UserControllerException("Sign In failed!");
        }
    }

    @Override
    public void signOut() throws UserControllerException {
        if (!isSignedIn())
            throw new UserControllerException("Not signed in!");
        currentUser = null;
    }

    @Override
    public boolean isSignedIn() {
        return currentUser != null;
    }

    @Override
    public Bruger getUser() throws UserControllerException {
        if (!isSignedIn())
            throw new UserControllerException("Not signed in!");
        return currentUser;
    }

    @Override
    public String getUserField(String username, String password, String userFieldKey) throws UserControllerException {
        try {
            Object userField = rmiController.getEkstraFelt(username, password, userFieldKey);
            String userFieldString = (String) userField;

            if (userFieldKey == Utils.HIGH_SCORE_FIELD_KEY) {
                if (userFieldString == null || userFieldString == "null")
                    userFieldString = "0";
            }

            return userFieldString;
        } catch (RemoteException e) {
            throw new UserControllerException("No user field found at key '" + userFieldKey + "!");
        }
    }

    @Override
    public void setUserField(String username, String password, String userFieldKey, String value) throws UserControllerException {
        try {
            rmiController.setEkstraFelt(username, password, userFieldKey, value);
        } catch (RemoteException e) {
            throw new UserControllerException("Failed setting user field '" + value + "' at key '" + userFieldKey + "!");
        }
    }

}