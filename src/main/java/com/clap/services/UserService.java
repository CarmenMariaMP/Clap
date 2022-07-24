package com.clap.services;

import com.clap.model.ArtisticContent;
import com.clap.model.User;
import com.clap.repository.UserRepository;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder crypt;

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

    public void save(User user) {
        user.setPassword(crypt.encode(user.getPassword()));
        userRepository.save(user);


   }

    public User setUserArtisticContent (String title, ArtisticContent content){
        User user = userRepository.getOwnerByTitle(title);
        content.setOwner(user);
        return null;
    }
}
