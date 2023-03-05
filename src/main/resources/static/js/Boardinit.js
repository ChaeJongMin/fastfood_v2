var index={
    init : function(){
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
        document.querySelectorAll('#btn-comment-update').forEach(function (btnItem){
            btnItem.addEventListener('click', function () {
                let updateForm=this.closest('form');
                _this.commentUpdate(updateForm);
            })
        });
        document.querySelectorAll('#btn-comment-delete').forEach(function (btnItem){
            btnItem.addEventListener('click', function () {
                _this.commentDelete(this);
            })
        });
    },
    save : function(){
        let data={
            writer: $('#writer').val(),
            title: $('#title').val(),
            content: $('#content').val()
        };
        if(!data.content){
            alert("내용이 비어있습니다.")
            return false;
        }
        if(!data.title){
            alert("제목이 비어있습니다.")
            return false;
        }
        $.ajax({
            type: 'POST',
            url : '/api/post',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
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
        if(!data.content){
            alert("내용이 비어있습니다.")
            return false;
        }
        if(!data.title){
            alert("제목이 비어있습니다.")
            return false;
        }
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
        if(!data.content){
            alert("댓글 내용이 비어있습니다.")
            return false;
        }
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
    },
    commentUpdate : function (updateForm) {
        let commentId= updateForm.querySelector('#id').value;
        let postId = $('#hidden-id').val();
        let data={
            content: updateForm.querySelector('#comment-content').value
        }
        if(!data.content){
            alert("댓글 내용이 비어있습니다.")
            return false;
        }
        $.ajax({
            type: 'PUT',
            url: '/api/update/'+postId+"/comment/"+commentId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('댓글을 수정했습니다.!!!.');
            window.location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    commentDelete : function (btnValue){
        let id=btnValue.value;
        $.ajax({
            type: 'DELETE',
            url: '/api/post/delete/comment/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('댓글이 삭제되었습니다.');
            window.location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
index.init();