function openModalViewDetails(id) {
    $.ajax({
        method: 'GET',
        url: '/Library/findyeucau?yeucau=' + id,
        success: function (response) {
            generateModalViewRequestDetail(response);
        },
        error: function () {
            alert("Có lỗi");
        }
    });
}
function generateModalViewRequestDetail(yeuCauMuonSach) {
    $('#ID-yeucau').text(yeuCauMuonSach.id);
    $('#NgayMuon').text(yeuCauMuonSach.ngayMuon);
    $('#NgayTra').text(yeuCauMuonSach.ngayTra);
    $('#QuaHan').text(yeuCauMuonSach.quaHan);
    $('#BoiThuong').text(yeuCauMuonSach.boiThuong);
    $('#SoTienCocSach').text(yeuCauMuonSach.soTienDatCoc);
    $('#PhiMuonSach').text(yeuCauMuonSach.phiMuonSach);
    $('#totalDeposit').text(yeuCauMuonSach.soTienDatCoc);
    if(yeuCauMuonSach.trangThai!==3 || yeuCauMuonSach.trangThai!=='3') {
        switch (yeuCauMuonSach.trangThai) {
            case 0:
                $('#status').css('background-color', '#f0ad4e').text('Chờ mượn');
                break;
            case '0':
                $('#status').css('background-color', '#f0ad4e').text('Chờ mượn');
                break;
            case 1:
                $('#status').css('background-color', '#5cb85c').text('Đã chấp nhận, chờ nhận sách');
                break;
            case '1':
                $('#status').css('background-color', '#5cb85c').text('Đã chấp nhận, chờ nhận sách');
                break;
            case 2:
                $('#status').css('background-color', '#f0ad4e').text('Đang mượn');
                break;
            case '2':
                $('#status').css('background-color', '#f0ad4e').text('Đang mượn');
                break;
        }
    }
    if(yeuCauMuonSach.diaChiNhanSach!==null && yeuCauMuonSach.diaChiNhanSach!=='') {
        $('#diaChi-PhiVanChuyen').removeClass('d-none');
        $('#DiaChi').val(yeuCauMuonSach.diaChiNhanSach);
    } else {
        $('#diaChi-PhiVanChuyen').addClass('d-none');
        $('#PhiVanChuyen').removeAttr('required');
    }
    if(yeuCauMuonSach.phiVanChuyen!=null) {
        $('#PhiVanChuyen').text(yeuCauMuonSach.phiVanChuyen);
    }

    const sachDuocMuonTableBody = $('#sachDuocMuonTableBody');
    sachDuocMuonTableBody.empty();
    let sachDuocMuonList = yeuCauMuonSach.sachDuocMuonList;
    if(sachDuocMuonList && sachDuocMuonList.length>0) {
        sachDuocMuonList.forEach(sachDuocMuon => {
            const $row = $("<tr></tr>");
            $row.append(`<td class="text-center">${sachDuocMuon.tenSach}</td>`);
            $row.append(`<td class="text-center">${sachDuocMuon.soTienDatCoc} đ</td>`);
            sachDuocMuonTableBody.append($row);
        });
    }

    $('#RequestModal').modal('show');

    // Store the current item ID for later use
    document.getElementById('RequestModal').setAttribute('data-current-id', id);
}