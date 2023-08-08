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

$getPendCountQuery = "SELECT COUNT(*) AS `TOTAL_PENDING_APPT` FROM `appointment_details` WHERE `APNT_STATUS` = 'PENDING'";
$getPendCountresult = mysqli_query($db_con, $getPendCountQuery);

$getActiveCountQuery = "SELECT COUNT(*) AS `TOTAL_ACTIVE_APPT` FROM `appointment_details` WHERE `APNT_STATUS` = 'ACCEPTED'";
$getActiveCountresult = mysqli_query($db_con, $getActiveCountQuery);

if ($getCompCountresult == null) {
    $response["error"] = TRUE;
    $response["message"] = "Can't getting Data!!!";
    echo json_encode($response);
    exit;
} else
    $response = mysqli_fetch_assoc($getCompCountresult);
$responses = mysqli_fetch_assoc($getActiveCountresult);
$responsess = mysqli_fetch_assoc($getPendCountresult);
// $response["error"] = FALSE;
// $response["message"] = "Data Got Succesfully";
echo json_encode($response);
echo json_encode($responses);
echo json_encode($responsess);
exit;
