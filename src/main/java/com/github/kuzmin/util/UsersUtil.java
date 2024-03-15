package com.github.kuzmin.util;

import com.github.kuzmin.model.Role;
import lombok.experimental.UtilityClass;
import com.github.kuzmin.model.User;
import com.github.kuzmin.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static UserTo createToWithoutPassword(User user) {
        return new UserTo(user.id(), user.getName(), user.getEmail(), null);
    }
}