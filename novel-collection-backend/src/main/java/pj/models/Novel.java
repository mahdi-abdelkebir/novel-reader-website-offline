package pj.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(indexes = {@Index(name = "nv_slug", columnList = "slug", unique = true), @Index(name = "nv_author", columnList = "author"), @Index(name = "nv_type", columnList = "type"), @Index(name = "nv_status", columnList = "status")})
public class Novel {
    
    @Id  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String slug;
    private String title;
    
    @Column(columnDefinition="TEXT")
    private String description;
    
    private String author;
    private String thumbnailURL;
    
    private String type; // Chinese, Korean....
    private String status; // Completed, Ongoing, Canceled, On Hold
    
    private Integer lastReleasedChapter;
    private String lastReleasedDate;
    
    private String source;
    private Float rating;
    
    // You can specify fetch = FetchType.EAGER in @OneToMany. This will fetch all 'Genre' entities (oneToMany) related to a novel when you do a find on a novel id.
   
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Genre> genres;
    
    @Transient
	Boolean added;
}
