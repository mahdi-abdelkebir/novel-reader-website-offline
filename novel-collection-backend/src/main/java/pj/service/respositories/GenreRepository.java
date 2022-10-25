package pj.service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pj.models.Genre;
import pj.models.Novel;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, String> {
	
    @Override
    @Query("SELECT DISTINCT g FROM Genre g")
    List<Genre> findAll(); 
    
}
