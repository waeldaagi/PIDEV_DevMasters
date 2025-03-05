package interfaces;

import models.User;

public interface IUserInterface {
    User signIn(String usernameOrEmail, String password);
}
