/* Add a polygon to the map */
/* TODO: Implement polygon drawing by using this code and the below code that gets X, Y coord
    of mouse click? */
function addPolygon(){
    var p1 = new L.LatLng(34, -126),
        p2 = new L.LatLng(40, -125),
        p3 = new L.LatLng(36, -124),
        polygonPoints = [p1, p2, p3],
        polygon = new L.Polygon(polygonPoints);

    map.addLayer(polygon);
}

var markerGroup = L.layerGroup()

function addMarker() {
    var markerLocation = new L.LatLng(39.657, -77.575),
        marker = new L.Marker(markerLocation);

    marker.bindTooltip("Overlapping precincts",
        {
            permanent: true,
            direction: 'right'
        });

    map.addLayer(marker).on('click', onClick);;
}

function onClick(e) {
    map.flyTo([39.657, -77.575], 10);
}

/* On map click displays string for where you clicked */
function viewCursorLocation(){
    function onMapClick(e) {
        var latlngStr = '(' + e.latlng.lat.toFixed(3) + ', ' + e.latlng.lng.toFixed(3) + ')';
        map.on('click', onMapClick);

        var popup = new L.Popup();
        popup.setLatLng(e.latlng);
        popup.setContent("You clicked the map at " + latlngStr);
        map.openPopup(popup);
    }
}