<?php

include('db.php');

$response = array();
header('Content-Type: application/json');

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["PHONE_NO"]) && isset($_POST["PASSWORD"])) {

    $PHONE_NO = $_POST["PHONE_NO"];
    $PASSWORD = $_POST["PASSWORD"];



    $updatePassQuery = "UPDATE `login_table` SET `PASSWORD` = MD5('$PASSWORD') WHERE `PHONE_NO` = '$PHONE_NO'";
    $result = mysqli_query($db_con, $updatePassQuery);

    if ($result) {

        $response["error"] = FALSE;
        $response["message"] = "Password reset Successfully...";
        echo json_encode($response);
        exit;
    } else {
        $response["error"] = TRUE;
        $response["message"] = "Something Went Wrong!!!";
        echo json_encode($response);
        exit;
    }
} else {

    // Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid parameters";
    echo json_encode($response);
    exit;
}
