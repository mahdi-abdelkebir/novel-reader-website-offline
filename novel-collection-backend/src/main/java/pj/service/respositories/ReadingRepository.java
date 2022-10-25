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

public interface ReadingRepository extends JpaRepository<Reading, String> {
	
    @Query("SELECT DISTINCT r FROM Reading r join r.compositeKey.user join r.compositeKey.novel WHERE r.compositeKey.user.id = :id")
    List<Reading> findAll(@Param("id") Long id);


    @Query(value = "SELECT DISTINCT r FROM Reading r WHERE r.compositeKey.user.id = :user_id AND r.compositeKey.novel.id = :novel_id")
    Optional<Reading> getNovel(@Param("user_id") Long user_id, @Param("novel_id") Long novel_id);

    @Modifying
    @Query(value = "INSERT INTO reading(user_id, novel_id) values (:user_id, :novel_id)", nativeQuery = true)
    void addNovel(@Param("user_id") Long user_id, @Param("novel_id") Long novel_id);
    
    @Modifying
    @Query(value = "UPDATE reading SET status = :status, chapter = :chapter  WHERE user_id = :user_id AND novel_id = :novel_id", nativeQuery = true)
	void change(Long user_id, Long novel_id, String status, Integer chapter);
    
    @Modifying
    @Query(value = "UPDATE reading SET chapter = :chapter  WHERE user_id = :user_id AND novel_id = :novel_id", nativeQuery = true)
	void change(Long user_id, Long novel_id, Integer chapter);
    
    @Modifying
    @Query(value = "DELETE FROM reading WHERE user_id = :user_id AND novel_id = :novel_id", nativeQuery = true)
	void removeNovel(Long user_id, Long novel_id);
}
