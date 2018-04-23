/*!
 * jQuery Json Plugin (with Transition Definitions)
 * Examples and documentation at: http://json.cn/
 * Copyright (c) 2012-2013  China.Ren.
 * Version: 1.0.2 (19-OCT-2013)
 * Dual licensed under the MIT and GPL licenses.
 * http://jquery.malsup.com/license.html
 * Requires: jQuery v1.3.1 or later
 */
var cars = [];
var JSONFormat = (function(){
    var _toString = Object.prototype.toString;

    function format(object, indent_count){
        var html_fragment = '';
        switch(_typeof(object)){
            case 'Null' :0
                html_fragment = _format_null(object);
                break;
            case 'Boolean' :
                html_fragment = _format_boolean(object);
                break;
            case 'Number' :
                html_fragment = _format_number(object);
                break;
            case 'String' :
                html_fragment = _format_string(object);
                break;
            case 'Array' :
                html_fragment = _format_array(object, indent_count);
                break;
            case 'Object' :
                html_fragment = _format_object(object, indent_count);
                break;
        }
        return html_fragment;
    };

    function _format_null(object){
        return '<span class="json_null">null</span>';
    }

    function _format_boolean(object){
        return '<span class="json_boolean">' + object + '</span>';
    }

    function _format_number(object){
        return '<span class="json_number">' + object + '</span>';
    }

    function _format_string(object){
        object = object.replace(/\</g,"&lt;");
        object = object.replace(/\>/g,"&gt;");
        if(0 <= object.search(/^http/)){
            object = '<a href="' + object + '" target="_blank" class="json_link">' + object + '</a>'
        }
        return '<span class="json_string">"' + object + '"</span>';
    }

    function _format_array(object, indent_count){
        var tmp_array = [];
        for(var i = 0, size = object.length; i < size; ++i){
            tmp_array.push(indent_tab(indent_count) + format(object[i], indent_count + 1));
        }
        return '<span data-type="array" data-size="' + tmp_array.length + '"><i  style="cursor:pointer;" class="fa fa-minus-square-o" onclick="hide(this)"></i>[<br/>'
            + tmp_array.join(',<br/>')
            + '<br/>' + indent_tab(indent_count - 1) + ']</span>';
    }

    function _format_object(object, indent_count){
        var tmp_array = [];
        for(var key in object){
            tmp_array.push( indent_tab(indent_count) + '<span class="json_key">"' + key + '"</span>:' +  format(object[key], indent_count + 1));
        }
        return '<span  data-type="object"><i  style="cursor:pointer;" class="fa fa-minus-square-o" onclick="hide(this)"></i>{<br/>'
            + tmp_array.join(',<br/>')
            + '<br/>' + indent_tab(indent_count - 1) + '}</span>';
    }

    function indent_tab(indent_count){
        return (new Array(indent_count + 1)).join('&nbsp;&nbsp;&nbsp;&nbsp;');
    }

    function _typeof(object){
        var tf = typeof object,
            ts = _toString.call(object);
        return null === object ? 'Null' :
            'undefined' == tf ? 'Undefined'   :
                'boolean' == tf ? 'Boolean'   :
                    'number' == tf ? 'Number'   :
                        'string' == tf ? 'String'   :
                            '[object Function]' == ts ? 'Function' :
                                '[object Array]' == ts ? 'Array' :
                                    '[object Date]' == ts ? 'Date' : 'Object';
    };

    function loadCssString(){
        var style = document.createElement('style');
        style.type = 'text/css';
        var code = Array.prototype.slice.apply(arguments).join('');
        try{
            style.appendChild(document.createTextNode(code));
        }catch(ex){
            style.styleSheet.cssText = code;
        }
        document.getElementsByTagName('head')[0].appendChild(style);
    }

    loadCssString(
        '.json_key{ color: #92278f;font-weight:bold;}',
        '.json_null{color: #f1592a;font-weight:bold;}',
        '.json_string{ color: #3ab54a;font-weight:bold;}',
        '.json_number{ color: #25aae2;font-weight:bold;}',
        '.json_link{ color: #717171;font-weight:bold;}',
        '.json_array_brackets{}');

    var _JSONFormat = function(origin_data){
        //this.data = origin_data ? origin_data :
        //JSON && JSON.parse ? JSON.parse(origin_data) : eval('(' + origin_data + ')');
        this.data = JSON.parse(origin_data);
    };

    _JSONFormat.prototype = {
        constructor : JSONFormat,
        toString : function(){
            return format(this.data, 1);
        }
    }

    return _JSONFormat;

})();
var last_html = '';
function hide(obj){
    var data_type = obj.parentNode.getAttribute('data-type');
    var data_size = obj.parentNode.getAttribute('data-size');
    obj.parentNode.setAttribute('data-inner',obj.parentNode.innerHTML);
    if (data_type === 'array') {
        obj.parentNode.innerHTML = '<i  style="cursor:pointer;" class="fa fa-plus-square-o" onclick="show(this)"></i>Array[<span class="json_number">' + data_size + '</span>]';
    }else{
        obj.parentNode.innerHTML = '<i  style="cursor:pointer;" class="fa fa-plus-square-o" onclick="show(this)"></i>Object{...}';
    }

}

function show(obj){
    var innerHtml = obj.parentNode.getAttribute('data-inner');
    obj.parentNode.innerHTML = innerHtml;
}


function loadXMLDoc(id)
{
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        // IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // IE6, IE5 浏览器执行代码
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var obj = xmlhttp.responseText;
            $("#json-result").html(obj);
        }
    }
    var url='/execute/store?id='+id;
    for(var i = 0 ;i < cars.length; i ++){
        url += '&'+cars[i]+'='+document.getElementById(cars[i]).value;
}
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
}


function loadXMLDoc1(id)
{
    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        // IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // IE6, IE5 浏览器执行代码
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var obj = xmlhttp.responseText;
            $("#json-result").html(obj);
        }
    }
    var url='/execute/execute?id='+id;
    for(var i = 0 ;i < cars.length; i ++){
        url += '&'+cars[i]+'='+document.getElementById(cars[i]).value;
    }
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
}


function getpara(id){
    $("#json-result").html('检索中');
    while(cars.length!=0){
        cars.pop();
    }
    $.getJSON('/inputPara/inputPara',{'id': id},function(ret){
        var content =  '';
        content += ('<input type="text" name="id" value= "'+id+'"id="para" class="form-control" style="visibility: hidden;margin-top: opx;"/>');
        for (var i=0; i<ret.length; i++){
            content += ("<br>参数名："+ ret[i].name + "      类型：INTEGER</br>");
            content += ('<input type="text" name="'+ret[i].name+'" id="'+ret[i].name+'" class="form-control" style="width: 280px;" placeholder=""/>');
            cars.push(ret[i].name);
        }
        content += '<button type="button" class="btn btn-primary" onClick="loadXMLDoc1('+id+')">调用</button>';
        $("#json-result").html(content);
    })
}


function execute(id){
    $("#json-result").html('检索中');
    while(cars.length!=0){
        cars.pop();
    }
    $.getJSON('/inputPara/inputPara',{'id': id},function(ret){
        var content =  '';
        // content += ('<input type="text" name="id" value= "'+id+'"id="para" class="form-control" style="visibility: hidden;margin-top: opx;"/>');
        for (var i=0; i<ret.length; i++){
            content += ("<br>参数名："+ ret[i].name + "      类型：INTEGER</br>");
            content += ('<input type="text" name="'+ret[i].name+'" id="'+ret[i].name+'" class="form-control" style="width: 280px;" placeholder=""/>');
            cars.push(ret[i].name);
        }
        content += '<button type="button" class="btn btn-primary" onClick="loadXMLDoc('+id+')">调用</button>';
        $("#json-result").html(content);
    })
}

$.getJSON('/DataSource/showDataSource',function(ret){
    var template = "<table class=\"table table-striped table-bordered\" border=1>";
    template +=  "<tr class=\"success\"><td>数据源ID</td><td>数据源名称</td><td>描述</td><td>类型</td><td>创建时间</td><td>操作</td></tr>";
    for (var i=0; i<ret.length; i++){
        template += ("<tr>");
        template += ("<td>"+ ret[i].dsId +"</td>");
        template += ("<td>"+ ret[i].dsName +"</td>");
        template += ("<td>"+ ret[i].dsDesc +"</td>");
        template += ("<td>"+ ret[i].type +"</td>");
        template += ("<td>"+ ret[i].timestamp +"</td>");
        template += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="getpara('+ret[i].dsId+')">测试</button>&nbsp&nbsp&nbsp<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="execute('+ret[i].dsId+')">调用</button></td>';
        template += ("</tr>");
    }
    template += "</table>";
    document.getElementById("envtest").innerHTML=template ;
})



$.getJSON('/task/getTask',function(ret){
    var template = "<table class=\"table table-striped table-bordered\" border=1>";
    template +=  "<tr class=\"success\"><td>任务ID</td><td>数据源ID</td><td>状态</td><td>创建人</td><td>创建时间</td><td>操作</td></tr>";
    for (var i=0; i<ret.length; i++){
        template += ("<tr>");
        template += ("<td>"+ ret[i].taskID +"</td>");
        template += ("<td>"+ ret[i].dsID +"</td>");
        template += ("<td>"+ ret[i].state +"</td>");
        template += ("<td>"+ ret[i].userName +"</td>");
        template += ("<td>"+ ret[i].timestamp +"</td>");
        template += '<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="getpara('+ret[i].dsId+')">暂停</button>&nbsp&nbsp&nbsp<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#jsonModal" onClick="execute('+ret[i].dsId+')">结束</button></td>';
        template += ("</tr>");
    }
    template += "</table>";
    document.getElementById("tabtest").innerHTML=template ;
})

