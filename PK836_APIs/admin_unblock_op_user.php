<?php
include('db.php');
header('Content-Type: application/json');

$response = array();

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["OP_USER_ID"]) && isset($_POST["UNBLOCK_REASON"]) && isset($_POST["UNBLOCKED_DATE"])) {

    $OP_USER_ID = $_POST["OP_USER_ID"];
    $UNBLOCK_REASON = $_POST["UNBLOCK_REASON"];
    $UNBLOCKED_DATE = $_POST["UNBLOCKED_DATE"];

    //check user phoneno whether its already registered
    $checkUserQuery = "select * from blocked_op_user where OP_USER_ID = '$OP_USER_ID' and STATUS = 'UNBLOCKED'";
    $checkUserresult = mysqli_query($db_con, $checkUserQuery);

    if ($checkUserresult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "User Already unblocked!!";
        echo json_encode($response);
        exit;
    } else {
        // $userBlockQuery = "INSERT INTO blocked_op_user (`OP_USER_ID`, `UNBLOCK_REASON`, `UNBLOCKED_DATE`, `STATUS`) VALUES ('$OP_USER_ID ','$UNBLOCK_REASON','$UNBLOCKED_DATE','UNBLOCKED')";
        $userBlockQuery = "UPDATE `blocked_op_user` SET `UNBLOCK_REASON` = '$UNBLOCK_REASON', `UNBLOCKED_DATE` = '$UNBLOCKED_DATE', `STATUS` = 'UNBLOCKED' WHERE `OP_USER_ID` = '$OP_USER_ID' AND `STATUS` = 'BLOCKED'";
        $userBlockResult = mysqli_query($db_con, $userBlockQuery);

        $UPDATEQuery = "UPDATE `login_table` SET `STATUS` = 'UNBLOCKED' WHERE `USERID` = '$OP_USER_ID'";
        $UPDATEResult = mysqli_query($db_con, $UPDATEQuery);
        if ($userBlockResult && $UPDATEResult) {

            $response["error"] = FALSE;
            $response["message"] = "Unblocked Succesfully...";
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
