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

if (isset($_POST["USERID"])) {

    $USERID = $_POST["USERID"];

    $checkAvailQuery = "SELECT * FROM `login_table` WHERE  `USERID` = '$USERID'";
    $checkAvailResult = mysqli_query($db_con, $checkAvailQuery);

    $checkBlockQuery = "SELECT `STATUS` FROM `login_table` WHERE `USERID` = '$USERID' AND `STATUS` = 'BLOCKED'";
    $checkBlockResult = mysqli_query($db_con, $checkBlockQuery);

    $checkUnBlockQuery = "SELECT `STATUS` FROM `login_table` WHERE `USERID` = '$USERID' AND `STATUS` = 'UNBLOCKED'";
    $checkUnBlockResult = mysqli_query($db_con, $checkUnBlockQuery);

    $checkResignQuery = "SELECT `STATUS` FROM `login_table` WHERE `USERID` = '$USERID' AND `STATUS` = 'RESIGNED'";
    $checkResignResult = mysqli_query($db_con, $checkResignQuery);


    if ($checkAvailResult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "DATA_NOT_FOUND";
        echo json_encode($response);
        exit;
    } else if ($checkBlockResult->num_rows > 0) {

        $response["error"] = TRUE;
        $response["message"] = "BLOCKED";
        echo json_encode($response);
        exit;
    } else if ($checkResignResult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "RESIGNED";
        echo json_encode($response);
        exit;
    } else if ($checkUnBlockResult->num_rows > 0) {
        $response["error"] = FALSE;
        $response["message"] = "DATA_FOUND";
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
