var index={
    init : function(){
        var _this=this;
        $('#btn-save').on('click', function () {
            alert("save");
            _this.save();
        });
    },
    save : function(){
        var data={
            writer: $('#writer').val(),
            title: $('#title').val(),
            content: $('#content').val(),
        };

        // $.ajax({
        //     type: 'POST',
        //     url : '/api/post',
        //     dataType: 'json',
        //     contentType: 'application/json; charset=utf-8',
        //     data: JSON.stringify(data)
        // }).done(function(){
        //     alert("글 등록!!!!");
        //     window.location.href='/fastfood/board';
        // }).fail(function(error){
        //     alert(JSON.stringify(error));
        // });
    }
};
index.init();