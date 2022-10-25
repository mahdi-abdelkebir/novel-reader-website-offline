package pj.service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pj.models.Genre;
import pj.models.Novel;

import java.util.List;
import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {
	
    @Override
    @Query("SELECT DISTINCT n FROM Novel n JOIN n.genres genre")
    List<Novel> findAll();
    
    // NORMAL SEARCH (BY slug)
    @Query("SELECT DISTINCT n FROM Novel n WHERE n.slug = :slug")
    Optional<Novel> findBySlug(@Param("slug") String slug);
 
    // NORMAL SEARCH (BY title)
    @Query("SELECT DISTINCT n FROM Novel n JOIN n.genres genre WHERE lower(n.title) LIKE lower(concat('%', :title,'%'))")
    List<Novel> findByTitle(@Param("title") String title);
    
    // CONTAINS AT LEAST ONE genre IN LIST
    @Query("SELECT DISTINCT n FROM Novel n join n.genres genre WHERE genre in :genres")
    List<Novel> findAtLeastOneGenres(@Param("genres") List<String> genres); // At
    
    // CONTAINS ALL genres IN LIST
    @Query("SELECT DISTINCT n FROM Novel n JOIN n.genres genre WHERE genre in :genres GROUP BY n.id having count(n.id) = :ListSize")
    List<Novel> findByGenres(@Param("genres") List<String> genres, @Param("ListSize") Long size);
    
    // #SUMMING: SEARCH BY (title, status, type) AND MUST CONTAINS ALL genres IN LIST
    @Query("SELECT DISTINCT n FROM Novel n JOIN n.genres g WHERE (:status = '' or n.status = :status) AND (:type = '' or n.type = :type) AND (:title = '' or lower(n.title) LIKE lower(concat('%', :title,'%'))) AND g.genre in :genres GROUP BY n.id having count(n.id) = :ListSize")
    List<Novel> search(@Param("title") String title, @Param("genres") List<String> genres, @Param("ListSize") Long size, @Param("status") String status, @Param("type") String type);
    
    // #SUMMING: SEARCH BY (title, status, type)
    @Query("SELECT DISTINCT n FROM Novel n JOIN n.genres g WHERE (:status = '' or n.status = :status) AND (:type = '' or n.type = :type) AND (:title = '' or lower(n.title) LIKE lower(concat('%', :title,'%')))")
    List<Novel> search(@Param("title") String title, @Param("status") String status, @Param("type") String type);

    @Query("SELECT DISTINCT g FROM Genre g")
	List<Genre> findAllGenres();
    
}
