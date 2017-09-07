/**
 * 
 */
    
	       var tableR = 16;
           var cell = document.getElementById("periodTestingTableDetail").rows.item(0).cells.length;
           var tr = document.querySelectorAll("#periodTestingTableDetail tbody tr");
        
           var dataO = [];
           if(tableR == cell){
               for(var i=0; i<tr.length; i++){
                   var checkbox = tr[i].children[0].children[0].checked;
                   if(checkbox == true){
                	   dataO.push([tr[i].children[2].innerHTML, 
                	                 tr[i].children[3].innerHTML, 
                	                 tr[i].children[10].innerHTML, 
                	                 tr[i].children[11].innerHTML, 
                	                 tr[i].children[12].innerHTML]);
                      
                   }
               }
               
           }
           
           localStorage.setItem('paraList',JSON.stringify(dataO));//将选择光路的信息暂存
        