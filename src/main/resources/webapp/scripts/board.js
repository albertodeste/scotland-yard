$(document).ready(loadWaitingPage);

function loadWaitingPage() {

    $.ajax('views/board-loading.html')
      .done((data) => { $('#content').html(data); });

    initMatch().then(showToken).then(awaitPlayers).catch(showError);
}

function showError(text) {
  console.error(text);
}

function awaitPlayers() {
  //TODO start refresh thread waiting for players
}

function showToken(token) {

  return new Promise((res, rej) => {

    $('#token').html('Digita il seguente codice per aggiungerti alla partita: ' + token);
    $('#status').html('In attesa di giocatori ...');
    res();
  });
}

function initMatch() {

  return new Promise((res, rej) => {

    $.ajax({
      url: '../match',
      method: 'POST'
    })
    .done((data) => {
      res(data.id);
    })
    .fail(() => {
    console.log('error');
      rej('Init match returned an error');
    });
  });
}