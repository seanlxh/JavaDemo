/**
 * Created by ZhangYE on 2016/5/9.
 */
//交互效果
$(function() {
    var _uat=navigator.userAgent;
    if(_uat.indexOf("MSIE 6.0")>0) alert("您正在使用 IE6 浏览器，部分功能可能因兼容性无法实现！推荐使用 Chrome / Firefox / Safari以及 IE9 以上浏览器浏览本站。");
    else if(_uat.indexOf("MSIE 7.0")>0) alert("您正在使用 IE7 浏览器，部分功能可能因兼容性无法实现！推荐使用 Chrome / Firefox /Safari以及 IE9 以上浏览器浏览本站。");
    //部门数据加载#A-G
    getOrg(1);
    //部门数据加载#H-N
    getOrg(2);
    //部门数据加载#O-T
    getOrg(3);
    //部门数据加载#U-Z
    getOrg(4);
    //数据目录列表页面第一页展示
    queryResByPage(1);
    //筛选条件点击切换
    $('.filter-list li').each(function(){
        $(this).click(function(event) {
            $(this).siblings('li').removeClass('active');
            $(this).addClass('active');
            var filter_obj = $(this).text();//获取点击筛选条件
            var parent_obj = $(this).parents('.filter-body').siblings('.filter-header').text();//获取点击筛选条件的类别
            //"分类"类别下操作
            if(parent_obj=='分类：'){
                if(filter_obj=='推荐'){
                    $('.theme-first-list').show();
                    $('.theme-second-list').hide();
                    $('.theme-first-list li').removeClass('active');
                    $('.theme-second-list li').removeClass('active');

                    $('.filter-theme-part').hide();
                    $('.filter-depart-part').hide();
                    $('.sel-first').text('');
                    $('.sel-first').parent('li').hide();
                    $('.sel-second').text('');
                    $('.sel-second').parent('li').hide();
                    $('.sel-depart').text('');
                    $('.sel-depart').parent('li').hide();
                    //推荐去掉筛选项
                    $('.sort-list ul li').each(function(){
                    	if($(this).index() == 0){
                    		$(this).addClass('active');
                    		$("#orderType").val($(this).attr("id"));
                    	}else{
                    		 $(this).removeClass('active');
                    	}
                    });
                    $("#orgId").val("");
                    $("#subjectId").val("");
                    $("#key").val("");
                    queryResByPage(1);
                }
                if(filter_obj=='领域'){
                    $('.filter-depart-part').hide();
                    $('.filter-theme-part').slideToggle();
                }
                if(filter_obj=='机构'){
                    $('.filter-theme-part').hide();
                    $('.filter-depart-part').slideToggle();
                }
            }
            //其他类别添加已选条件
            else{
                if(filter_obj!='不限'){
                    if(parent_obj=='标签：'){
                        $('.sel-tag').text(filter_obj);
                        $('.filter-selected').fadeIn();
                        $('.sel-tag').parent('li').show();
                        $("#tag").val($(this).text());
                        queryResByPage(1);
                    }
                    if(parent_obj=='格式：'){
                        $('.sel-format').text(filter_obj);
                        $('.filter-selected').fadeIn();
                        $('.sel-format').parent('li').show();
                        $("#fileType").val($(this).attr("rel"));
                        queryResByPage(1);
                    }
                    if(parent_obj=='评级：'){
                        $('.sel-star').text(filter_obj);
                        $('.filter-selected').fadeIn();
                        $('.sel-star').parent('li').show();
                    }
                    if(parent_obj=='价格：'){
                        $('.sel-price').text(filter_obj);
                        $('.filter-selected').fadeIn();
                        $('.sel-price').parent('li').show();
                    }
                }
                //点击不限
                if(filter_obj=='不限'){
                    $(this).siblings('li').removeClass('active');
                    if(parent_obj=='标签：'){
                        $('.sel-tag').text('');
                        $('.sel-tag').parent('li').hide();
                        $("#tag").val("");
                        queryResByPage(1);
                    }
                    if(parent_obj=='格式：'){
                        $('.sel-format').text('');
                        $('.sel-format').parent('li').hide();
                        $("#fileType").val("");
                        queryResByPage(1);
                    }
                    if(parent_obj=='评级：'){
                        $('.sel-star').text('');
                        $('.sel-star').parent('li').hide();
                    }
                    if(parent_obj=='价格：'){
                        $('.sel-price').text('');
                        $('.sel-price').parent('li').hide();
                    }
                }
            }
        });
    });
    //主题一级列表点击事件
    $('.theme-first-list li').each(function(){
        $(this).click(function(event) {
            $(this).siblings('li').removeClass('active');
            $(this).addClass('active');
        	var subjectId = $(this).attr("id");
        	$("#subjectId").val(subjectId);
        	$(".filter-theme-part").hide();
            //添加到已选条件
            $('.sel-first').text($(this).find('span').text());
            $('.filter-selected').fadeIn();
            $('.sel-first').parent('li').show();
            queryResByPage(1);
        });
    });
    //部门点击事件
    $(".depart-list").on("click","li",function(){
        $('.depart-list li').removeClass('active');
        $(this).addClass('active');
        //添加到已选条件
        $('.sel-depart').text($(this).text());
        $('.filter-selected').fadeIn();
        $('.sel-depart').parent('li').show();
        var orgId = $(this).attr("rel");
        $("#orgId").val(orgId);
        $(".filter-depart-part").hide();
        queryResByPage(1);
    });
    //删除全部已选条件
    $('.selected-empty').click(function(event) {
        $('.selected-body').text('');
        $('.filter-selected li').hide();
        $('.theme-first-list').show();
        $('.theme-second-list').hide();
        $('.theme-first-list li').removeClass('active');
        $('.theme-second-list li').removeClass('active');
        $('.filter-theme-part').hide();
        $('.filter-depart-part').hide();
        $('.filter-list li').removeClass('active');
        $('.filter-list').each(function(){
            $(this).children('li').eq(0).addClass('active');
        });
        $('.sort-list ul li').each(function(){
        	if($(this).index() == 0){
        		$(this).addClass('active');
        		$("#orderType").val($(this).attr("id"));
        	}else{
        		 $(this).removeClass('active');
        	}
        });
        $("#orgId").val("");
        $("#subjectId").val("");
        $("#key").val("");
        queryResByPage(1);
        
    });
    //删除单个已选条件
    $('.filter-selected ul li i').click(function(event) {
        var sel_obj = $(this).parent('li').index();//获取点击的已选类别
        if(sel_obj==0){//删除一级主题
            $('.theme-first-list').show();
            $('.theme-second-list').hide();
            $('.theme-first-list li').removeClass('active');
            $('.theme-second-list li').removeClass('active');
            //去掉一级主题
            $(this).siblings('.selected-body').text('');
            $(this).parent('li').hide();
            //去掉二级主题
            $('.filter-selected ul li').eq(1).find('.selected-body').text('');
            $('.filter-selected ul li').eq(1).hide();
            $("#childsubjectId").val("");
            $("#subjectId").val("");
            if("" == $(".sel-depart").text()){
                $('.filter-list li').each(function(){
                	if($(this).index() == 0){
                		$(this).addClass('active');
                	}else{
                		 $(this).removeClass('active');
                	}
                });
            }
            queryResByPage(1);
        }
        if(sel_obj==1){//删除部门
            $('.depart-list li').removeClass('active');
            $(this).siblings('.selected-body').text('');
            $(this).parent('li').hide();
            $("#orgId").val("");
            if("" == $(".sel-first").text()){
                $('.filter-list li').each(function(){
                	if($(this).index() == 0){
                		$(this).addClass('active');
                	}else{
                		 $(this).removeClass('active');
                	}
                });
            }
            queryResByPage(1);
        }
    });
    //判断是否显示已选条件
    $('.cata-filter').click(function() {
        var count_obj = 0;
        $('.filter-selected li').each(function() {
            var display_obj = $(this).css('display');
            if (display_obj != 'none') {
                count_obj++;
            }

        });
        if (count_obj == 0) {
            $('.filter-selected').hide();
        }
    });
    //收起筛选菜单
    var switch_obj=1;
    $('.filter-toggle-btn').click(function(event) {
        $('.filter-toggle').slideToggle();
        if(switch_obj==1){
            $(this).html('展开<i class="iconfont">&#xe726;</i>');
            switch_obj=0;
        }
        else{
            $(this).html('收起<i class="iconfont">&#xe725;</i>');
            switch_obj=1;
        }
    });
    //排序点击切换
    $('.sort-list ul li').each(function(){
        $(this).click(function(event) {
            $(this).siblings('li').removeClass('active');
            $(this).addClass('active');
            var sort = $(this).attr("id");
            $("#orderType").val(sort);
            queryResByPage(1);
        });
    });
    //列表/卡片展现形式点击切换
    $('.sort-itemstyle div').each(function(){
        $(this).click(function(event) {
            if($(this).index()==1){
                $('.cata-list').hide();
                $('.cata-card').fadeIn();
            }
            else if($(this).index()==0){
                $('.cata-card').hide();
                $('.cata-list').fadeIn();
            }
            $(this).siblings('div').removeClass('active');
            $(this).addClass('active');
        });
    });
    //下载排行-改变颜色
    $('.rank-list li').each(function(){
        if($(this).index()<3){
            $(this).find('span').css('background-color','rgb(23, 144, 207)');
        }
    });
    //返回顶部
    $(function () {
        $(window).scroll(function(){
            if ($(window).scrollTop()>100){
                $(".back-top").fadeIn(1500);
            }
            else{
                $(".back-top").fadeOut(1500);
            }
        });
        //当点击跳转链接后，回到页面顶部位置
        $(".back-top").click(function(){
            $('body,html').animate({scrollTop:0},1000);
            return false;
        });
    });
    
  //实现enter键搜索的功能
    $("#key").keydown(function(event){
    	if(event.which == "13")    
    		queryResByPage(1);
    });
    
    $("#keySearch").click(function(){
    	queryResByPage(1);
    });
})