package pj.service.abstr;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsServiceInterface {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}