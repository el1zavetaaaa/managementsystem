

var baseUrlForManagers = "http://localhost:8080/managers";
function loadManagers(){
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET",baseUrlForManagers,true);
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState ===4 && xmlhttp.status ===200){
            var managers = JSON.parse(xmlhttp.responseText);
            var tbltop = `<div class="container"><table>
               <tr><h3 style="text-align: center">Manager List</h3></tr>
			    <tr><th id="col1">Id</th><th id="col1">Name</th><th id="col1">Update</th><th id="col1">Delete</th></tr>`;
            //main table content we fill from data from the rest call
            var main ="";
            for (i = 0; i < managers.length; i++){
                main +=
                    "<table>" +
                    "<tr>" +
                    "<td id='col1'>"+managers[i].id+"</td>" +
                    "<td id='col1'>"+managers[i].name+"</td>" +
                    "<td id='col1'><button onclick='updateManager("+managers[i].id+")'>Update</button>"+
                    "<td id='col1'><button onclick='deleteManager("+managers[i].id+")'>Delete</button></td>"+
                    "</tr></table> ";
            }
            var tblbottom = "</table></div>";
            var tbl = tbltop + main + tblbottom;
            document.getElementById("managersinfo").innerHTML = tbl;
        }
    };
    xmlhttp.send();
}
var baseurlForEmployees = "http://localhost:8080/employees";
function loadEmployees(){
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET",baseurlForEmployees,true);
    xmlhttp.onreadystatechange = function() {
        if(xmlhttp.readyState ===4 && xmlhttp.status ===200){
            var employees = JSON.parse(xmlhttp.responseText);
            var tbltop = `<div class="container"><table >
                 <tr><h3 style="text-align: center">Employee List</text></tr>
			    <tr><th id="col2">First Name</th><th id="col2">Last Name</th><th id="col2">Email</th><th id="col2">Update</th><th id="col2">Delete</th></tr>`;
            //main table content we fill from data from the rest call
            var main ="";
            for (i = 0; i < employees.length; i++){
                main +=
                    "<table>" +
                    "<tr>" +
                    "<td id='col2'>"+employees[i].firstName+"</td>" +
                    "<td id='col2'>"+employees[i].lastName+"</td>" +
                    "<td id='col2'>"+employees[i].email+"</td>" +
                    "<td id='col2'><button onclick='updateEmployee("+employees[i].id+")'>Update</button>"+
                    "<td id='col2'><button onclick='deleteEmployee("+employees[i].id+")'>Delete</button></td>"+
                    "</tr></table>";
            }
            var tblbottom = "</table></div>";
            var tbl = tbltop + main + tblbottom;
            document.getElementById("employeesinfo").innerHTML = tbl;
        }
    };
    xmlhttp.send();
}

function deleteManager(id){
    $.ajax({
        url: 'http://localhost:8080/managers/'+id,
        method: 'DELETE',
        success: function () {
            alert('manager has been deleted');
            loadManagers();
        },
        error: function (error) {
            alert(error);
        }
    })
}

function deleteEmployee(id){
    $.ajax({
        url: 'http://localhost:8080/employees/'+id,
        method: 'DELETE',
        success: function () {
            alert('employee has been deleted');
            loadEmployees();
        },
        error: function (error) {
            alert(error);
        }
    })
}

function updateManager(id){
    $.ajax({
        url: 'http://localhost:8080/managers/'+id,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            $("#managername").val(data.name);
            $('#managerid').val(data.id);
            loadManagers();
        },
        error: function (error) {
            alert(error);
        }
    })
}

function resetManager(){
    $("#managername").val('');
}

function updateEmployee(id){
    $.ajax({
        url: 'http://localhost:8080/employees/'+id,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            $("#employeefirstname").val(data.firstName);
            $('#employeelastname').val(data.lastName);
            $('#employeeemail').val(data.email);
            $('#employeeid').val(data.id);
            loadEmployees();
        },
        error: function (error) {
            alert(error);
        }
    })
}

function resetEmployee(){
    $("#employeefirstname").val('');
    $('#employeelastname').val('');
    $('#employeeemail').val('');
}
    window.onload = function () {
        loadManagers();
        loadEmployees();
    }
