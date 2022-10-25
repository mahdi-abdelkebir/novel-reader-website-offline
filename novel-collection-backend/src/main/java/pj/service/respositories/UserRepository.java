package pj.service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pj.models.Novel;
import pj.models.Reading;
import pj.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findOneByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);

}
