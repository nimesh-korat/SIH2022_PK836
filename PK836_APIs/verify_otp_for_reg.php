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

if (isset($_POST["PHONE_NO"]) && isset($_POST["OTP"])) {

    $PHONE_NO = $_POST["PHONE_NO"];
    $OTP = $_POST["OTP"];

    $userQuery = "SELECT * FROM `otp_table` WHERE  `PHONE_NO` = '$PHONE_NO' AND `OTP` = '$OTP' AND `STATUS` = 'ACTIVE'";
    $result = mysqli_query($db_con, $userQuery);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Incorrect OTP!!!";
        echo json_encode($response);
        exit;
    } else {

        $updateStatusQuery = "UPDATE `otp_table` SET `STATUS` = 'INACTIVE' WHERE `PHONE_NO` = '$PHONE_NO' AND `OTP` = '$OTP' AND `STATUS` = 'ACTIVE'";
        $updateStatusresult = mysqli_query($db_con, $updateStatusQuery);

        $response["error"] = FALSE;
        $response["message"] = "OTP Successfully Verified...";
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
