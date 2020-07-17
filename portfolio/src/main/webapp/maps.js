const mapsKey = config.MAPS_KEY;

window.onload = createScriptTagForMap();


function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map-container'),
        //Default to Southern California
        {center: {lat:33.835, lng: -118.390}, zoom: 12});
    let pvLatLng = {lat: 33.792, lng: -118.407};
    let tpzLatLng = {lat: 33.832, lng: -118.392};
    let bwLatLng = {lat: 33.852, lng: -118.399};
    let lfLatLng = {lat: 33.875, lng: -118.408};
    let nmbpLatLng = {lat: 33.884, lng: -118.413};
    let portoLatLng = {lat: 33.905, lng: -118.423};

    let pvMark = new google.maps.Marker({
        position: pvLatLng,
        map: map,
        title: 'Palos Verdes Cove'
    });

    let tpzMark = new google.maps.Marker({
        position: tpzLatLng,
        map: map,
        title: 'Topaz'
    });

    let bwMark = new google.maps.Marker({
        position: bwLatLng,
        map: map,
        title: 'Break Wall'
    });

    let lfMark = new google.maps.Marker({
        position: lfLatLng,
        map: map,
        title: 'Longfellow'
    });

    let nmbpMark = new google.maps.Marker({
        position: nmbpLatLng,
        map: map,
        title: 'Northside Manhattan Beach Pier'
    });

    let portoMark = new google.maps.Marker({
        position: portoLatLng,
        map: map,
        title: 'El Porto'
    });
}

function createScriptTagForMap() {
  let script = document.createElement('script');
  script.src = 'https://maps.googleapis.com/maps/api/js?key=' + mapsKey + '&callback=createMap';

  document.head.appendChild(script);
}