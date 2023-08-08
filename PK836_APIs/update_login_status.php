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

if (isset($_POST["OP_ACT_STATUS"]) && isset($_POST["OP_ID"])) {

    $OP_ACT_STATUS = $_POST["OP_ACT_STATUS"];
    $OP_ID = $_POST["OP_ID"];

    $checkApptQuery = "select * from `op_active_status` WHERE `OP_ID` = '$OP_ID'";
    $checkApptresult = mysqli_query($db_con, $checkApptQuery);

    if ($checkApptresult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "User Does't Exist!!!";
        echo json_encode($response);
        exit;
    } else {

        $acceptQuery = "UPDATE `op_active_status` SET `OP_ACT_STATUS` = '$OP_ACT_STATUS', `STATUS_DATE_TIME`= NOW() WHERE  `OP_ID` = '$OP_ID'";
        $acceptResult = mysqli_query($db_con, $acceptQuery);
        if ($acceptResult) {
            $response["error"] = FALSE;
            $response["message"] = "Succesfully $OP_ACT_STATUS";
            echo json_encode($response);
            exit;
        } else {
            $response["error"] = TRUE;
            $response["message"] = "Something Went Wrong!!!";
            echo json_encode($response);
            exit;
        }
    }
} else {
    //Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid Parameters";
    echo json_encode($response);
    exit;
}
