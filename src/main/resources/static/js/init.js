var index={
    init : function(){
        alert("js실행");
        var _this=this;
        $('#btn-save-board').on('click', function () {
            _this.save();
        });
        $('#btn-update').on('click', function () {
            _this.update();
        });
        $('#btn-delete').on('click', function () {
            _this.delete();
        });
        $('#btn-comment-save').on('click', function () {
            _this.commentSave();
        });
    },
    save : function(){
        let data={
            writer: $('#writer').val(),
            title: $('#title').val(),
            content: $('#content').val()
        };
        $.ajax({
            type: 'POST',
            url : '/api/post',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            // success : function (data){
            //     alert("반환값: "+data);
            // }
        }).done(function(){
            alert("글 등록!!!!");
            window.location.href="/fastfood/board";
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },
    update : function (){
        let data = {
            title: $('#title-field').val(),
            content: $('#content').val()
        };
        let id = $('#hidden-id').val();
        $.ajax({
            type: 'PUT',
            url: '/api/put/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = "/fastfood/board";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function(){
        let id = $('#hidden-id').val();
        let userId=$('#hidden-userid').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/delete/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = "/fastfood/board";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    commentSave : function (){
        let id = $('#hidden-id').val();
        let data={
            content: $('#commentIn').val()
        };
        alert(data.content);
        $.ajax({
            type: 'POST',
            url: '/api/post/'+id+"/comment",
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('댓글 등록!!!.');
            window.location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });


    }
};
index.init();