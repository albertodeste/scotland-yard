$(document).ready(loadWaitingPage);

function loadWaitingPage() {

    $.ajax('views/board-loading.html')
      .done((data) => { $('#content').html(data); });

    initMatch()
    .then(showJoinString)
    .then(awaitPlayers)
    .catch(showError);
}

function showError(text) {
  console.error(text);
}

function playerJoined(joinEvent) {

  //TODO GET player and display role + name
  $('#player_list').append("- " + joinEvent.id + "<br/>");
  //TODO display start button if match can start
}

function awaitPlayers() {

    $.ajax({
        'url': '../subscribe',
        'method': 'POST',
        'data': JSON.stringify({'subscriber-type': 'board', 'id': window.matchId, 'event-listening': ['player_added']})
      })
      .done((data) => {

        playerJoined(data);
        awaitPlayers();
      })
      .fail(() => {
        awaitPlayers();
      });
}

function showJoinString(match) {

  return new Promise((res, rej) => {

    $('#token').html('Digita il seguente codice per aggiungerti alla partita: ' + match.join_string);
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

      window.matchId = data.id;
      res(data);
    })
    .fail(() => {
      rej('Init match returned an error');
    });
  });
}