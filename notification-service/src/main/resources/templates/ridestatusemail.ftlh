<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ride Status Update</title>
    <style>
        h2 {
            text-align: center;
        }

        #container {
            font-family: "Segoe UI", sans-serif;
            font-size: 22px;
            padding: 40px;
            display: flex;
            justify-content: center;
        }

        #main-content {
            background-color: #f6feff;
            display: inline-block;
            padding: 24px;
            border: 10px solid #006a82;
            border-radius: 45px;
            color: black;
        }
    </style>
</head>

<body>
<div id="container">
    <div id="main-content">
        <h2>Dear ${firstName}!</h2>
        <p>Status of your ride is now ${status}</p>
        <p>Start point: <b>${startPoint}</b></p>
        <p>Destination point: <b>${destinationPoint}</b></p>
        <p>Estimated cost: <b>${estimatedCost}</b></p>
    </div>
</div>
</body>
</html>