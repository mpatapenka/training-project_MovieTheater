;

function checkPrice() {
    var eventName = $("#eventName").text();
    var dateTime = $("#seanceDateTime").text();
    var seats = [];
    $('input[name=seatNumber]:checked').map(function () {
        seats.push($(this).val());
    });

    var checkPriceUrl = $("#checkPriceUrl").val();
    $.get(checkPriceUrl + "?eventName=" + eventName + "&dateTime=" + dateTime + "&seats=" + seats, function (price, status) {
        console.log("Selling Price calculation response status: " + status);
        console.log("Selling Price calculation response data: " + JSON.stringify(price));
        $("#sellingPrice").text(price + " BYN");
    }, "json");
}

function prepareSeats() {
    var seats = [];
    $('input[name=seatNumber]:checked').map(function () {
        seats.push($(this).val());
    });
    $("#seats").val(seats);
}

function updateSeances() {
    var eventName = $("#eventName").val();
    var getEventDetailsByNameUrl = $("#eventDetailsByNameUrl").val();

    $("#dateTime").empty().append("<option value='#'>...</option>");

    $.get(getEventDetailsByNameUrl + "?eventName=" + eventName, function (event, status) {
        console.log("Event details response status: " + status);
        console.log("Event details response data: " + JSON.stringify(event));

        event["seances"].forEach(function (seance) {
            $("#dateTime").append("<option value='" + seance["dateTime"].replace(" ", "T") + "'>" + seance["dateTime"] + "</option>");
        });
    }, "json");
}