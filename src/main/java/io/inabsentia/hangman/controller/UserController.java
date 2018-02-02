package io.inabsentia.hangman.controller;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class UserController implements IUserController {

    private final String RMI_URL = "rmi://javabog.dk/brugeradmin";

    private Brugeradmin brugerAdmin;
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
        if (brugerAdmin == null) {
            try {
                brugerAdmin = (Brugeradmin) Naming.lookup(RMI_URL);
            } catch (NotBoundException | RemoteException | MalformedURLException e) {
                throw new UserControllerException("Failed initializing RMI stub: " + RMI_URL);
            }
        }
    }

    public static synchronized IUserController getInstance() {
        return instance;
    }

    @Override
    public void signIn(String userName, String password) throws UserControllerException {
        try {
            currentUser = brugerAdmin.hentBruger(userName, password);
        } catch (Exception e) {
            throw new UserControllerException("Sign In failed!");
        }
    }

    @Override
    public void signOut() throws UserControllerException {
        if (currentUser == null)
            throw new UserControllerException("Not signed in!");
        currentUser = null;
    }

    @Override
    public boolean isSignedIn() {
        return currentUser != null;
    }

    @Override
    public Bruger getUser() throws UserControllerException {
        if (currentUser == null)
            throw new UserControllerException("Not signed in!");
        return currentUser;
    }

}