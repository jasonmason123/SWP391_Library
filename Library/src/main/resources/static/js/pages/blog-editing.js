function submitBlog() {
    if(window.confirm("Bạn có chắc chắn đã hoàn thành và muốn gửi bài viết không? Bạn sẽ không thể chỉnh sửa bài viết sau khi gửi đi (trừ trường hợp bài viết bị từ chối phát hành)")) {
        let blogData = {
            id: $('#blog-submit-form').data('blogid'),
            tieuDe: $('#tieu-de').val(),
            noiDung: tinyMCE.get('content').getContent(),
            tagIds: $('#tags').val()
        }
        $.ajax({
            url: '/Library/submitblog',
            method: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(blogData),
            success: function () {
                alert("Bài viết đã được gửi đi và đang chờ xét duyệt, vui lòng kiểm tra email để cập nhật trạng thái bài viết của bạn");
            },
            error: function () {
                alert("Gửi không thành công, vui lòng thử lại sau!");
            }
        });
    }
}

function saveDraft() {
    if(window.confirm("Bạn có chắc chắn muốn lưu bài viết dưới dạng nháp không?")) {
        let blogid = $('#blog-submit-form');
        let blogData = {
            id: blogid.data('blogid'),
            tieuDe: $('#tieu-de').val(),
            noiDung: tinyMCE.get('content').getContent(),
            tagIds: $('#tags').val()
        }
        console.log(blogData);
        $.ajax({
            url: '/Library/savedraft',
            method: 'POST',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(blogData),
            success: function (response) {
                if (!blogid.data('blogid')) {
                    $('#blog-submit-form').data('blogid', response); // Set blogid if not already set
                }
                alert("Bài viết đã được lưu dưới dạng nháp");
            },
            error: function () {
                alert("Gửi không thành công, vui lòng thử lại sau!");
            }
        });
    }
}

function showPreview() {
    let content = tinyMCE.get('content').getContent();
    $('#preview-title').text($('#tieu-de').val());
    $('#preview-content').html(content);
    $('#edit-section').addClass('d-none');
    $('#preview-section').removeClass('d-none');
}

function showEdit() {
    $('#edit-section').removeClass('d-none');
    $('#preview-section').addClass('d-none');
}

$(document).ready(function () {
    tinymce.init({
        selector: '#content',
        plugins: 'image media link',
        toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
        image_title: true,
        automatic_uploads: true,
        file_picker_types: 'image',
        content_style: "body { font-size: 14pt; }",
        setup: function (editor) {
            editor.on('init', function () {
                let initialContent = $('#content').data('noidung');
                editor.setContent(initialContent || '');
            });
        }
    });
    $('#tags').select2({
        placeholder: 'Tìm và chọn tag',
        allowClear: true,
        width: 'resolve',
        dropdownAutoWidth: true,
        theme: 'classic'
    });
    $('#submit-blog').on('click', function (e) {
        e.preventDefault();
        let tieude = $('#tieu-de').val();
        let noidung = tinyMCE.get('content').getContent();
        if(tieude!=='' && noidung!=='') {
            submitBlog();
        } else {
            alert('Tiêu đề và nội dung không được để trống');
        }
    });
    $('#save-draft').on('click', function (e) {
        e.preventDefault();
        let tieude = $('#tieu-de').val();
        let noidung = tinyMCE.get('content').getContent();
        if(tieude!=='' && noidung!=='') {
            saveDraft();
        } else {
            alert('Tiêu đề và nội dung không được để trống');
        }
    });
    $('#preview-btn').on('click', function() {
        showPreview();
    });
    $('#edit-btn').on('click', function() {
        showEdit();
    });
});