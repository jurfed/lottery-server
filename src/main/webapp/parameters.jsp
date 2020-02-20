<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>parameter input</title>
</head>
<body>

<form action="${pageContext.servletContext.contextPath}/?webpage=1" method="POST">

    <br>
    <label><font size="+2">Please enter new parameters for saving</font></label>
    </br>

    <table border="1">
        <tr>
            <td>Balance</td>
            <td>Maximum win</td>
            <td>Currency code</td>
            <td>Sound Value</td>
            <td>Language Code</td>
            <td>Current bet</td>
            <td>Current Game id</td>
        </tr>

        <tr valign="top">
            <td><input type="text" name="currentBalance" value="${currentBalance}"></td>
            <td><input type="text" name="maxWin" value="${maxWin}"></td>
            <td><input type="text" name="currencyCode" value="${currencyCode}"></td>
            <td><input type="text" name="soundValue" value="${soundValue}"></td>
            <td><input type="text" name="languageCode" value="${languageCode}"></td>
            <td><input type="text" name="currentBet" value="${currentBet}"></td>
            <td><input type="text" name="currentGame" value="${currentGameId}"></td>
        </tr>

    </table>
    <br>
    <label><font size="+2">${message}</font></label>
    </br>

    <input type="hidden" name="webpage" value="1">
    <tr>
        <td><input type="submit" align="center" value="Submit"/></td>
    </tr>
</form>

</body>
</html>
