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

    const sachDuocMuonTableBody = $('#sachDuocMuonTable tbody');
    sachDuocMuonTableBody.empty();
    let sachDuocMuonList = yeuCauMuonSach.sachDuocMuonList;
    let status = yeuCauMuonSach.trangThai;
    if((status===2 || status==='2') && $('#daMatHead').length === 0) {
        $('#sachDuocMuonTable thead tr').append('<th scope="col" id="daMatHead" class="text-center">Đã mất</th>');
    } else if(!(status===2 || status==='2')) {
        $('#daMatHead').remove();
    }
    if(sachDuocMuonList && sachDuocMuonList.length>0) {
        let bookFine = 0;
        sachDuocMuonList.forEach(sachDuocMuon => {
            const $row = $("<tr></tr>");
            $row.append(`<td class="text-center">${sachDuocMuon.tenSach}</td>`);
            $row.append(`<td class="text-center">${sachDuocMuon.soTienDatCoc} đ</td>`);
            if((status===2 || status==='2') && $('#daMatHead').length !== 0) {
                let $checkbox;
                if(sachDuocMuon.trangThai===1 || sachDuocMuon.trangThai==='1') {
                    $checkbox = $(`<span style="color: #0275d8;">Đã trả</span>`);
                } else if (sachDuocMuon.trangThai===0 || sachDuocMuon.trangThai==='0') {
                    $checkbox = $(`<input type="checkbox" class="form-check-input sach" name="lost" value="${sachDuocMuon.sachId}" data-tienCoc="${sachDuocMuon.soTienDatCoc}">`);
                } else {
                    $checkbox = $(`<input type="checkbox" class="form-check-input sach" name="lost" value="${sachDuocMuon.sachId}" data-tienCoc="${sachDuocMuon.soTienDatCoc}" checked>`);
                }
                const $checkboxCell = $(`<td class="text-center"></td>`).append($checkbox);
                $row.append($checkboxCell);
            }
            sachDuocMuonTableBody.append($row);
        });
        const elements = document.querySelectorAll('.sach');
        elements.forEach(element => {
            if(element.tagName==='INPUT' && element.checked) {
                bookFine += Number(element.getAttribute('data-tienCoc'));
                console.log("bookFine: " + bookFine);
            }
        });
        let totalFine = (bookFine + yeuCauMuonSach.quaHan*1000 < yeuCauMuonSach.soTienDatCoc) ?
            bookFine + yeuCauMuonSach.quaHan*1000 : yeuCauMuonSach.soTienDatCoc;
        let totalReturn = Number(yeuCauMuonSach.soTienDatCoc) - Number(totalFine);
        $('#totalFine').text(totalFine);
        $('#totalReturn').text(totalReturn);
    }

    $('#RequestModal').modal('show');

    // Store the current item ID for later use
    document.getElementById('RequestModal').setAttribute('data-current-id', yeuCauMuonSach.id);
}

$(document).ready(function () {
    $('#reportLostBooksForm').on('submit', function (e) {
        e.preventDefault();
        if(window.confirm("Bạn có chắc chắn những sách bạn đã đánh dấu là đã mất hay không?")) {
            const idYeuCau = $('#ID-yeucau').text();
            const lostBookIdList = [];
            $('input[name="lost"]:checked').each(function() {
                lostBookIdList.push($(this).val());
            });
            const formData = {
                yeuCau: [idYeuCau],
                lost: lostBookIdList
            };
            $.ajax({
                type: 'POST',
                url: '/Library/borrowing/reportlostbooks',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function() {
                    alert('Đã thông báo sách đã mất');
                    window.location.reload();
                },
                error: function() {
                    alert('Có lỗi');
                }
            });
        }
    });
});