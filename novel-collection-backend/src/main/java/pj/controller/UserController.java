package pj.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gargoylesoftware.htmlunit.javascript.host.Console;

import pj.models.Bookmark;
import pj.models.Chapter;
import pj.models.Novel;
import pj.models.Reading;
import pj.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserController {

	@Autowired
	private UserService serviceUser;

	@GetMapping("/novels/all")
	@ResponseBody
	public List<Reading> getAllNovels() {
		return serviceUser.getNovels();
	}

	@PostMapping("/novels/add")
	public ResponseEntity<String> addNovel(@RequestBody Novel novel) {
		return serviceUser.addNovel(novel.getId());
	}
	 
	 @GetMapping("/novel/change/novel={novel_id}&chapter={chapter_n}")
	 public ResponseEntity<String> changeNovelChapter(@PathVariable Long novel_id, @PathVariable Integer chapter_n) {
	 	return serviceUser.changeReading(novel_id, chapter_n);
	 }
	 
	 @PostMapping("/novel/change")
	 public ResponseEntity<String> changeNovel(@RequestBody Reading reading) {
	 	return serviceUser.changeReading(reading);
	 }


	 @PostMapping("/novels/remove")
	 public ResponseEntity<String> removeNovel(@RequestBody Reading reading) {
	 	return serviceUser.removeNovel(reading);
	 }
	 
	 
	@GetMapping("/bookmarks/all")
	@ResponseBody
	public List<Bookmark> getAllBookmarks() {
		return serviceUser.getBookmarks();
	}
		
	 @PostMapping("/bookmarks/add")
	 public ResponseEntity<String> addBookmark(@Valid @RequestBody Bookmark bookmark) {
		bookmark.configureNew();
	 	return serviceUser.addBookmark(bookmark);
	 }
	 
	 @PostMapping("/bookmarks/edit")
	 @ResponseBody
	 public ResponseEntity<String> editBookmark(@Valid @RequestBody Bookmark bookmark) {
	 	return serviceUser.editBookmark(bookmark);
	 }
	 
	 @PostMapping("/bookmarks/remove")
	 @ResponseBody
	 public ResponseEntity<String> removeBookmark(@RequestBody Bookmark bookmark) {
		 System.out.println("test");
		 return serviceUser.removeBookmark(bookmark);
	}
}
