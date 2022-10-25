package pj.models.keys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pj.models.Novel;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterKey implements Serializable {

	@ManyToOne(fetch = FetchType.EAGER)
	private Novel novel;
	private Integer number;
}
