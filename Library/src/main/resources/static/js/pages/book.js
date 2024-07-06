$(document).ready(function() {
    $('input[name="tacGia"], input[name="nhaXuatBan"]').change(function(e) {
        e.preventDefault();
        $('#filterForm').submit();
    });
});