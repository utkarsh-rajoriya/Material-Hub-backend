package com.utkarsh.MaterialHub.Services;

import com.utkarsh.MaterialHub.daos.UserRepo;
import com.utkarsh.MaterialHub.models.User;
import com.utkarsh.MaterialHub.models.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username); // or findByEmail
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new UserPrincipal(user.orElse(null));
    }
}
