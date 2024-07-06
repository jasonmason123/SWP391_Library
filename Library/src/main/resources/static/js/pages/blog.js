$(document).ready(function() {
    $('input[name="tag"]').change(function(e) {
        e.preventDefault();
        $('#filterForm').submit();
    });
});