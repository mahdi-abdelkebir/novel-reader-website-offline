package pj.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CharMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pj.models.Chapter;
import pj.models.Genre;
import pj.models.Novel;
import pj.models.keys.ChapterKey;
import pj.utils.GrabberUtils;

import org.openqa.selenium.NoSuchElementException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.annotation.PostConstruct;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DownloadService {
    private final String source = "https://boxnovel.com";
//    private final static String GAME_URL = "https://api.rawg.io/api/games/";
//    private final static String SCREENSHOTS_URL = "/screenshots";
//    private final static String API_KEY = "1298c33c3de5463fa3844201bf868615";
    private ObjectMapper objectMapper;
	
    @PostConstruct
    public void postInit() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
//    
//    public WebClient createClient(String url) throws FailingHttpStatusCodeException, IOException, InterruptedException {
//    	WebClient webClient = new WebClient(BrowserVersion.CHROME);
//    	webClient.getOptions().setJavaScriptEnabled(true);
//    	webClient.getOptions().setThrowExceptionOnScriptError(false);
//    	webClient.getOptions().setCssEnabled(false);
//    	
//        webClient.setAjaxController(new AjaxController(){
//            @Override
//            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async)
//            {
//                return true;
//            }
//        });
//        
//        WebRequest request = new WebRequest(new URL(url));
//        HtmlPage page = webClient.getPage(request);
//
//        int i = webClient.waitForBackgroundJavaScript(1000);
//
//        while (i > 0)
//        {
//            i = webClient.waitForBackgroundJavaScript(1000);
//
//            if (i == 0)
//            {
//                break;
//            }
//            synchronized (page) 
//            {
//                System.out.println("wait");
//                page.wait(500);
//            }
//        }
//
//        webClient.getAjaxController().processSynchron(page, request, true);
//
//		return webClient;
//    }
    public Document connect(String link) {
    	GrabberUtils.info("Connecting to site... "+link);
        try {
        	
        	Document html = Jsoup.connect(source+link).get();
            GrabberUtils.info("Successfully connected to site");
         
            return html;
        } catch (Exception e) {
            GrabberUtils.err("Could not connect to webpage!", e);
        }
        return null;
    }
    
    public Novel download(String slug) throws IOException {
    	Novel novel = getData(slug);
    	return novel;
    }
    
    public Novel getData(String slug) {
    	Novel novel = new Novel();
    	//http://localhost:8080/download/novel/super-gene-boxnovel/
        Document html = connect("/novel/"+slug);
        
        if (html != null) {
        	try {
	        	GrabberUtils.info("Downloading "+slug+"...");
	        	
	        	Elements content = html.select(".summary-content");
	        	
	            String title = html.select(".post-title h1").text();
	            GrabberUtils.info("Title: "+title);
	            
	        	novel.setDescription(getDescription(html));
	            GrabberUtils.info("Description: "+novel.getDescription());
	            
	            Elements authorContent = content.select(".author-content");
	            if (authorContent.size() > 0) {
		            novel.setAuthor(authorContent.first().text());
		            GrabberUtils.info("Author: "+novel.getAuthor());
	            } else {
	            	GrabberUtils.info("Author: NONE");
	            }
	            
	            novel.setThumbnailURL(html.select(".summary_image img").attr("abs:src"));
	            
	            getChapterLatest(novel, title);
	            
	            Elements _tags = content.select(".tags-content a");
	            if (_tags.size() > 0) {
		            novel.setType(_tags.get(0).text());
		            GrabberUtils.info("Type: "+novel.getType());
		        } else {
		        	GrabberUtils.info("Type: NONE");
		        }
	            
	            Elements _genres = content.select(".genres-content a");
	            Set<Genre> genres = new HashSet<>();
	            for (Element tag : _genres) {
	                genres.add(new Genre(tag.text()));
	            }
	            GrabberUtils.info("Genres: "+genres.toString());
	            novel.setGenres(genres);
	           
	            Element status = content.last();
	            novel.setStatus(status.text());
	            GrabberUtils.info("Status: "+novel.getStatus());
	            
	            novel.setRating(Float.valueOf(html.selectFirst(".total_votes").text()));
	            GrabberUtils.info("Rating: "+novel.getRating());
	            
	        	novel.setSource(source);
	        	novel.setSlug(slug);
	        	novel.setTitle(title);
	            
	            GrabberUtils.info("Download finished successfully.");
	            
            } catch (NoSuchElementException e) {
                GrabberUtils.err(e.getMessage()+". Correct novel link?");
            } catch (Exception e) {
                GrabberUtils.err(e.getMessage()+". Correct novel link?");
        	}
        }

        return novel;
    }


	private String getDescription(Document html) {
        Elements description = html.select("#editdescription");
        if (description.size() == 0) {
        	description = html.select(".j_synopsis");
            if (description.size() == 0) {
            	return html.select(".summary__content").first().html();
        	}
    	}
		return description.html();
	}
	
	private void getChapterLatest(Novel novel, String title) {
		System.out.println("String has become: "+title.replace("’", "'"));
        Document html = connect("/?s="+URLEncoder.encode(title.replace("’", "'"), StandardCharsets.UTF_8)+"&post_type=wp-manga");
 
        if (html != null) {
        	try {
                Elements SearchResults = html.select(".c-tabs-item__content");
	            for (Element result : SearchResults) {
	                if (result.select(".post-title .h4 a").text().compareTo(title) == 0) {
	                	String chapter = CharMatcher.inRange('0', '9').retainFrom(result.select(".chapter").text());
	                	
	                	if (chapter.length() > 0) {
	                		novel.setLastReleasedChapter(Integer.valueOf(chapter)); 
	                	}
	                	
	    	            novel.setLastReleasedDate(result.select(".post-on").text());
	    	            
	    	            GrabberUtils.info("Last Chapter: "+novel.getLastReleasedChapter());
	    	            GrabberUtils.info("Posted On: "+novel.getLastReleasedDate());
	    	            break;
	                }
	            }
	       
            } catch (NoSuchElementException e) {
                GrabberUtils.err(e.getMessage()+". Unable to get search result or chapters information. The source HTML must have changed.");
        	}
        }
	};
	
    public Chapter getChapter(Novel novel, Integer chapter_n) {
    	Chapter chapter = new Chapter();
    	Document html;
    	
    	if (novel.getLastReleasedChapter().intValue() == chapter_n && novel.getStatus().equals("Completed") == true) {
    		html = connect("/novel/"+novel.getSlug()+"/chapter-"+chapter_n+"-end");
    	} else {
    		html = connect("/novel/"+novel.getSlug()+"/chapter-"+chapter_n);
    	}
    	
        if (html != null) {
        	try {
        		chapter.setCompositeKey(new ChapterKey(novel, chapter_n));
                Element chapterTitle = html.selectFirst(".breadcrumb .active");
        		chapter.setTitle(chapterTitle.text());
	
                Element chapterContent = html.selectFirst(".text-left");
        		chapter.setContent(chapterContent.html());
        		
            } catch (NoSuchElementException e) {
                GrabberUtils.err(e.getMessage()+". Unable to get search result or chapters information. The source HTML must have changed.");
        	}
        }
        
		return chapter;
    }
    
    
    
    
    // OTHERS
    
    public List<String> downloadBulkByTitle(String title) {
    	List<String> list = new ArrayList<String>();
    	Document html = connect("/?s="+URLEncoder.encode(title.replace("’", "'"), StandardCharsets.UTF_8)+"&post_type=wp-manga");
    	
        List<Element> _tags = html.select(".post-title .h4 a");
        for (Element tag : _tags) {
            String[] bits = tag.attr("href").split("/");
            list.add(bits[bits.length-1]);
        	System.out.println("found... "+bits[bits.length-1]);
        }
        
    	return list;
    }
    
    public List<String> downloadBulkByPages(int from, int pages) {
    	List<String> list = new ArrayList<String>();
    	for (int i = from; i < from+pages; i++) {
    		Document doc = connect("/page/"+i);
            List<Element> _tags = doc.select(".h5 a");
            for (Element tag : _tags) {
                String[] bits = tag.attr("href").split("/");
                list.add(bits[bits.length-1]);
            }
    	}
    	return list;
    }
}
