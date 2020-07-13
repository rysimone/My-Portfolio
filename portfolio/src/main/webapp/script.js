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
  document.querySelectorAll('.fact.visible').forEach(element => element.classList.remove('visible'));

  // Display the random fact chosen
  randomFact.classList.add('visible');
}

async function getMessageFromServer() {
  const response = await fetch('/data');
  const quote = await response.text();
  document.getElementById('message-container').innerHTML = quote;
}

function login(url){
  window.location = url;
}

async function setButtonName(){
  const response = await fetch('/login');
  const status = await response.text();
  let obj = JSON.parse(status);
  let element = document.getElementById("login-button");
  if(!(obj.status.localeCompare("Login"))){
      element.value = "Login";
  }
  else {
      console.log(obj.status);
      element.value = "Logout";
  }
}

async function logInUser(){
  const response = await fetch('/login');
  const status = await response.text();
  let obj = JSON.parse(status);
  login(obj.url);
}