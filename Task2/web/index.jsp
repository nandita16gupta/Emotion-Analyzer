<%-- 
    Document   : index
    Created on : Nov 9, 2016, 3:45:50 PM
    Author     : nandita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tone Analyzer</title>
    </head>
    <body>
        <form action="getAnalysis" method="GET">
            <h1><u>Detailed analytics of your input texts are: </u></h1><br>       
            <h1><u><font color="red">Top 5 <i>Anger</i> Tone Texts: </font></u></h1><br> <p><%= request.getAttribute("aText")%></p>
            <h1><u><font color="green">Top 5 <i>Disgust</i> Tone Texts: </font></u></h1><br> <p><%= request.getAttribute("dText")%></p>
            <h1><u><font color="purple">Top 5 <i>Fear</i> Tone Texts: </font></u></h1><br> <p><%= request.getAttribute("fText")%></p>
            <h1><u><font color="orange">Top 5 <i>Joy</i> Tone Texts: </font></u></h1><br> <p><%= request.getAttribute("jText")%></p>
            <h1><u><font color="blue">Top 5 <i>Sadness</i> Tone Texts: </font></u></h1><br> <p><%= request.getAttribute("sText")%></p>
            <h1>Few more insights & analysis</h1><br>
            <p><%= request.getAttribute("highJoy")%></p>
            <p><%= request.getAttribute("highAngry")%></p>
            <p><%= request.getAttribute("total")%></p>
            <p><%= request.getAttribute("latency")%></p>
            <h1><u>Displaying all the records stored in database</u></h1><br>
            <h1>Database Dump: </h1> <p><%= request.getAttribute("dump")%></p>
        </form>
    </body>
</html>
