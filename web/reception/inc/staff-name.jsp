<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<span class="staff-name"><c:out value="${sessionScope.loggedInUser.hoTen}" default="Lễ tân"/></span>
