const mapsKey = config.MAPS_KEY;

window.onload = createScriptTagForMap();

function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map-container'),
        {center: {lat:33.835, lng: -118.390}, zoom: 5});
    console.log("Map should be created")
}

function createScriptTagForMap() {
  let script = document.createElement('script');
  script.src = 'https://maps.googleapis.com/maps/api/js?key=' + mapsKey + '&callback=createMap';

  document.head.appendChild(script);
}