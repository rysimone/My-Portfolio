// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
  // Pick a random fact.
  const facts = document.getElementsByClassName('fact');
  const randomFact = facts[Math.floor(Math.random() * facts.length)];

  // Remove any visible fact
  document.querySelectorAll('.fact.visible')
      .forEach(element => element.classList.remove('visible'));

  // Display the random fact chosen
  randomFact.classList.add('visible');
}

// Retrieves comments from /data and places them in the DOM
async function fetchComments() {
  const response = await fetch('/data');
  const comments = await response.json();
  document.getElementById('comments-container').innerText = "";
  for(var key of Object.keys(comments)){
      let comment = document.createElement("dt");
      let text = document.createTextNode(key + ": " + comments[key]);
      comment.appendChild(text);
      document.getElementById('comments-container').appendChild(comment);
  }
}

// Sends POST Request to /delete-data to delete all the comments in the
// Datastore and removes them from the DOM
async function deleteComments() {
  const request = new Request('/delete-data', {method: 'POST'});
  await fetch(request);
  fetchComments();
}


async function configureLoginLink(){
  const response = await fetch('/login');
  const status = await response.text();
  const obj = JSON.parse(status);
  const element = document.getElementById("login-button");
  element.innerText = obj.status;
  element.href = obj.url;
}
