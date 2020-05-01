/* Function for highlighting a state with their party affiliation*/
/* TODO: Doesn't work right now, I have it on Alabama and it literally highlights everything besides Alabama */

function highlightAffiliation() {
    L.geoJSON(statesData, {
        style: function (feature) {
            switch (feature.properties.party) {
                case 'Republican':
                    return {color: "#ff0000"};
                case 'Democrat':
                    return {color: "#0000ff"};
                default:
                    break;
            }
        }
    }).addTo(map);
}