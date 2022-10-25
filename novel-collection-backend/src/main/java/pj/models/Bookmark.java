package pj.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore @ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private String chapterTitle;
	
	@Column(nullable = false) @NotBlank(message = "Title may not be empty")
    private String title;
    
	@Column(columnDefinition="TEXT", nullable = false)
    private String content;
    
    private String thoughts;
   
	@Column(nullable = false)
    private String tags;
    
    public void configureNew() {
    	this.chapterTitle = chapter.getTitle()+" ("+this.chapter.getCompositeKey().getNovel().getTitle()+")";
    	this.tags = "";
    }
    
    @Transient
    private Chapter chapter;
}
