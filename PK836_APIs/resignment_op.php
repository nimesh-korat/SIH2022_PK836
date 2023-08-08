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

if (isset($_POST["OP_ID"]) && isset($_POST["RESIGN_REASON"])) {

    $OP_ID = $_POST["OP_ID"];
    $RESIGN_REASON = $_POST["RESIGN_REASON"];

    $checkAlreadyResignQuery = "select * from `op_resign` where `OP_ID` = '$OP_ID'";
    $checkAlreadyResignResult = mysqli_query($db_con, $checkAlreadyResignQuery);

    if ($checkAlreadyResignResult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "You have already sent Resquest!!!";
        echo json_encode($response);
        exit;
        
    } else {

        $ResignQuery = "INSERT INTO `op_resign`(`OP_ID`, `RESIGN_REASON`,`RESIGN_STATUS`) VALUES ('$OP_ID', '$RESIGN_REASON','PENDING')";
        $ResignResult = mysqli_query($db_con, $ResignQuery);
        if ($ResignResult) {

            $response["error"] = FALSE;
            $response["message"] = "Successfully Request Sent.";
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
