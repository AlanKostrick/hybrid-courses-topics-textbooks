package courses;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {

	@Resource
	CourseRepository courseRepo;

	@Resource
	TopicRepository topicRepo;

	@Resource
	TextbookRepository textbookRepo;

	@RequestMapping("/course")
	public String findOneCourse(@RequestParam(value = "id") long id, Model model) throws CourseNotFoundException {
		Optional<Course> course = courseRepo.findById(id);

		if (course.isPresent()) {
			model.addAttribute("courses", course.get());
			model.addAttribute("course", course.get());
			return "course";
		}
		throw new CourseNotFoundException();

	}

	@RequestMapping("/show-courses")
	public String findAllCourses(Model model) {
		model.addAttribute("courses", courseRepo.findAll());
		return ("courses");

	}

	@RequestMapping("/topic")
	public String findOneTopic(@RequestParam(value = "id") long id, Model model) throws TopicNotFoundException {
		Optional<Topic> topic = topicRepo.findById(id);

		if (topic.isPresent()) {
			model.addAttribute("topics", topic.get());
			model.addAttribute("courses", courseRepo.findByTopicsContains(topic.get()));
			return "topic";
		}
		throw new TopicNotFoundException();
	}

	@RequestMapping("/show-topics")
	public String findAllTopics(Model model) {
		model.addAttribute("topics", topicRepo.findAll());
		return ("topics");

	}

	@RequestMapping("/add-course")
	public String addCourse(String courseName, String courseDescription, String topicName) {

		Topic topic = topicRepo.findByName(topicName);
		if (topic == null) {
			topic = new Topic(topicName);
			topicRepo.save(topic);
		}

		Course newCourse = courseRepo.findByName(courseName);

		if (newCourse == null) {
			newCourse = new Course(courseName, courseDescription, topic);
			courseRepo.save(newCourse);
		}
		return "redirect:/show-courses";
	}

	@RequestMapping("/delete-course")
	public String deleteCourseByName(String courseName) {

		Course foundCourse = courseRepo.findByName(courseName);

		if (foundCourse != null) {

			for (Textbook text : foundCourse.getTextbooks()) {
				textbookRepo.delete(text);
			}

			courseRepo.delete(foundCourse);
		}

		return "redirect:/show-courses";

	}

	@RequestMapping("/del-course")
	public String deleteCourseById(Long courseId) {

		Optional<Course> foundCourseResult = courseRepo.findById(courseId);
		Course courseToRemove = foundCourseResult.get();

		for (Textbook text : courseToRemove.getTextbooks()) {
			textbookRepo.delete(text);
		}

		courseRepo.deleteById(courseId);
		return "redirect:/show-courses";

	}

	@RequestMapping("/find-by-topic")
	public String findCoursesByTopic(String topicName, Model model) {
		Topic topic = topicRepo.findByName(topicName);
		model.addAttribute("courses", courseRepo.findByTopicsContains(topic));

		return "/topic";
	}

	@RequestMapping("/sort-courses")
	public String sortCourses(Model model) {
		model.addAttribute("courses", courseRepo.findAllByOrderByNameAsc());

		return "courses";
	}

	@RequestMapping(path = "/topics/{topicName}", method = RequestMethod.POST)
	public String addTopic(@PathVariable String topicName, Model model) {
		Topic topicToAdd = topicRepo.findByName(topicName);
		if (topicToAdd == null) {
			topicToAdd = new Topic(topicName);
			topicRepo.save(topicToAdd);
		}
		model.addAttribute("topics", topicRepo.findAll());
		return "partials/topics-list-added";
	}


	@RequestMapping(path = "/topics/remove/{id}", method = RequestMethod.POST)
	public String removeTopic(@PathVariable Long id, Model model) {

		Optional<Topic> topicToRemoveResult = topicRepo.findById(id);
		Topic topicToRemove = topicToRemoveResult.get();

		for (Course course : topicToRemove.getCourses()) {
			course.removeTopic(topicToRemove);
			courseRepo.save(course);
		}

		topicRepo.delete(topicToRemove);
		model.addAttribute("topics", topicRepo.findAll());
		return "partials/topics-list-removed";
	}

}
