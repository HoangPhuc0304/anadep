package com.hps.anadep.security;

import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        AuthToken authToken = authTokenRepository.findByGithubToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticate failed"));

        return AppUser.build(authToken.getUser());
    }
}
