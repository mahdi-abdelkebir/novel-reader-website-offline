package pj.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pj.models.Bookmark;
import pj.models.Chapter;
import pj.models.Genre;
import pj.models.Novel;
import pj.models.Reading;
import pj.models.User;
import pj.service.respositories.BookmarkRepository;
import pj.service.respositories.ReadingRepository;
import pj.service.respositories.UserRepository;
import pj.utils.GrabberUtils;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	  
	@Autowired
	ReadingRepository readingRepository;
	
	@Autowired
	BookmarkRepository bookmarkRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
		
		return user.configureAuthorities();
	}
	
	protected User getUserDetails() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userDetails = null;
		if (principal instanceof UserDetails) {
			userDetails = (User) principal;
		}
		return userDetails;
	}
	
	public List<Reading> getNovels() {
		return readingRepository.findAll(getUserDetails().getId());
	}

	@Transactional
	public ResponseEntity<String> addNovel(Long novel_id) {
		Long user_id = getUserDetails().getId();
		System.out.println("USER "+user_id+" is adding something...");
		
        Optional<Reading> optional = readingRepository.getNovel(user_id, novel_id);
        
        if(optional.isEmpty()) {
	        try {
	            readingRepository.addNovel(user_id, novel_id);
	        } catch (Exception e) {
	            e.printStackTrace();
	            GrabberUtils.err("Error while inserting the assocition entity (Reading) (user = "+user_id+", novel = "+novel_id+").");
	            return ResponseEntity.badRequest().body("Error while saving the novel.");
	        }
	        GrabberUtils.info("Reading entity saved");
	        return ResponseEntity.ok("OK");
        }
        
        GrabberUtils.info("Reading (user = "+user_id+", novel = "+novel_id+") exists.");
        return ResponseEntity.badRequest().body("Reading entity already exists");
	}

	@Transactional
	public ResponseEntity<String> changeReading(Reading reading) {
		Long user_id = getUserDetails().getId();
		System.out.println("USER "+user_id+" is changing something...");
		
        try {
    		Long novel_id = reading.getCompositeKey().getNovel().getId();
	        Optional<Reading> optional = readingRepository.getNovel(user_id, novel_id);
	        
	        if(!optional.isEmpty()) {
	        	System.out.println("USER "+user_id+" is changing (novel = "+novel_id+", status = "+reading.getStatus()+", chapter = "+reading.getChapter()+")");
		         readingRepository.change(user_id, novel_id, reading.getStatus(), reading.getChapter());
	
		        GrabberUtils.info("Reading entity saved");
		        return ResponseEntity.ok("OK");
	        }
	        
            GrabberUtils.info("Reading (user = "+user_id+", novel = "+novel_id+") doesn't exist.");
            return ResponseEntity.badRequest().body("Reading entity doesn't exists");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        GrabberUtils.err("Error while saving the assocition entity (Reading).");
        return ResponseEntity.badRequest().body("Error while saving the novel.");
	}
	
	@Transactional
	public ResponseEntity<String> changeReading(Long novel_id, Integer chapter_n) {
		Long user_id = getUserDetails().getId();
		System.out.println("USER "+user_id+" is changing something...");
		
        try {
	        Optional<Reading> optional = readingRepository.getNovel(user_id, novel_id);
	        
	        if(!optional.isEmpty()) {
	        	System.out.println("USER "+user_id+" is changing (novel = "+novel_id+", chapter = "+chapter_n+")");
		         readingRepository.change(user_id, novel_id, chapter_n);
	
		        GrabberUtils.info("Reading entity saved");
		        return ResponseEntity.ok("OK");
	        }
	        
            GrabberUtils.info("Reading (user = "+user_id+", novel = "+novel_id+") doesn't exist.");
            return ResponseEntity.badRequest().body("Reading entity doesn't exists");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        GrabberUtils.err("Error while saving the assocition entity (Reading).");
        return ResponseEntity.badRequest().body("Error while saving the novel.");
	}
	
	@Transactional
	public ResponseEntity<String> removeNovel(Reading reading) {
		Long user_id = getUserDetails().getId();
		System.out.println("USER "+user_id+" is removing something...");
		
        try {
    		Long novel_id = reading.getCompositeKey().getNovel().getId();
	        Optional<Reading> optional = readingRepository.getNovel(user_id, novel_id);
	        
	        if(!optional.isEmpty()) {
	        	readingRepository.removeNovel(user_id, novel_id);
	
		        GrabberUtils.info("Reading entity removed");
		        return ResponseEntity.ok("OK");
	        }
	        
            GrabberUtils.info("Reading (user = "+user_id+", novel = "+novel_id+") doesn't exist.");
            return ResponseEntity.badRequest().body("Reading entity doesn't exists");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        GrabberUtils.err("Error while removing the assocition entity (Reading).");
        return ResponseEntity.badRequest().body("Error while removing the novel.");
	}
	
	
	public List<Bookmark> getBookmarks() {
		return bookmarkRepository.findAll(getUserDetails().getId());
	}
	
	@Transactional
	public ResponseEntity<String> addBookmark(Bookmark bookmark) {
		User user = getUserDetails();
		System.out.println("USER "+user.getId()+" is adding something...");
		
		bookmark.setUser(user);
	
        try {
            bookmarkRepository.save(bookmark);
            
        } catch (Exception e) {
            e.printStackTrace();
            GrabberUtils.err("Error while inserting the assocition entity (Bookmark) (user = "+user.getId()+", chapterTitle = "+bookmark.getChapterTitle()+").");
            return ResponseEntity.badRequest().body("Error while saving the bookmark.");
        }
        GrabberUtils.info("Bookmark entity saved");
        return ResponseEntity.ok("OK");
	}
	
	public ResponseEntity<String> editBookmark(Bookmark bookmark) {
		User user = getUserDetails();
		System.out.println("USER "+user.getId()+" is editing something...");
		
		bookmark.setUser(user);
		
        try {
            bookmarkRepository.save(bookmark);
            
        } catch (Exception e) {
            e.printStackTrace();
            GrabberUtils.err("Error while saving the assocition entity (Bookmark) (user = "+user.getId()+", bookmark = "+bookmark.getId()+").");
            return ResponseEntity.badRequest().body("Error while saving the bookmark.");
        }
        
        GrabberUtils.info("Bookmark entity updated.");
        return ResponseEntity.ok("OK");
	}
	
	public ResponseEntity<String> removeBookmark(Bookmark bookmark) {
		User user = getUserDetails();
		System.out.println("USER "+user.getId()+" is removing something...");
		
        try {
        	 bookmarkRepository.deleteById(bookmark.getId());
            
        } catch (Exception e) {
            e.printStackTrace();
            GrabberUtils.err("Error while saving the assocition entity (Bookmark) (user = "+user.getId()+", bookmark = "+bookmark.getId()+").");
            return ResponseEntity.badRequest().body("Error while saving the bookmark.");
        }
        
        GrabberUtils.info("Bookmark entity deleted.");
        return ResponseEntity.ok("OK");
	}
	
	
}