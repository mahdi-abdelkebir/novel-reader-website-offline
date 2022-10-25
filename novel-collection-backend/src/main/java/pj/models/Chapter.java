package pj.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pj.models.keys.ChapterKey;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    
	@EmbeddedId ChapterKey compositeKey;
    
	private String title;
	
    @Column(columnDefinition="TEXT")
    private String content;
   
}
