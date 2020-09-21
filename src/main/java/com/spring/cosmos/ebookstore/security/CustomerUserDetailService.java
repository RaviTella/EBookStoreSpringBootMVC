package com.spring.cosmos.ebookstore.security;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.user.User;
import com.spring.cosmos.ebookstore.model.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private UserRepository userRepository;

    @Autowired
    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository
                    .findById(email, new PartitionKey(email))
                    .orElseThrow(() -> {
                        return new UsernameNotFoundException(email);
                    });
        } catch(UsernameNotFoundException e) {
            logger.error(e.toString());
            throw e;
        }
        return new SecuredUser(user);
    }
}
