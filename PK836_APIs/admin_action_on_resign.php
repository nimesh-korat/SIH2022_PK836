<?php
include('db.php');
$response = array();

header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header("Access-Control-Allow-Headers: *");
header('Access-Control-Allow-Credentials', 'true');

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["RESIGN_ID"]) && isset($_POST["OP_ID"]) && isset($_POST["RESIGN_STATUS"])) {

    $RESIGN_ID = $_POST["RESIGN_ID"];
    $OP_ID = $_POST["OP_ID"];
    $RESIGN_STATUS = $_POST["RESIGN_STATUS"];

    $checkResignQuery = "select * from `op_resign` where `RESIGN_ID` = '$RESIGN_ID' AND `OP_ID` = '$OP_ID' AND `RESIGN_STATUS` = '$RESIGN_STATUS'";
    $checkResignresult = mysqli_query($db_con, $checkResignQuery);

    $checkApptQuery = "select * from `op_resign` where `RESIGN_ID` = '$RESIGN_ID'";
    $checkApptresult = mysqli_query($db_con, $checkApptQuery);


    if ($checkApptresult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Application Does't Exist!!!";
        echo json_encode($response);
        exit;
    } else if ($checkResignresult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "Already $RESIGN_STATUS";
        echo json_encode($response);
        exit;
    } else {
        $acceptQuery = "UPDATE `op_resign` SET `RESIGN_STATUS` = '$RESIGN_STATUS', `ADMIN_ACTION_DATE`= NOW() WHERE `RESIGN_ID` = '$RESIGN_ID' AND `OP_ID` = '$OP_ID'";
        $acceptResult = mysqli_query($db_con, $acceptQuery);
        if ($acceptResult) {
            $response["error"] = FALSE;
            $response["message"] = "Action Completed Succesfully...";
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
