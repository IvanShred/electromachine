package ru.shred.electromachine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.shred.electromachine.AuthorizedUser;
import ru.shred.electromachine.dao.UserDao;
import ru.shred.electromachine.model.User;

@Service("userService")
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь " + email + " не найден");
        }
        return new AuthorizedUser(user);
    }
}
