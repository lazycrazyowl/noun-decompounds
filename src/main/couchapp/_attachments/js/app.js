$(document).ready(function(){
  var dbName = "noun_decompounding";
  var db = $.couch.db(dbName);
  
  var st = new $jit.ST({
    injectInto: "tree",
		
		duration: 200,
		
    levelDistance: 50,
		
		orientation: 'top',
		
    Node: {
			autoHeight: true,  
			autoWidth: true, 
      type: 'rectangle',
      color: '#aaa',
      overridable: true
    },
    
    Edge: {
      type: 'bezier',
      overridable: true
    },
    
    onCreateLabel: function(label, node){
      label.id = node.id;
      label.innerHTML = node.name;
      label.onclick = function(){
        st.onClick(node.id);
      };
      var style = label.style;
      style.cursor = 'pointer';
    },
    
    onBeforePlotNode: function(node){
      //add some color to the nodes in the path between the  
      //root node and the selected node.  
      if (node.selected) {
        node.data.$color = "#ff7";
      }
      else {
        delete node.data.$color;
        //if the node belongs to the last plotted level  
        if (!node.anySubnode("exist")) {
          //count children number  
          var count = 0;
          node.eachSubnode(function(n){
            count++;
          });
          //assign a node color based on  
          //how many children it has  
          node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];
        }
      }
    },
    
    onBeforePlotLine: function(adj){
      if (adj.nodeFrom.selected && adj.nodeTo.selected) {
        adj.data.$color = "#eed";
        adj.data.$lineWidth = 3;
      }
      else {
        delete adj.data.$color;
        delete adj.data.$lineWidth;
      }
    }
  });
  
  var updateList = function(callback){
    $.ajax({
      type: 'GET',
      dataType: 'html',
      url: '/' + dbName + "/_design/tree/_list/split/splitWords",
      success: function(data){
        $("#words").html(data);
        callback();
      }
    });
  };
  
  var updateTree = function(id){
    db.openDoc(id, {
      success: function(result){
        st.loadJSON(result.tree);
        st.compute();
        st.onClick(st.root);
      }
    })
  };
	
  updateList(function(){
    $("#words li").click(function(){
      updateTree(this.id);
    })
  });
});
