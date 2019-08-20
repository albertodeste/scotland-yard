$(document).ready(loadWaitingPage);

function loadWaitingPage() {

    $.ajax('views/board-loading.html')
      .done((data) => { $('#content').html(data); });

    //TODO generate match on server
    //TODO display JOIN token
    //TODO start refresh thread waiting for players on match
}