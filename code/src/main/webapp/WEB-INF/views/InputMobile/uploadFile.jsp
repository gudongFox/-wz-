
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>附件上传</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body{
            background-color:#efeff4;
        }
        img{
            width: 20px;
            margin-right: 5px;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="weui-uploader">
        <div class="weui-uploader__hd">
            <p class="weui-uploader__title">文件上传</p>
        </div>
        <div class="weui-uploader__bd">
            <div class="weui-panel__bd">
                <div class="weui-media-box weui-media-box_small-appmsg">
                    <div class="weui-cells" id="uploadFile">



                    </div>
                </div>
            </div>
            <div class="weui-uploader__input-box">
                <input id="uploaderInput" class="weui-uploader__input" type="file" accept="*" multiple />
            </div>
            <div id="dialog1" style="display: none;">
                <div class="weui-mask"></div>
                <div class="weui-dialog">
                    <div class="weui-dialog__hd"><strong class="weui-dialog__title">添加成功</strong></div>
                    <div class="weui-dialog__bd">返回主页？</div>
                    <div class="weui-dialog__ft">
                        <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_default">取消</a>
                        <a href="javascript:;" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        $('#uploaderInput').on('change',function (event) {
            var files = event.target.files;
            if (files.length === 0) {
                return;
            }
            console.log(files[0].name)
            console.log(files[0].type)
            console.log(files[0].size)
            var appendHtml = "";
            for(var i =0;i<files.length;i++)
            {
                var fileName = files[i].name;
                var genre = fileName.split(".")[1].toLowerCase();
                var a = $("<a>");
                a.attr('class','weui-cell weui-cell_access');
                var div1 = $("<div>");
                div1.attr('class','weui-cell__hd');
                var img = $('<img>');
                div1.append(img);
                if (genre=="doc"||genre=="docx") img.attr('src','/m/img/word.png');
                else if(genre=="xls"||genre=="xlsx") img.attr('src','/m/img/excel.png');
                else if(genre=="ppt"||genre=="pptx") img.attr('src','/m/img/ppt.png');
                else if(genre=="txt") img.attr('src','/m/img/txt.png');
                else img.attr('src','/m/img/img.png');
                var div = $("<div>");
                div.attr('class',"weui-cell__bd weui-cell_primary");
                var p = $("<p>");
                p.html(files[i].name);
                div.append(p);
                a.append(div1);
                a.append(div);
                $('#uploadFile').append(a);


            }
            // $('#dialog1').css('display','block')
        })

    })

</script>
</body>
</html>