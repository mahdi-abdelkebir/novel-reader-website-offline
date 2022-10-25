package pj.service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pj.models.Chapter;
import pj.models.Genre;
import pj.models.Novel;
import pj.models.keys.ChapterKey;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, ChapterKey> {
	
    @Query("SELECT DISTINCT c FROM Chapter c JOIN c.compositeKey.novel n WHERE c.compositeKey.novel.slug = :novel_slug AND c.compositeKey.number = :chapter_n")
    Optional<Chapter> getChapter(@Param("novel_slug") String novel_slug, @Param("chapter_n") Integer chapter_n); // At
    
    @Query("SELECT DISTINCT c FROM Chapter c JOIN c.compositeKey.novel n WHERE c.compositeKey.novel.slug = :novel_slug")
    List<Chapter> getChapters(@Param("novel_slug") String novel_slug); // At
}
