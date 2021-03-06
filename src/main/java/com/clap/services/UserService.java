package com.clap.services;

import com.clap.model.ArtisticContent;
import com.clap.model.User;
import com.clap.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoggedUser() {
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username="";
    if (principal instanceof UserDetails) {
        username = ((UserDetails)principal).getUsername();
    } else {
        username = "null";
    }
    return username;
    }

    public User register(User user) throws Exception {
        Optional<User> userFound = userRepository.findByUsername(user.getUsername());
        if (userFound.isPresent()) {
            throw new Exception("Username already registered!");
        } else {
            encryptPassword(user);
            user.setCreatedDate(new Date());
            return userRepository.save(user);
        }
    }

    private void encryptPassword(User user) {
        user.setPassword(getEncoder().encode(user.getPassword()));
    }

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User setUserArtisticContent (String title, ArtisticContent content){
        User user = userRepository.getOwnerByTitle(title);
        content.setOwner(user);
        return null;
    }

}
