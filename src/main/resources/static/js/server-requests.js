const topicAddButton = document.querySelector('.add-topic button');
const topicAddInput = document.querySelector('.add-topic input');
const topicsList = document.querySelector('.topics-list ul');
const topicRemoveButtons = document.querySelectorAll('.x');


const xhr = new XMLHttpRequest()
xhr.onreadystatechange = function() {
	if (xhr.readyState === 4 && xhr.status === 200) {
		const res = xhr.responseText;
		topicsList.innerHTML = res;
	}
}

topicAddButton.addEventListener('click', function() {
	postTopics(topicAddInput.value);
//	console.log(topicAddInput.value);
	topicAddInput.value = "";
})

//topicRemoveButtons.forEach(button =>{
//	button.addEventListener('click', function(event){ 
//		let topicId = event.target.previousElementSibling.previousElementSibling.value;
//		console.log(topicId);
//		removeTopic(topicId);
//	})
//})

topicsList.addEventListener('click', function(event){
	if(event.target.classList.contains('x')){
	let topicId = event.target.previousElementSibling.previousElementSibling.value;
	console.log(topicId);
	removeTopic(topicId);
	}
})


function postTopics(topicName) {
	xhr.open('POST', '/topics/' + topicName, true);
	xhr.send();
}

function removeTopic(id){
	xhr.open('POST', '/topics/remove/' + id, true);
	xhr.send();
}
