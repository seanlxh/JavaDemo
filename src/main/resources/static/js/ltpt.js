var allData;
function realtime(){
    $.getJSON('/ajax_real_lt/',function(ret){
        allData = ret;
        var template = "<table class=\"table table-striped table-bordered\" borderColor=#000000 height=40 cellPadding=1 width=800 align=lift border=1>";
        template +=  "<tr class=\"success\"><td>key</td><td>id</td><td>resultcode</td><td>detail</td><td>updatetime</td></tr>";
        for (var i = ret.tranlist.length - 1; i >= 0; i--){
            template += "<tr>";
            template += "<td>"+ ret.tranlist[i].key  +"</td>";
            template += "<td>"+ ret.tranlist[i].tran_id +"</td>";
            template += "<td>"+ ret.tranlist[i].result_code  +"</td>";
            template += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="showJson('+i+')">消息体详情</button></td>';
            template += "<td>"+ ret.tranlist[i].dbtime  +"</td>";
            template += "</tr>";
        };
        for (var i = ret.orderlist.length - 1; i >= 0; i--){
            template += "<tr>";
            template += "<td>"+ ret.orderlist[i].key  +"</td>";
            template += "<td>"+ ret.orderlist[i].order_id +"</td>";
            template += "<td>"+ ret.orderlist[i].result_code  +"</td>";
            template += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="showJson1('+i+')">消息体详情</button></td>';
            template += "<td>"+ ret.orderlist[i].dbtime  +"</td>";
            template += "</tr>";
        };
        for (var i = ret.agglist.length - 1; i >= 0; i--){
            template += "<tr>";
            template += "<td>"+ ret.agglist[i].key  +"</td>";
            template += "<td>"+ ret.agglist[i].agg_id +"</td>";
            template += "<td>"+ ret.agglist[i].result_code  +"</td>";
            template += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="showJson2('+i+')">消息体详情</button></td>';
            template += "<td>"+ ret.agglist[i].dbtime  +"</td>";
            template += "</tr>";
        };
        template += "</table>";
        // alert(template)
        document.getElementById("tabtest").innerHTML=template ;
    })
}
function showJson(num){
    var content =  new JSONFormat(allData.tranlist[num].tran_content,4).toString();
    $("#json-result").html(content);
}

function showJson1(num){
    var content =  new JSONFormat(allData.orderlist[num].msg_content,4).toString();
    $("#json-result").html(content);
}

function showJson2(num){
    var content =  new JSONFormat(allData.agglist[num].agg_content,4).toString();
    $("#json-result").html(content);
}

function getResult(){
    var card    = document.getElementById('keywordcard');
    var nocard  = document.getElementById('keywordnotcard');
    var btype   = -1
    var idtype  = ""
    var idvalue = ""

    if(card.value != "" && nocard.value != ""){
        document.getElementById("resulttest").innerHTML="只能选择储值卡业务或者非卡业务";
        return;
    }
    if(card.value == "" && nocard.value == "" ){
        document.getElementById("resulttest").innerHTML="输入为空,请检查";
        return;
    }
    if(nocard.value != ""){
        var options=$("#notcard option:selected");
        btype   = 1
        idtype  = options.text()
        idvalue = nocard.value
    }
    if(card.value != ""){
        var options=$("#card option:selected");
        btype   = 2
        idtype  = options.text()
        idvalue = card.value
    }
    var template  = "<hr style=\"filter:alpha(opacity=0,finishopacity=100,style=1);width:80%;background-color:#0080FF;height:1px;float:left;\" />"
    template += "<div style=\"hight:100%;float:left;\">"

    $.getJSON('/ajax_check_result/',{'btype': btype, 'idtype': idtype, 'idvalue' : idvalue},function(retall){
        var rets = retall.end
        var ret1 = retall.top
        var findflag = -1
        for (one in ret1)
        {
            findflag  = 0
            template += "<table class=\"table table-striped table-bordered\" borderColor=#000000 cellPadding=1  height=40 align=lift border=1 >"
            template += "<tr  class=\"success\" bgcolor=\"#FFD1A4\"><td>"+idtype+"</td><td>"+(ret1[one][idtype])+"</td><td>"+ret1[one].dbtime+"</td><td></td></tr>"
            template += "<tr><td>商数校验</td><td>"
            template += "<table class=\"table table-striped table-bordered\">"
            if("undefined"!=typeof(ret1[one].check_result.formatcheck)){
                template += "<tr ><td>"+"消息体格式校验结果:"+"</td><td>"+JSON.stringify(ret1[one].check_result.formatcheck)+"</td></tr>"
            }
            if("undefined"!=typeof(ret1[one].check_result.resultCodeCheck)){
                template += "<tr><td>"+"数据库返回值校验结果:"+"</td><td>"+JSON.stringify(ret1[one].check_result.resultCodeCheck.data.msg_content[idvalue])+"</td></tr>"
            }
            template += "</table>";
            template += "<td><button  class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#jsonModal\"onClick=\"getBdcOrderData("+ret1[one][idtype]+","+btype+")\">查看商数数据</button></td></td>";
            template += "<td><button class=\"btn btn-primary\" onClick=\"repairDataFlow("+ret1[one][idtype]+",\'"+idtype+"\',"+btype+")\">点此重新获取</button></td></tr>";
            template += "</table>"
            template += "<hr style=\"filter:alpha(opacity=0,finishopacity=100,style=1);width:100%;background-color:#0080FF;height:1px;float:left;\" />"
        }
        template += "<table class=\"table table-striped table-bordered\" borderColor=#000000 cellPadding=1  height=40  align=lift border=1>";
        for (one in rets)
        {
            findflag  = 0
            if (idtype == "order_id" || idtype == "agg_id"){
                idtype = "tran_id"
            }
            template += "<tr  class=\"success\" bgcolor=\"#FFD1A4\"><td>"+idtype+"</td><td>"+(rets[one][idtype])+"</td><td>"+rets[one].dbtime+"</td><td></td></tr>"
            template += "<tr><td>商数校验</td><td>"
            template += "<table class=\"table table-striped table-bordered\" border=1>"
            if("undefined"!=typeof(rets[one].check_result.formatcheck)){
                template += "<tr><td>"+"消息体格式校验结果:"+"</td><td>"+JSON.stringify(rets[one].check_result.formatcheck)+"</td></tr>"
            }
            if("undefined"!=typeof(rets[one].check_result.resultCodeCheck)){
                template += "<tr><td>"+"数据库返回值校验结果:"+"</td><td>"+JSON.stringify(rets[one].check_result.resultCodeCheck)+"</td></tr>"
            }
            if("undefined"!=typeof(rets[one].check_result.marketSplitCheck)){
                template += "<tr><td>"+"营销拆分结果:"+"</td><td>"+JSON.stringify(rets[one].check_result.marketSplitCheck)+"</td></tr>"
            }
            template += "</table>";
            template += "</td>";
            if ( idtype == "topic_id"){
                template += "<td></td></tr>"
            }
            else
            {
                var tran_id_str = rets[one].tran_id.toString()
                console.log(rets[one].tran_id);
                template += "<td> <button  class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#jsonModal\"onClick=\"getBdcData(\'"+tran_id_str+"\',"+btype+")\">查看商数数据</button> </td>"
                template +="<td rowspan=\"2\"><button class=\"btn btn-primary\" onClick=\"repairDataFlow(\'"+tran_id_str+"\',\'"+idtype+"\',"+btype+")\">点此重新获取</button></td></tr>"
            }
            if( btype == 1){
                template += "<tr><td>财务校验</td><td>"
                template += "<table class=\"table table-striped table-bordered\" border=1>";
                if("undefined"!=typeof(rets[one].check_result.financeCheck)  ){
                    template += "<tr><td>"+"财务校验结果:"+"</td><td>"+JSON.stringify(rets[one].check_result.financeCheck)+"</td></tr>"
                }
                template += "</table>";
                template += "</td>";
                if ( idtype == "topic_id"){
                    template += "<td></td></tr>"
                }
                else
                {
                    template += "<td> <button  class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#jsonModal\" onClick=\"getFinanceData("+rets[one].cert_id+")\" >查看财务数据</button> </td></tr>"
                }
            }
        }
        template += "</table>";
        template += "</div>";
        //alert(template)
        if (findflag  == -1)
        {
            template = "未找到相关数据,请确认输入"
        }
        document.getElementById("resulttest").innerHTML=template ;
    })
}

$(function ()
{ $("[data-toggle='popover']").popover();
});

function getFinanceData(cert_id){
    $("#json-result").html('检索中');
    $.getJSON('/get_finance_data/',{'cert_id': cert_id},function(ret){
        var content =  new JSONFormat(ret,4).toString();
        $("#json-result").html(content);
    })
}

function getBdcData(tran_id,btype){
    $("#json-result").html('检索中');
    $.getJSON('/get_bdc_data/',{'tran_id': tran_id,'btype': btype},function(ret){
        var content =  new JSONFormat(ret,4).toString();
        $("#json-result").html(content);
    })
}

function getBdcOrderData(order_id,btype){
    $("#json-result").html('检索中');
    $.getJSON('/get_bdc_order_data/',{'order_id': order_id,'btype': btype},function(ret){
        var content =  new JSONFormat(ret,4).toString();
        $("#json-result").html(content);
    })
}

function repairDataFlow(idvalue,idtype,btype){
    $.getJSON('/repair_data_flow/',{'idvalue': idvalue,'idtype': idtype,'btype': btype},function(ret){
        var content =  new JSONFormat(ret,4).toString();
        $("#json-result").html(content);
    })
}

function checkSearchType(num){
    var card    = document.getElementById('keywordcard');
    var nocard  = document.getElementById('keywordnotcard');
    if(num == 1){
        if(card.value != "" ){
            card.removeAttribute("disabled");
            nocard.disabled = "disabled";
            nocard.value = "";
        }
    }

    if(num == 2){
        if(nocard.value != "" ){
            nocard.removeAttribute("disabled");
            card.disabled = "disabled";
            card.value = "";
        }
    }

}

