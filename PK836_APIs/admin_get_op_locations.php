<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");
include('db.php');

$response = array();

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}



$response = array();

$checkLoggedInQuery = "SELECT `OP_STATUS_ID`,`OP_ACT_STATUS`,OP_LAT+0.0 AS OP_LAT,OP_LONG+0.0 AS OP_LONG,`OP_ID`,`STATUS_DATE_TIME` FROM `op_active_status` WHERE `OP_ACT_STATUS` = 'LOGGED_IN'"; // change here.
$loginresult = mysqli_query($db_con, $checkLoggedInQuery);

$loginnumrow = mysqli_num_rows($loginresult);

$checkLoggedOutQuery = "SELECT `OP_STATUS_ID`,`OP_ACT_STATUS`,OP_LAT+0.0 AS OP_LAT,OP_LONG+0.0 AS OP_LONG,`OP_ID`,`STATUS_DATE_TIME` FROM `op_active_status` WHERE `OP_ACT_STATUS` = 'LOGGED_OUT'"; // change here.
$logoutresult = mysqli_query($db_con, $checkLoggedOutQuery);

$logoutnumrow = mysqli_num_rows($logoutresult);

if ($loginresult->num_rows == 0 && $logoutresult->num_rows == 0) {
    $response["error"] = TRUE;
    $response["message"] = "Sorry no data found.";
    echo json_encode($response);
    exit;
} else {
    $data = array();

    for ($i = 1; $i <= $loginnumrow; $i++) {
        while ($val = mysqli_fetch_assoc($loginresult)) {

            $details['OP_STATUS_ID'] = $val['OP_STATUS_ID'];
            $details['OP_ID'] = $val['OP_ID'];
            $details['OP_ACT_STATUS'] = $val['OP_ACT_STATUS'];
            $details['OP_LAT']  = floatval($val['OP_LAT']);
            $details['OP_LONG']  = floatval($val['OP_LONG']);
            $details['STATUS_DATE_TIME'] = $val['STATUS_DATE_TIME'];

            array_push($data, $details);
        }
    }
    $data1 = array();

    for ($i = 1; $i <= $logoutnumrow; $i++) {
        while ($val = mysqli_fetch_assoc($logoutresult)) {

            $details['OP_STATUS_ID'] = $val['OP_STATUS_ID'];
            $details['OP_ID'] = $val['OP_ID'];
            $details['OP_ACT_STATUS'] = $val['OP_ACT_STATUS'];
            $details['OP_LAT']  = floatval($val['OP_LAT']);
            $details['OP_LONG']  = floatval($val['OP_LONG']);
            $details['STATUS_DATE_TIME'] = $val['STATUS_DATE_TIME'];

            array_push($data1, $details);
        }
    }
    $response["OP_LOGIN"] = $data; // change in response name.
    $response["OP_LOGOUT"] = $data1; // change in response name.
    $response["error"] = FALSE;
    $response["message"] = "Successfully data Found.";
    echo json_encode($response);
    exit;
}
