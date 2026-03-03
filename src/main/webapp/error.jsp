<!DOCTYPE html>
<html>
    <head>
        <title>Error</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="styles.css"/>
    </head>
    <body>
        <script>
            alert('<%= request.getAttribute("message") %>');
            window.location.href = 'index.html';
        </script>
    </body>
</html>