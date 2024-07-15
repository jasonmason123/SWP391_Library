let formatter = new Intl.NumberFormat('en-US');
$(document).ready(function() {
    $.ajax({
        url: '/Library/management/dashboard/admin/areaChart',
        method: 'POST',
        dataType: 'json',
        success: function(data) {
            var ctx = document.getElementById("myAreaChart");
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels:Object.keys(data),
                    datasets: [{
                        label: "Số tài khoản",
                        lineTension: 0.4,
                        backgroundColor: "rgba(2,117,216,0.2)",
                        borderColor: "rgba(2,117,216,1)",
                        pointRadius: 5,
                        pointBackgroundColor: "rgba(2,117,216,1)",
                        pointBorderColor: "rgba(255,255,255,0.8)",
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(2,117,216,1)",
                        pointHitRadius: 50,
                        pointBorderWidth: 2,
                        data: Object.values(data),
                    }],
                },
                options: {
                    scales: {
                        xAxes: [{
                            time: {
                                unit: 'month'
                            },
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                maxTicksLimit: 7
                            }
                        }],
                        yAxes: [{
                            ticks: {
                                min: 0,
                                maxTicksLimit: 5
                            },
                            gridLines: {
                                color: "rgba(0, 0, 0, .125)",
                            }
                        }],
                    },
                    legend: {
                        display: false
                    }
                }
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Error fetching the area chart data:', textStatus, errorThrown);
        }
    });
});
document.addEventListener("DOMContentLoaded", function() {

    fetch('/Library/management/dashboard/staff/barChart')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.category);
            const counts = data.map(item => item.count);


            const ctx = document.getElementById('myStaffBarChart');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Số lượng blog',
                        data: counts,
                        backgroundColor: "#007bff",
                        borderColor: "rgba(2,117,216,1)",
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        xAxes: [{

                            gridLines: {
                                display: false
                            },
                            ticks: {
                                maxTicksLimit: 7
                            }
                        }],
                        yAxes: [{
                            ticks: {
                                min: 0,
                                max: 10,
                                maxTicksLimit: 4
                            },
                            gridLines: {
                                display: true
                            }
                        }],
                    },
                    legend: {
                        display: false
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching data:', error));
});

$(document).ready(function() {
    $.ajax({
        url: '/Library/management/dashboard/staff/areaChart',
        method: 'POST',
        dataType: 'json',
        success: function(data) {
            var ctx = document.getElementById("myStaffAreaChart");
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels:Object.keys(data),
                    datasets: [{
                        label: "Số yêu cầu mượn",
                        lineTension: 0.4,
                        backgroundColor: "rgba(2,117,216,0.2)",
                        borderColor: "rgba(2,117,216,1)",
                        pointRadius: 5,
                        pointBackgroundColor: "rgba(2,117,216,1)",
                        pointBorderColor: "rgba(255,255,255,0.8)",
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(2,117,216,1)",
                        pointHitRadius: 50,
                        pointBorderWidth: 2,
                        data: Object.values(data),
                    }],
                },
                options: {
                    scales: {
                        xAxes: [{
                            time: {
                                unit: 'month'
                            },
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                maxTicksLimit: 7
                            }
                        }],
                        yAxes: [{
                            ticks: {
                                min: 0,
                                maxTicksLimit: 5,

                            },
                            gridLines: {
                                color: "rgba(0, 0, 0, .125)",
                            }
                        }],
                    },
                    legend: {
                        display: false
                    }
                }
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Error fetching the area chart data:', textStatus, errorThrown);
        }
    });
});
$(document).ready(function() {
    $.ajax({
        url: '/Library/management/dashboard/admin/staffChart',
        method: 'POST',
        dataType: 'json',
        success: function(data) {
            var ctx = document.getElementById("myStaffChart");
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: "Số tài khoản",
                        lineTension: 0.4,
                        backgroundColor: "rgba(2,117,216,0.2)",
                        borderColor: "rgba(2,117,216,1)",
                        pointRadius: 5,
                        pointBackgroundColor: "rgba(2,117,216,1)",
                        pointBorderColor: "rgba(255,255,255,0.8)",
                        pointHoverRadius: 5,
                        pointHoverBackgroundColor: "rgba(2,117,216,1)",
                        pointHitRadius: 50,
                        pointBorderWidth: 2,
                        data: Object.values(data),
                    }],
                },
                options: {
                    scales: {
                        xAxes: [{
                            time: {
                                unit: 'month'
                            },
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                maxTicksLimit: 12
                            }
                        }],
                        yAxes: [{
                            ticks: {
                                min: 0,
                                maxTicksLimit: 5
                            },
                            gridLines: {
                                color: "rgba(0, 0, 0, .125)",
                            }
                        }],
                    },
                    legend: {
                        display: false
                    }
                }
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Error fetching the area chart data:', textStatus, errorThrown);
        }
    });
});
document.addEventListener("DOMContentLoaded", function() {

    fetch('/Library/management/dashboard/pieChart')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.category);
            const counts = data.map(item => item.count);
            const ctx = document.getElementById('myPieChart');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        data: counts,
                        backgroundColor: ['#007bff', '#dc3545', '#ffc107', '#28a745','#f07c29'],
                    }],
                },
            });
        })
        .catch(error => console.error('Error fetching data:', error));
});

