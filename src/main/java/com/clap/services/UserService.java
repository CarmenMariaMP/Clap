package com.clap.services;

import com.clap.model.ArtisticContent;
import com.clap.model.User;
import com.clap.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder crypt;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.disabled(false);
            builder.password(user.get().getPassword());
            builder.authorities(user.get().getAuthorities());

        } else {
            throw new UsernameNotFoundException("User not found");
        }
        return builder.build();
    }

    public User register(User user) throws Exception {
        Boolean userExist = userExists(user);
        if (userExist) {
            throw new Exception("Username already registered!");
        } else {
            user.setPassword(crypt.encode(user.getPassword()));
            user.setCreatedDate(new Date());
            return userRepository.save(user);
        }
    }

    public User setUserArtisticContent(String title, ArtisticContent content) {
        User user = userRepository.getOwnerByTitle(title);
        content.setOwner(user);
        return null;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public String getTypeByUsername(String username) {
        return userRepository.getTypeByUsername(username);
    }

    public String getTypeById(String user_id) {
        return userRepository.getTypeById(user_id);
    }

    public Boolean userExists(User user) {
        Boolean userExist = false;
        Optional<User> userFound = getUserByUsername(user.getUsername());
        if (userFound.isPresent()) {
            userExist = true;
        }
        return userExist;
    }

    public String getLoggedUser() {
        String username="";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.print(principal);
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = null;
        }
        return username;
    }

}
