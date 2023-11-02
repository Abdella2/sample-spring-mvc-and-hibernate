<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page
import="java.time.LocalDate" %> <%@ page
import="java.time.format.DateTimeFormatter" %> <%@ page
import="java.time.ZoneId" %> <%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Student Confirmation</title>
  </head>
  <body>
    The Student is confirmed: ${student.firstName} ${student.lastName}
    <br /><br />
    Year: ${student.year}
    <br /><br />
    Id: ${student.id}
    <br /><br />
    Country: ${student.country}
    <br /><br />
    Favorite Language: ${student.favoriteLanguage}
    <br /><br />
    Operating Systems:
    <c:forEach var="os" items="${student.operatingSystems}">
      <ul>
        <li>${os}</li>
      </ul>
    </c:forEach>
    <br /><br />

    <!-- <% Date date =
    Date.from(request.getAttribute("student.dateOfBirth").atStartOfDay(ZoneId.systemDefault()).toInstant());
    request.setAttribute("date", date); %>
    <fmt:formatDate value="${date}" pattern="dd/MM/yyyy" var="formattedDate" />
    Birthday: ${formattedDate} -->
  </body>
</html>
