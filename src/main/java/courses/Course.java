package courses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import static java.lang.String.format;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String description;

	@JsonIgnore
	@ManyToMany
	private Collection<Topic> topics;

	@JsonIgnore
	@OneToMany(mappedBy = "course")
	private Collection<Textbook> textbooks;

	public long getId() {
		return id;
	}

	public String getName() {

		return name;
	}

	public String getDescription() {
		return description;
	}

	public Collection<Topic> getTopics() {

		return topics;
	}

	public Collection<Textbook> getTextbooks() {

		return textbooks;
	}

	public Collection<String> getTopicsUrls() {
		Collection<String> urls = new ArrayList<>();
		for (Topic t : topics) {
			urls.add(format("/courses/%d/topics/%s", this.getId(), t.getName().toLowerCase()));
		}

		return urls;
	}

	public Course() {

	}

	public Course(String name, String description, Topic... topics) {
		this.name = name;
		this.description = description;
		this.topics = new HashSet<>(Arrays.asList(topics));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
