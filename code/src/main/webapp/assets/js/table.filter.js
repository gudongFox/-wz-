var tablefilter={};
tablefilter.params={};
tablefilter.option={};
tablefilter.queryFunction={};
tablefilter.initializeFilter=function(option){
    var tableLength=$("table[filter]").length;
    tablefilter.option=option;
    for(var tableIndex=0;tableIndex<tableLength;tableIndex++){
        var tableSelector=$("table[filter]").eq(tableIndex);
        var existId=$("table[filter]").attr("filter");
        if(existId==undefined||existId==""){
            var randomId=Math.random().toString(36).substr(2);
            tablefilter.initializeFilterTable(tableSelector,tablefilter.option,randomId);
        }
    }
}
tablefilter.initializeFilterTable=function(tableSelector,option,randomId){
    var thLength=tableSelector.find("th").length;
    var trLength=tableSelector.find("tr").length;
    if(trLength>1){
        var row=document.createElement("tr");
        tableSelector.attr("filter",randomId)
        var trHtml="";
        console.log("创建过滤行");
        for(var index=0;index<thLength;index++){
            if(option.filterColumns.hasOwnProperty(index)){
                var property=option.filterColumns[index];
                trHtml+='<td style="padding: 0 0 0 0">';
                if(property.type=="select"){
                    trHtml+='<select name="filterInput" onchange="tablefilter.columnsFilter(\''+randomId+'\')" type="text" class="form-control">';
                        property.option.forEach(function (item) {
                            trHtml+='<option value="'+item.value+'">'+item.title+'</ooption>'
                        })
                    trHtml+='</select>';
                }
                else{
                    if(property.hasOwnProperty("placeholder")){
                        var placeHolderValue=property.placeholder;
                        trHtml+='<input placeholder="'+placeHolderValue+'" name="filterInput" onkeyup="tablefilter.columnsFilter(\''+randomId+'\')" type="text" class="form-control">';
                    }
                    else{
                        trHtml+='<input name="filterInput" onkeyup="tablefilter.columnsFilter(\''+randomId+'\')" type="text" class="form-control">';
                    }
                }

                trHtml +='</td>';
            }
            else{
                if(option.handleColumn==index){
                    trHtml+='<td style="padding: 5px 0 0 8px;">'+
                        '<i onclick="tablefilter.refreshData(\''+randomId+'\')" title="查询全数据" style="color: black;width: 26px" class="fa fa-search"></i>'+
                        '</td>';
                }
                else{
                    trHtml+='<td style="padding: 0 0 0 0">' +
                        '<input  name="filterInput" readonly type="text" class="form-control">' +
                        '</td>';
                }
            }
        };
        row.innerHTML=trHtml;
        tableSelector.find("tr").eq(0).after(row);
    }
};
tablefilter.columnsFilter=function(id){
    var filterMap=new Map();
    var inputLength=$("table[filter="+id+"]").find("[name='filterInput']").length;
    var selector=$("table[filter="+id+"]").find("[name='filterInput']");
    for(var inputIndex=0;inputIndex<inputLength;inputIndex++){
        var current=selector[inputIndex];
        if(current.tagName.toLowerCase()=="select"){
            var value=current.selectedOptions[0].text;
            if(value=="全部"){
                filterMap.set(inputIndex,"");
            }
            else{
                filterMap.set(inputIndex,value);
            }

        }
        else{
            var value=current.value;
            filterMap.set(inputIndex,value);
        }


    };
    $("table[filter="+id+"]").find("tbody tr").hide();
    $("table[filter="+id+"]").find("tbody tr").filter(function (rowIndex){
        var trNode = $(this);
        var tdLength=trNode.find("td").length;
        var rowValue=true;
        try{
            for(var tdIndex=0;tdIndex<tdLength;tdIndex++){
                var mapValue=filterMap.get(tdIndex);
                var tdText=trNode.find("td").eq(tdIndex).text();
                if(tablefilter.option.filterColumns.hasOwnProperty(tdIndex)){
                    var property=tablefilter.option.filterColumns[tdIndex];
                    console.log("filter");
                    if(mapValue!=undefined&&mapValue!=""){
                        if(tdText.toLowerCase().indexOf(mapValue.toLowerCase())==-1){
                            rowValue=false;
                        }
                    }
                }
            }
        }
        catch (e) {
            return  true;
        }
        return  rowValue;

    }).show();
}
tablefilter.refreshData=function(id){
    tablefilter.refreshParameters(id);
    tablefilter.queryFunction(tablefilter.params);
};
tablefilter.refreshParameters=function(id){
    var inputLength=$("table[filter="+id+"]").find("[name='filterInput']").length;
    for(var inputIndex=0;inputIndex<inputLength;inputIndex++){
        var value=$("table[filter="+id+"]").find("[name='filterInput']").eq(inputIndex).val();
        if(tablefilter.option.filterColumns.hasOwnProperty(inputIndex)){
            var key=tablefilter.option.filterColumns[inputIndex].colName;
            tablefilter.params[key]=value;
        }
    };
}
