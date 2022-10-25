package pj.controller;

import lombok.RequiredArgsConstructor;
import pj.models.Chapter;
import pj.models.Genre;
import pj.models.Novel;
import pj.service.DownloadService;
import pj.service.NovelService;
import pj.service.UserService;
import pj.utils.GrabberUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/res")
public class ResourceController {
	
	@Autowired
    private NovelService serviceNovel;
	
//  PUBLIC
	
    @GetMapping("/novels/all")
    @ResponseBody
    public List<Novel> getNovels() {return serviceNovel.getAllNovels();}
    
    @GetMapping("/genres/all")
    @ResponseBody
    public List<Genre> getGenres() {return serviceNovel.getAllGenres();}
    
    @GetMapping("/status/all")
    @ResponseBody
    public List<String> getStatus() {
    	ArrayList<String> list = new ArrayList<String>();
    	list.add("");
    	list.add("Completed");
    	list.add("OnGoing");
    	list.add("Cancelled");
    	list.add("OnHold");
    	return list;
    }
    
    @GetMapping("/search/title={title}&genres={genres}&status={status}&type={type}")
    @ResponseBody
    public List<Novel> findNovels(@PathVariable String title, @PathVariable ArrayList<String> genres, @PathVariable String status, @PathVariable String type) {
    	GrabberUtils.info("Searching:");
    	GrabberUtils.info("Title: '"+title+"'");
    	GrabberUtils.info("Genres: ["+String.join(",", genres)+"]");
    	GrabberUtils.info("Status: '"+status+"'");
    	GrabberUtils.info("Type: '"+type+"'");
    	
        return serviceNovel.findBy(title, genres, status, type);
    }
    
    @GetMapping("/novel/{slug}/chapter-all")
    @ResponseBody
    public List<Chapter> getAllChapters(@PathVariable String slug) {
    	return serviceNovel.getAllChapters(slug);
    }
    
    @GetMapping("/novel/{slug}/chapter-{chapN}")
    @ResponseBody
    public Chapter getChapter(@PathVariable String slug, @PathVariable Integer chapN) {
    	return serviceNovel.getChapter(slug, chapN);
    }
    
//     ADMIN
    @GetMapping("/download/chapters/name={slug}/start={start_from}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadChapter(@PathVariable String slug, @PathVariable Integer start_from) {
    	return serviceNovel.downloadChapters(slug, start_from);
    }
    
    @GetMapping("/download/novels/slug={slug}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadBySlug(@PathVariable String slug) {
        return serviceNovel.save(slug);
    }
    
    @GetMapping("/download/novels/title={title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadByTitle(@PathVariable String title) {
        return serviceNovel.saveBulkByTitle(title);
    }
    
    @GetMapping("/download/novels/from={from}&to={to}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadInBulk(@PathVariable int from, @PathVariable int to) {
    	return serviceNovel.saveBulkByPages(from, to);
//        return ResponseEntity.ok("Done.");;
    }
}
