package courses;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class CourseControllerTest {

	@InjectMocks
	private CourseController underTest;

	@Mock
	private Course course;
	Long courseId;

	@Mock
	private Course anotherCourse;

	@Mock
	private Topic topic;

	@Mock
	private Topic anotherTopic;

	@Mock
	private CourseRepository courseRepo;

	@Mock
	private TopicRepository topicRepo;

	@Mock
	private Model model;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldAddSingleCourseToModel() throws CourseNotFoundException {
		long arbitraryCourseId = 1;
		when(courseRepo.findById(arbitraryCourseId)).thenReturn(Optional.of(course));

		underTest.findOneCourse(arbitraryCourseId, model);
		verify(model).addAttribute("courses", course);

	}

	@Test
	public void shouldAddAllCoursesToModel() {
		Collection<Course> allCourses = Arrays.asList(course, anotherCourse);
		when(courseRepo.findAll()).thenReturn(allCourses);

		underTest.findAllCourses(model);
		verify(model).addAttribute("courses", allCourses);
	}

	@Test
	public void shouldAddSingleTopicToModel() throws TopicNotFoundException {
		long arbitraryTopicId = 1;
		when(topicRepo.findById(arbitraryTopicId)).thenReturn(Optional.of(topic));

		underTest.findOneTopic(arbitraryTopicId, model);

		verify(model).addAttribute("topics", topic);
	}

	@Test
	public void shouldAddAllTopicsToModel() {
		Collection<Topic> allTopics = Arrays.asList(topic, anotherTopic);
		when(topicRepo.findAll()).thenReturn(allTopics);

		underTest.findAllTopics(model);
		verify(model).addAttribute("topics", allTopics);
	}

	@Test
	public void shouldAddAdditionalCoursesToModel() {
		String topicName = "topic name";
		Topic newTopic = topicRepo.findByName(topicName);
		String courseName = "new course";
		String courseDescription = "new course description";
		underTest.addCourse(courseName, courseDescription, topicName);
		Course newCourse = new Course(courseName, courseDescription, newTopic);
		when(courseRepo.save(newCourse)).thenReturn(newCourse);
	}

	@Test
	public void shouldRemoveCourseFromModelByName() {
		String courseName = course.getName();
		when(courseRepo.findByName(courseName)).thenReturn(course);
		underTest.deleteCourseByName(courseName);
		verify(courseRepo).delete(course);
	}

	@Test
	public void shouldRemoveCourseFromModelById() {
		underTest.deleteCourseById(courseId);
		verify(courseRepo).deleteById(courseId);
	}

}
