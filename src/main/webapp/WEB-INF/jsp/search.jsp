<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: jfpr2
  Date: 19/05/2020
  Time: 2:53 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Page</title>

    <link rel="stylesheet" type="text/css" href="/css/main.css">
</head>
<body>
<!-- Site header -->
<jsp:include page="header.jsp"/>

<!-- Page content -->
<main class="main-content">
    <!-- The current search criteria -->
    <div id="search-criteria">
        <h4>Search Criteria</h4>
        <!-- Flight search fields -->
        <form method="get" id="searchFlight" action="/search">
            <!-- Script for displaying return date on return flights -->
            <script>
                // Function to display return date input field if return flight
                function showDiv(divId, changedId, element)
                {
                    document.getElementById(divId).style.display = element.value === "return" ? 'inline' : 'none';
                    document.getElementById(changedId).required = element.value === "return";
                }

                // Function to restrict return date to not minimum than depart
                function restrictDepart()
                {
                    document.getElementById("return").min = document.getElementById("depart").value;
                    // Disable return date until depart is selected
                    document.getElementById("return").disabled = false;
                    // Clear depart value
                    document.getElementById("return").value = "";
                }
            </script>

            <!-- Starting airport -->
            <div class="search-form-group">
                <input list="locations" name="from" id="from" value="<%=request.getParameter("from")%>" required>
                <p>to</p>
                <input list="locations" name="to" id="to" value="<%=request.getParameter("to")%>" required>
            </div>

            <!-- temp destinations -->
            <datalist id="locations">
                <option value="SYD">Sydney - SYD</option>
                <option value="GIG">Rio De Janeiro - GIG</option>
                <option value="LAX">Los Angeles - LAX</option>
            </datalist>

            <div class="search-form-group">
                <select id="type" name="type" onchange="showDiv('search-form-return-date', 'return', this)" value="<%=request.getParameter("type")%>">
                    <option value="oneway">One-way</option>
                    <option value="return">Return</option>
                </select>
            </div>

            <div class="search-form-group">
                <input type="number" id="adults" name="adults" min="1" max="9" value="<%=request.getParameter("adults")%>" required>
                <p>Adult/s</p>
                <input type="number" id="children" name="children" min="0" max="9" value="<%=request.getParameter("children")%>" required>
                <p>Children</p>
            </div>
            <br>

            <div class="search-form-group">
                <div class="home-form-group">
                    <jsp:useBean id="now" class="java.util.Date"/>
                    <input type="date" id="depart" name="depart" min="<fmt:formatDate pattern="yyyy-MM-dd" value="${now}" />"
                           onchange="restrictDepart()" value="<%=request.getParameter("depart")%>" required>
                </div>
            </div>

            <div id="search-form-return-date">
                <p>to</p>
                <input type="date" id="return" name="return" min="<fmt:formatDate pattern="yyyy-MM-dd" value="${now}" />"
                       value="<%=request.getParameter("return")%>" disabled>
            </div>
        </form>
    </div>

    <!-- Result template -->
    <div class="flight-result">
        <div class="flight-result-time">
            <p>Depart time</p>
            <h4>00:00AM</h4>
            <p>Arrival time</p>
            <h4>00:00AM</h4>
        </div>
        <div class="flight-result-details">
            <p>Company</p>
            <p>Class Type</p>
            <p>Flight ID</p>
        </div>
        <div class="flight-result-cost">
            <h3>$000</h3>
        </div>
        <div class="flight-result-book">
            <button type="button">Book</button>
        </div>
    </div>

    <!-- Parse all returned flights -->
    <c:forEach items="${flights}" var="flight">
        <div class="flight-result">
            <div class="flight-result-time">
                <p>Depart time</p>
                <h4>${flight.departTime}</h4>
                <p>Arrival time</p>
                <h4>${flight.arrivalTime}</h4>
            </div>
            <div class="flight-result-details">
                <p>${flight.company}</p>
                <p>${flight.classType}</p>
                <p>${flight.id}</p>
            </div>
            <div class="flight-result-cost">
                <h3>${flight.cost}</h3>
            </div>
            <div class="flight-result-book">
                <button type="button">Book</button>
            </div>
        </div>
    </c:forEach>
</main>
</body>
</html>
