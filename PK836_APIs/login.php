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

if (isset($_POST["PHONE_NO"]) && isset($_POST["PASSWORD"]) && isset($_POST["ROLE"])) {

    $PHONE_NO = $_POST["PHONE_NO"];
    $PASSWORD = $_POST["PASSWORD"];
    $ROLE = $_POST["ROLE"];

    $checkAvailQuery = "SELECT * FROM `login_table` WHERE  `PHONE_NO` = '$PHONE_NO' AND `ROLE` = '$ROLE'";
    $checkAvailResult = mysqli_query($db_con, $checkAvailQuery);

    $checkBlockQuery = "SELECT `STATUS` FROM `login_table` WHERE `PHONE_NO` = '$PHONE_NO' AND `STATUS` = 'BLOCKED'";
    $checkBlockResult = mysqli_query($db_con, $checkBlockQuery);

    $userQuery = "SELECT `USERID`, `F_NAME`, `L_NAME`, `EMAIL_ID`, `PASSWORD`, `PHONE_NO`, `ADDRESS`, `CITY`, `STATE`, `PINCODE`, `DOB`, `GENDER`, `ROLE`,`DP_FILE`, `REG_DATE_TIME` FROM `login_table` WHERE  `PHONE_NO` = '$PHONE_NO' AND `PASSWORD` = MD5('$PASSWORD') AND `ROLE` = '$ROLE'";
    $result = mysqli_query($db_con, $userQuery);

    if ($checkAvailResult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Data not Available";
        echo json_encode($response);
        exit;
    } else {
        if ($checkBlockResult->num_rows > 0) {

            $response["error"] = TRUE;
            $response["message"] = "Can not Login, You are Blocked!!!";
            echo json_encode($response);
            exit;
        } else {
            if ($result->num_rows == 0) {
                $response["error"] = TRUE;
                $response["message"] = "user not found or Invalid login details.";
                echo json_encode($response);
                exit;
            } else {
                $user = mysqli_fetch_assoc($result);
                $response["error"] = FALSE;
                $response["message"] = "Successfully logged in.";
                $response["user"] = $user;
                echo json_encode($response);
                exit;
            }
        }
    }
} else {

    // Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid parameters";
    echo json_encode($response);
    exit;
}
