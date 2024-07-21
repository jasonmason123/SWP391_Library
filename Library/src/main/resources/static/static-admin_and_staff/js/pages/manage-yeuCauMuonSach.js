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
    let soTienDatCoc = document.getElementById('SoTienDatCoc_' + id).getAttribute('data-SoTienDatCoc');
    let phiMuonSach = document.getElementById('PhiMuonSach_' + id).getAttribute('data-PhiMuonSach');
    let diaChi = document.getElementById('DiaChi_' + id).getAttribute('data-DiaChi');
    let phiVanChuyen = document.getElementById('PhiVanChuyen_' + id);

    $('#ID-yeucau').text(Id);
    $('#tenNguoiMuon-detail').text(tenNguoiMuon);
    $('#NgayMuon-detail').text(ngayMuon);
    $('#NgayTra-detail').text(ngayTra);
    $('#QuaHan-detail').text(quaHan);
    $('#BoiThuong-detail').text(boiThuong);
    $('#statusOptions').val(trangThai);
    $('#NgayTao-detail').text(ngayTao);
    $('#NgayCapNhat-detail').text(ngayCapNhat);
    $('#SoTienCocSach-detail').text(soTienDatCoc);
    $('#PhiMuonSach-detail').text(phiMuonSach);
    if(diaChi!==null && diaChi!=='') {
        $('#diaChi-PhiVanChuyen-detail').removeClass('d-none');
        $('#DiaChi-detail').val(diaChi);
        $('#PhiVanChuyen-detail').attr('required', 'required');
        if(trangThai!=='0') {
            $('#PhiVanChuyen-detail').removeAttr('required').attr('disabled', 'disabled');
        }
    } else {
        $('#diaChi-PhiVanChuyen-detail').addClass('d-none');
        $('#PhiVanChuyen-detail').removeAttr('required');
    }
    if(phiVanChuyen!=null) {
        $('#PhiVanChuyen-detail').val(phiVanChuyen.getAttribute('data-PhiVanChuyen'));
    }

    const sachDuocMuonTableBody = $('#sachDuocMuonTableBody');
    sachDuocMuonTableBody.empty();
    let status = $('#statusOptions').val();
    if((status===2 || status==='2') && $('#daTiepNhanHead').length === 0) {
        $('#sachDuocMuonTable thead tr').append('<th scope="col" id="daTiepNhanHead" class="text-center">Đã tiếp nhận</th>');
    } else if(!(status===2 || status==='2')) {
        $('#daTiepNhanHead').remove();
        $('.sachDaTiepNhan').remove();
    }
    if(sachDuocMuonList && sachDuocMuonList.length>0) {
        let bookFine = 0;
        sachDuocMuonList.forEach(sachDuocMuon => {
            const $row = $("<tr></tr>");
            $row.append(`<td class="text-center">${sachDuocMuon.tenSach}</td>`);
            $row.append(`<td class="text-center">${sachDuocMuon.soTienDatCoc} đ</td>`);
            if((status===2 || status==='2') && $('#daTiepNhanHead').length !== 0) {
                let $checkbox;
                if(sachDuocMuon.trangThai===1 || sachDuocMuon.trangThai==='1') {
                    $checkbox = $(`<input type="checkbox" class="sachDaTiepNhan form-check-input" name="sachDaTra" data-tienCoc="${sachDuocMuon.soTienDatCoc}" checked disabled>`);
                } else if(sachDuocMuon.trangThai===0 || sachDuocMuon.trangThai==='0') {
                    $checkbox = $(`<input type="checkbox" class="sachDaTiepNhan form-check-input" name="sachDaTra" data-tienCoc="${sachDuocMuon.soTienDatCoc}">`);
                } else {
                    $checkbox = $('<span class="badge badge-danger">Đã mất/hỏng</span>');
                }
                $checkbox.attr('value', sachDuocMuon.sachId);
                const $checkboxCell = $('<td class="text-center"></td>').append($checkbox);
                $row.append($checkboxCell);
            }
            sachDuocMuonTableBody.append($row);
        });
        const checkboxes = document.querySelectorAll('input[name="sachDaTra"]');
        checkboxes.forEach(checkbox => {
            if(!checkbox.checked) {
                bookFine += Number(checkbox.getAttribute('data-tienCoc'));
            }
        });
        let totalFine = (bookFine + quaHan*1000 < soTienDatCoc) ? bookFine + quaHan*1000 : soTienDatCoc;
        let totalReturn = Number(soTienDatCoc) - Number(totalFine);
        if(Number(trangThai) >= 2) {
            $('#totalDepositWrapper').show();
            $('#totalDeposit').text(soTienDatCoc);
            $('#totalFineWrapper').show();
            $('#totalFine').text(totalFine);
            $('#totalReturnWrapper').show();
            $('#totalReturn').text(totalReturn);
        } else {
            $('#totalDepositWrapper').hide();
            $('#totalFineWrapper').hide();
            $('#totalReturnWrapper').hide();
        }
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

function openModalViewRequestDetailNotFix(id, sachDuocMuonList) {
    let Id = document.getElementById('ID_' + id).innerText;
    let tenNguoiMuon = document.getElementById('TenNguoiMuon_' + id).getAttribute('data-TenNguoiMuon');
    let ngayMuon = document.getElementById('NgayMuon_' + id).innerText;
    let ngayTra = document.getElementById('NgayTra_' + id).innerText;
    let quaHan = document.getElementById('QuaHan_' + id).getAttribute('data-QuaHan');
    let boiThuong = document.getElementById('BoiThuong_' + id).getAttribute('data-BoiThuong');
    let trangThai = document.getElementById('TrangThai_' + id).getAttribute("data-TrangThai");
    let ngayTao = document.getElementById('NgayTao_' + id).innerText;
    let ngayCapNhat = document.getElementById('NgayCapNhat_' + id).getAttribute('data-NgayCapNhat');
    let soTienDatCoc = document.getElementById('SoTienDatCoc_' + id).getAttribute('data-SoTienDatCoc');
    let phiMuonSach = document.getElementById('PhiMuonSach_' + id).getAttribute('data-PhiMuonSach');
    let diaChi = document.getElementById('DiaChi_' + id).getAttribute('data-DiaChi');
    let phiVanChuyen = document.getElementById('PhiVanChuyen_' + id);

    $('#ID-yeucau-notfix').text(Id);
    $('#tenNguoiMuon-notfix').text(tenNguoiMuon);
    $('#NgayMuon-notfix').text(ngayMuon);
    $('#NgayTra-notfix').text(ngayTra);
    $('#QuaHan-notfix').text(quaHan);
    $('#BoiThuong-notfix').text(boiThuong);
    $('#statusOptions').text(trangThai);
    $('#NgayTao-notfix').text(ngayTao);
    $('#NgayCapNhat-notfix').text(ngayCapNhat);
    $('#SoTienCocSach-notfix').text(soTienDatCoc);
    $('#PhiMuonSach-notfix').text(phiMuonSach);
    $('#totalDepositNotFix').text(soTienDatCoc);
    $('#totalFineNotFix').text(boiThuong);
    $('#totalReturnNotFix').text(Number(soTienDatCoc) - Number(boiThuong));
    $('#status-notfix').css('background-color', '#0275d8').text('Đã trả');
    if(diaChi!==null && diaChi!=='') {
        $('#diaChi-PhiVanChuyen-notfix').removeClass('d-none');
        $('#DiaChi-notfix').val(diaChi);
    } else {
        $('#diaChi-PhiVanChuyen-notfix').addClass('d-none');
        $('#PhiVanChuyen-notfix').removeAttr('required');
    }
    if(phiVanChuyen!=null) {
        $('#PhiVanChuyen-notfix').text(phiVanChuyen.getAttribute('data-PhiVanChuyen'));
    }

    const sachDuocMuonTableBody = $('#sachDuocMuonTableBodyNotFix');
    sachDuocMuonTableBody.empty();
    if(sachDuocMuonList && sachDuocMuonList.length>0) {
        sachDuocMuonList.forEach(sachDuocMuon => {
            const $row = $("<tr></tr>");
            $row.append(`<td class="text-center">${sachDuocMuon.tenSach}</td>`);
            $row.append(`<td class="text-center">${sachDuocMuon.soTienDatCoc} đ</td>`);
            let $trangThaiRow = $(`<td class="text-center"></td>`);
            let $trangThai = $(`<span></span>`);
            if(sachDuocMuon.trangThai===1 || sachDuocMuon.trangThai==='1') {
                $trangThai.text('Đã trả').attr('class', 'badge badge-primary');
            } else {
                $trangThai.text('Đã mất/hỏng').attr('class', 'badge badge-danger');
            }
            $trangThaiRow.append($trangThai);
            $row.append($trangThaiRow);
            sachDuocMuonTableBody.append($row);
        });
    }

    $('#notfixRequestModal').modal('show');

    // Store the current item ID for later use
    document.getElementById('notfixRequestModal').setAttribute('data-current-id', id);
}

$('#statusOptions').on('change', function () {
    console.log("Reached statusOptions change")
    let val = parseInt($('#statusOptions').val(), 10);
    switch (val) {
        case 0:
            $('#status-detail').css('background-color', '#d9534f').text('Chờ duyệt');
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
    let updatedStatus = $('#statusOptions').val();
    let message;
    if(updatedStatus===3 || updatedStatus==='3') {
        message = "Khi cập nhật lên 'Đã trả', những sách chưa được tiếp nhận sẽ được coi như là đã mất, bạn có chắc chắn muốn cập nhật không?";
    } else {
        message = "Bạn có chắc chắn muốn cập nhật yêu cầu này?";
    }
    if(window.confirm(message)) {
        //send ajax request to server
        let currentId = document.getElementById('detailRequestModal').getAttribute('data-current-id');
        let phiVanChuyen = $('#PhiVanChuyen-detail').val();
        let url = '/Library/management/manageBookBorrowed/updateRequestStatus';
        const sachDaTraList = Array.from(document.querySelectorAll('input[name="sachDaTra"]:checked'))
            .map(checkbox => checkbox.value);
        let data = {
            yeuCauId: currentId,
            status: updatedStatus,
            phiVanChuyen: phiVanChuyen,
            sachDaTraList: sachDaTraList
        }
        $.ajax({
            method: 'POST',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function () {
                alert("Cập nhật thành công");
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const errorResponse = jqXHR.responseText;
                if(errorResponse && errorResponse === 'Invalid status') {
                    alert("Trạng thái cập nhật không hợp lệ, vui lòng cập nhật lại");
                } else if(errorResponse && errorResponse === 'Insufficient amount') {
                    alert("Số lượng sách trong kho không đủ đáp ứng yêu cầu này");
                } else if(errorResponse && errorResponse === 'No returned books') {
                    alert("Chưa thể cập nhật trạng thái yêu cầu lên 'Đã trả' khi chưa tiếp nhận sách");
                } else {
                    alert("Có lỗi, vui lòng thử lại sau");
                }
                console.error("Error details:", textStatus, errorThrown);
            }
        });
    }
});