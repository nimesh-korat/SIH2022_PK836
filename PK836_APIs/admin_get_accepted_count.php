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

$getAceptCountQuery = "SELECT COUNT(*) AS `TOTAL_ACTIVE_APPT` FROM `appointment_details` WHERE `APNT_STATUS` = 'ACCEPTED'";

$getAceptCountresult = mysqli_query($db_con, $getAceptCountQuery);

if ($getAceptCountresult == null) {
    $response["error"] = TRUE;
    $response["message"] = "Can't getting Data!!!";
    echo json_encode($response);
    exit;
} else
    $response = mysqli_fetch_assoc($getAceptCountresult);
$response["error"] = FALSE;
$response["message"] = "Data Got Succesfully";
echo json_encode($response);
exit;
