package pj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import pj.models.Chapter;
import pj.models.Genre;
import pj.models.Novel;
import pj.models.Reading;
import pj.models.User;
import pj.service.respositories.ChapterRepository;
import pj.service.respositories.GenreRepository;
import pj.service.respositories.NovelRepository;
import pj.service.respositories.ReadingRepository;
import pj.utils.GrabberUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class NovelService {
	
	@Autowired
    private DownloadService downloaderService;
	
	@Autowired
    private NovelRepository novelsRepository;
	
	@Autowired
    private GenreRepository genreRepository;
	
	
	@Autowired
	private ReadingRepository readingRepository;

	@Autowired
	private ChapterRepository chapterRepository;
	
	
    public ResponseEntity<String> save(String slugNovel) {
    //	downloadTest(slugNovel);
    	
        Optional<Novel> optional = novelsRepository.findBySlug(slugNovel);
        
        if(optional.isEmpty()) {
        	Novel gameDetails;
 	        try {
 	            gameDetails = downloaderService.download(slugNovel);
 	            
 	            Set<Genre> list = gameDetails.getGenres();
 	            
 	            for (Genre genre : list) {
 	            	try {
 	            		genreRepository.save(genre);
 	            	} catch (Exception e) {
 	            		log.info("{} already exists in the database.", genre.getGenre());
 	            	}
 	            }
 	            
 	            novelsRepository.save(gameDetails);
 	        } catch (IOException e) {
 	            e.printStackTrace();
 	            log.info("Error while downloading the novel {}.", slugNovel);
 	            return ResponseEntity.badRequest().body("Error while downloading the novel.");
 	        }
 	        if (gameDetails.getTitle() == null || gameDetails.getTitle().equals("")) {
 	            log.info("Error while saving the novel {}.", slugNovel);
 	            return ResponseEntity.badRequest().body("Error while saving the novel.");
 	        }
 	        log.info("Novel saved");
 	        return ResponseEntity.ok("OK");
        }
        log.info("Novel {} exists.", slugNovel);
        return ResponseEntity.badRequest().body("Novel exists");
    }
    
    private void downloadTest(String slugNovel) {
    	try {
			downloaderService.download(slugNovel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("DOWNLOAD ERROR "+slugNovel);
		}
	}

	public List<Novel> getAllNovels() {
        return novelsRepository.findAll();
    }
    
    public List<Genre> getAllGenres() {
        return novelsRepository.findAllGenres();
    }
   
    public List<Novel> findBy(String title, List<String> genres, String status, String type) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Long user_id = Long.valueOf(0);
    	
    	if (auth.isAuthenticated()) {
    		Object principal = auth.getPrincipal();
    		if (principal instanceof UserDetails) {
    			user_id =  ((User) principal).getId();
    		}
    	}
    	
    	int listSize = genres.size();
    	List<Novel> novels;
    	
    	if (listSize > 0) {
    		novels = novelsRepository.search(title, genres, Long.valueOf(listSize), status, type);
    	} else {
    		novels = novelsRepository.search(title, status, type);
    	}
    	
    	if (user_id > 0) {
	    	for (int i = 0; i < novels.size(); i++) {
	    		Novel novel = novels.get(i);
	    		
	    		Optional<Reading> optional = readingRepository.getNovel(user_id, novel.getId());
		        if(!optional.isEmpty()) {
		        	novel.setAdded(true);
		        } else {
		        	novel.setAdded(false);
		        }
	    	}
    	}
    	
    	return novels;
    }

	public ResponseEntity<String> saveBulkByTitle(String title) {
		List<String> ids = downloaderService.downloadBulkByTitle(title);
		for (String id : ids) {
			System.out.println("Downloading novel "+id+"...");
			
            GrabberUtils.info(save(id).toString());
		}
		
		return ResponseEntity.ok("Done.");
	}
	
	public ResponseEntity<String> saveBulkByPages(int from, int to) {
		List<String> ids = downloaderService.downloadBulkByPages(from, to);
		for (String id : ids) {
            GrabberUtils.info(save(id).toString());
		}
		
		return ResponseEntity.ok("Done.");
	}
	
	
    public Chapter getChapter(String novel_slug, Integer chapter_n) {
    	Optional<Chapter> chapter_op = chapterRepository.getChapter(novel_slug, chapter_n);
    	
    	if (!chapter_op.isEmpty()) {
    		return chapter_op.get();
    	} else {
    		Optional<Novel> novel_op = novelsRepository.findBySlug(novel_slug);
    		
    		if (!novel_op.isEmpty()) {
				Chapter chapter = downloaderService.getChapter(novel_op.get(), chapter_n);
				chapterRepository.save(chapter);

				return chapter;
    		}
    		
    		return new Chapter();
    	}
    }

	public List<Chapter> getAllChapters(String novel_slug) {
        return chapterRepository.getChapters(novel_slug);
    }
	
	public ResponseEntity<String> downloadChapters(String slug, Integer start_from) {
		Optional<Novel> novel_op = novelsRepository.findBySlug(slug);
		
		if (!novel_op.isEmpty()) {
			Novel novel = novel_op.get();
			
			if (novel.getLastReleasedChapter() != null) {
		        Integer chaptersAdded = 0;
		      ProgressBar pb = new ProgressBar("Downloading...", novel.getLastReleasedChapter() - start_from, ProgressBarStyle.ASCII); // name, initial max
		       pb.start(); // the progress bar starts timing
		       
		        for (int chapter_n = start_from; chapter_n <= novel.getLastReleasedChapter(); chapter_n++) {
		        	Chapter chapter = downloaderService.getChapter(novel, chapter_n);
		        	try {
		        		
		        		chapterRepository.save(chapter);
		        		 pb.step(); // step by 1
				  //       pb.setExtraMessage("Reading..."); // Set extra message to display at the end of the bar
		        	} catch (Exception e) {
		        		log.info("Chapter {} already exists in the database.", chapter_n);
		        	}
		        }
		       
		       pb.stop(); // stops the progress bar
		        
				return ResponseEntity.ok("Done. Downloaded "+chaptersAdded+" new chapters.");
			}
				
			return ResponseEntity.ok("The novel has no 'Lastest Chapter' to restrict the loop (NULL).");
		}
		
		return ResponseEntity.ok("Novel '"+slug+"' doesn't exist in the database.");
	}
	
	
}
