function openModalViewRequestDetail(id, sachDuocMuonList) {
    let Id = document.getElementById('ID_' + id).innerText;
    let tenNguoiMuon = document.getElementById('TenNguoiMuon_' + id).getAttribute('data-TenNguoiMuon');
    let ngayMuon = document.getElementById('NgayMuon_' + id).innerText;
    let ngayTra = document.getElementById('NgayTra_' + id).innerText;
    let quaHan = document.getElementById('QuaHan_' + id).getAttribute('data-QuaHan');
    let boiThuong = document.getElementById('BoiThuong_' + id).getAttribute('data-BoiThuong');
    let trangThai = document.getElementById('TrangThai_' + id).getAttribute("data-TrangThai");
    let ngayTao = document.getElementById('NgayTao_' + id).innerText;
    let ngayCapNhat = document.getElementById('NgayCapNhat_' + id).getAttribute('data-NgayCapNhat');

    $('#ID-yeucau').text(Id);
    $('#tenNguoiMuon-detail').text(tenNguoiMuon);
    $('#NgayMuon-detail').text(ngayMuon);
    $('#NgayTra-detail').text(ngayTra);
    $('#QuaHan-detail').text(quaHan);
    $('#BoiThuong-detail').text(boiThuong);
    $('#statusOptions').val(trangThai);
    $('#NgayTao-detail').text(ngayTao);
    $('#NgayCapNhat-detail').text(ngayCapNhat);

    const sachDuocMuonTableBody = $('#sachDuocMuonTableBody');
    sachDuocMuonTableBody.empty();
    if(sachDuocMuonList && sachDuocMuonList.length>0) {
        sachDuocMuonList.forEach(sachDuocMuon => {
            const $row = $("<tr></tr>");
            $row.append(`<td class="text-center">${sachDuocMuon.tenSach}</td>`);
            $row.append(`<td class="text-center">${sachDuocMuon.soLuong}</td>`);
            sachDuocMuonTableBody.append($row);
        });
    }

    //request status here
    let trangThaiNumber = Number(trangThai);
    if(trangThaiNumber===-1) {
        $("#statusOptions").attr('disabled', 'disabled');
    } else {
        $("#statusOptions > option").each(function() {
            $(this).removeAttr('disabled');
            let optionValue = Number($(this).val());
            if(!(optionValue===trangThaiNumber || optionValue===trangThaiNumber+1 || optionValue===-1)) {
                $(this).attr('disabled', 'disabled');
            }
        });
    }
    if(trangThaiNumber>=2) {
        $('#statusOptions option:first').attr('disabled', 'disabled');
    }

    switch (trangThaiNumber) {
        case -1:
            $('#status-detail').css('background-color', '#d9534f').text('Từ chối');
            break;
        case 0:
            $('#status-detail').css('background-color', '#f0ad4e').text('Chờ duyệt');
            break;
        case 1:
            $('#status-detail').css('background-color', '#5cb85c').text('Đã duyệt, chờ nhận');
            break;
        case 2:
            $('#status-detail').css('background-color', '#f0ad4e').text('Đang mượn');
            break;
        case 3:
            console.log("Đã trả");
            $('#status-detail').css('background-color', '#0275d8').text('Đã trả');
            break;
    }

    $('#detailRequestModal').modal('show');

    // Store the current item ID for later use
    document.getElementById('detailRequestModal').setAttribute('data-current-id', id);
}

$('#statusOptions').on('change', function () {
    console.log("Reached statusOptions change")
    let val = parseInt($('#statusOptions').val(), 10);
    switch (val) {
        case -1:
            $('#status-detail').css('background-color', '#d9534f').text('Từ chối');
            break;
        case 0:
            $('#status-detail').css('background-color', '#f0ad4e').text('Chờ duyệt');
            break;
        case 1:
            $('#status-detail').css('background-color', '#5cb85c').text('Đã duyệt, chờ nhận');
            break;
        case 2:
            $('#status-detail').css('background-color', '#f0ad4e').text('Đang mượn');
            break;
        case 3:
            console.log("Đã trả");
            $('#status-detail').css('background-color', '#0275d8').text('Đã trả');
            break;
    }
});

document.getElementById('yeuCauMuonSachDetails').addEventListener('submit', function(e) {
    e.preventDefault();
    if(window.confirm("Bạn có chắc chắn muốn cập nhật trạng thái cho yêu cầu này?")) {
        //send ajax request to server
        let currentId = document.getElementById('detailRequestModal').getAttribute('data-current-id');
        let updatedStatus = $('#statusOptions').val();

        $.ajax({
            method: 'POST',
            url: '/Library/management/updateRequestStatus?yeuCauId=' + currentId + '&status=' + updatedStatus,
            success: function () {
                alert("Cập nhật thành công");
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const errorResponse = jqXHR.responseText;
                if(errorResponse && errorResponse === 'Invalid status') {
                    alert("Trạng thái cập nhật không hợp lệ, vui lòng cập nhật lại");
                    console.error("Error response:", errorResponse);
                } else {
                    alert("Có lỗi, vui lòng thử lại sau");
                }
                console.error("Error details:", textStatus, errorThrown);
            }
        });
    }
});