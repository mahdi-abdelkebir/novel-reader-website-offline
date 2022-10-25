package pj.service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pj.models.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	

    @Query("SELECT DISTINCT b FROM Bookmark b JOIN b.user u WHERE u.id = :user_id")
    List<Bookmark> findAll(@Param("user_id") Long user_id); // At
}
