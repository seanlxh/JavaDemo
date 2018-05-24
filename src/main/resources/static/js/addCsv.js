$.getJSON('/inputPara/getpara_type',{},function(ret){
    for(var i = 0 ; i < ret.length; i ++){
        newopt = document.createElement("option");
        newopt.text = ret[i].name;
        newopt.value = ret[i].id;
        //第二个参数是选项的指定位置
        document.getElementById("type").options.add(newopt, i+1);
    }
    for(var i = 0 ; i < ret.length; i ++){
        newopt = document.createElement("option");
        newopt.text = ret[i].name;
        newopt.value = ret[i].id;
        //第二个参数是选项的指定位置
        document.getElementById("typecsv").options.add(newopt, i+1);
    }
})
