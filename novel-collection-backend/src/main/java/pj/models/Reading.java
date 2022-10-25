package pj.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pj.models.keys.ReadingKey;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reading  {
	
	@EmbeddedId ReadingKey compositeKey;
	
	@Column(nullable = false, columnDefinition = "TEXT DEFAULT 'Unread'")
	private String status;
	
	@Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
	private Integer chapter;
	
	public Reading(Novel novel, User user) {
		this.compositeKey.setNovel(novel);
		this.compositeKey.setUser(user);
	}
}
