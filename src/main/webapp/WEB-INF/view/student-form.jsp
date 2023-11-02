<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
  <head>
    <style>
      .error {
        color: red;
      }
    </style>
  </head>
  <body>
    <h2>Well Come to Student Form</h2>
    <br>
    <form:form action='processForm' modelAttribute='student'>
      First Name: <form:input path='firstName'/>
      <br>
      <br>
      Last Name: <form:input path='lastName'/>
      <form:errors path='lastName' cssClass='error'/>
      <br>
      <br>
      Year: <form:input path='year' />
        <form:errors path="year" cssClass='error' />
          <br><br>
          Id: <form:input path="id" />
          <form:errors path="id" cssClass='error' />
            <br><br>
      Country:
      <form:select path='country'>
        <form:options items="${countryOptions}"/>
      </form:select>
      <br><br>
      Country:
        <form:radiobuttons path='favoriteLanguage' items="${student.countryOptions}"/>
      <br><br>
      Favorite Language:
      Java:<form:radiobutton value='java' path='favoriteLanguage' />
      C#:<form:radiobutton path='favoriteLanguage' value='C#' />
      <br><br>
      Operating Systems:
      Linux<form:checkbox path='operatingSystems' value='linux' />
      Window<form:checkbox path='operatingSystems' value='ws' />
      <br><br>
      Birthday: <form:input path='dateOfBirth'/>
      <form:errors path='dateOfBirth' cssClass='error'/>
      <br><br>
      Hobby: <form:input path='studentDetail.hobby' />
      <br><br>
      <input type="submit" value='Submit'>
    </form:form>
  </body>
</html>
