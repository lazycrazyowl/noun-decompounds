var couchapp = require('couchapp'),
  path = require('path');

var ddoc = {
  _id: '_design/tree',
  views: {},
  lists: {}
};
module.exports = ddoc;

/**
 * By url view
 * 
 * Sorts all documents by url.
 * Can be used to access docs by url 
 */
ddoc.views.splitWords = {
  map: function(doc) {
    if (doc.word) {
      emit(doc.word, {id: doc._id, word: doc.word});
    }
  }
};

ddoc.lists.split = function(head, req) {
  start({
    headers: {"Content-type": "text/html"}
  });
  send("<ul id='split'>\n");
  while(row = getRow()) {
    doc = row.value;
    send("\t<li class='split word' id='"+doc.id+"'>" + doc.word + "</li>\n");
  }
  send("</ui>\n");
};

couchapp.loadAttachments(ddoc, path.join(__dirname, '_attachments'));