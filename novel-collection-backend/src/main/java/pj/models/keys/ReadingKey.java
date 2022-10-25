package pj.models.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import pj.models.Novel;
import pj.models.User;

@Embeddable
@Data
public class ReadingKey implements Serializable {


	@ManyToOne(fetch = FetchType.EAGER)
	private Novel novel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
}
