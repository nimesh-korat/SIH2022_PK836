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

if (isset($_POST["APPT_ID"]) && isset($_POST["OP_ID"]) && isset($_POST["REASON"])) {

    $APPT_ID = $_POST["APPT_ID"];
    $OP_ID = $_POST["OP_ID"];
    $REASON = $_POST["REASON"];

    //check user phoneno whether its already registered
    $checkAPNTQuery = "select * from appointment_details where APNT_ID = '$APPT_ID'";
    $checkAPNTresult = mysqli_query($db_con, $checkAPNTQuery);

    $checkRejectQuery = "select * from rejected_appt where APPT_ID = '$APPT_ID'";
    $checkRejectresult = mysqli_query($db_con, $checkRejectQuery);

    if ($checkAPNTresult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no such Appointment found.";
        echo json_encode($response);
        exit;
    } else if ($checkRejectresult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "Already Cancelled.";
        echo json_encode($response);
        exit;
    } else {
        $userRjectQuery = "INSERT INTO rejected_appt (`APPT_ID`, `RJCTD_BY`,`RJCT_OP_USER_ID`, `RJCT_REASON`) VALUES ('$APPT_ID', 'OPERATOR','$OP_ID','$REASON')";
        $userRjectResult = mysqli_query($db_con, $userRjectQuery);

        $UPDATEQuery = "UPDATE `appointment_details` SET `APNT_STATUS` = 'CANCELLED_BY_OPERATOR' WHERE `APNT_ID` = '$APPT_ID'";
        $UPDATEResult = mysqli_query($db_con, $UPDATEQuery);

        $UPDATEonfetchQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'CANCELLED_BY_OPERATOR' WHERE `APPT_ID` = '$APPT_ID'";
        $UPDATEonfetchResult = mysqli_query($db_con, $UPDATEonfetchQuery);

        if ($userRjectResult && $UPDATEResult && $UPDATEonfetchResult) {

            $response["error"] = FALSE;
            $response["message"] = "Successfully Cancelled.";
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
