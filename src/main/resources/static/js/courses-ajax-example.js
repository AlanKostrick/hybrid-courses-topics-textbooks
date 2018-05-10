const xhr = new XMLHttpRequest()
xhr.onreadystatechange = function() {
	if (this.readyState == 4 && this.status == 200) {
		const res = JSON.parse(xhr.response)
		const container = document.querySelector('.container')

		console.log({res})

		res.forEach(function(course) {
			const courseItem = document.createElement('div')

			const name = document.createElement('h2')
			name.innerText = course.name;

			const description = document.createElement('p')
			description.innerText = course.description

			const topics = [];
			course.topicsUrls.forEach(topicUrl => {
				const topicUrlElement = document.createElement('li')
				topicUrlElement.innerHTML = `course topics: <a href="${topicUrl}">${topicUrl}</a>`
				topics.push(topicUrlElement)
			})

			container.appendChild(courseItem)
			courseItem.appendChild(name)
			courseItem.appendChild(description)

			topics.forEach(topicUrl => courseItem.appendChild(topicUrl))

		});

	}
}

xhr.open('GET', 'http://localhost:8080/courses', true)
xhr.send()