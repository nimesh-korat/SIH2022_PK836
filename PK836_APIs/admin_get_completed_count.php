<?php

include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

$getCompCountQuery = "SELECT COUNT(*) AS `TOTAL_COMPLETED_APPT` FROM `appointment_details` WHERE `APNT_STATUS` = 'COMPLETED'";

$getCompCountresult = mysqli_query($db_con, $getCompCountQuery);

if ($getCompCountresult == null) {
    $response["error"] = TRUE;
    $response["message"] = "Can't getting Data!!!";
    echo json_encode($response);
    exit;
} else {
    $response = mysqli_fetch_assoc($getCompCountresult);
    $response["error"] = FALSE;
    $response["message"] = "Data Got Succesfully";
    echo json_encode($response);
    exit;
}
