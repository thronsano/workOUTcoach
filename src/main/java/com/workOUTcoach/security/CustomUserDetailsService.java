package com.workOUTcoach.security;

import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserModel userModel;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userModel.getUserByEmail(email);
        UserBuilder builder;

        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.disabled(false);
            builder.password(user.getPassword());
            String[] authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new);
            builder.authorities(authorities);
        } else
            throw new UsernameNotFoundException("User not found!");

        return builder.build();
    }

}
