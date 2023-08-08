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

if (isset($_POST["OP_USER_ID"]) && isset($_POST["BLOCK_REASON"]) && isset($_POST["BLOCK_DATE"])) {

    $OP_USER_ID = $_POST["OP_USER_ID"];
    $BLOCK_REASON = $_POST["BLOCK_REASON"];
    $BLOCK_DATE = $_POST["BLOCK_DATE"];


    //check user phoneno whether its already registered
    $checkUserQuery = "select * from blocked_op_user where OP_USER_ID = '$OP_USER_ID' and STATUS = 'BLOCKED'";
    $checkUserresult = mysqli_query($db_con, $checkUserQuery);

    $checkUserUBQuery = "select * from blocked_op_user where OP_USER_ID = '$OP_USER_ID' and STATUS = 'UNBLOCKED'";
    $checkUserUBresult = mysqli_query($db_con, $checkUserUBQuery);

    $userBlockAQuery = "UPDATE `blocked_op_user` SET `BLOCK_REASON` = '$BLOCK_REASON', `BLOCK_DATE` = '$BLOCK_DATE', `STATUS` = 'BLOCKED' WHERE `OP_USER_ID` = '$OP_USER_ID' AND `STATUS` = 'BLOCKED'";
    $userBlockAResult = mysqli_query($db_con, $userBlockAQuery);


    if ($checkUserresult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "User Already Blocked!!";
        echo json_encode($response);
        exit;
    } else if ($checkUserUBresult->num_rows > 0) {

        $userBlockAQuery = "UPDATE `blocked_op_user` SET `BLOCK_REASON` = '$BLOCK_REASON', `BLOCK_DATE` = '$BLOCK_DATE', `STATUS` = 'BLOCKED' WHERE `OP_USER_ID` = '$OP_USER_ID' AND `STATUS` = 'UNBLOCKED'";
        $userBlockAResult = mysqli_query($db_con, $userBlockAQuery);

        $UPDATEQuery = "UPDATE `login_table` SET `STATUS` = 'BLOCKED' WHERE `USERID` = '$OP_USER_ID'";
        $UPDATEResult = mysqli_query($db_con, $UPDATEQuery);

        if ($userBlockAResult && $UPDATEResult) {

            $response["error"] = FALSE;
            $response["message"] = "Again Blocked Succesfully...";
            echo json_encode($response);
            exit;
        } else {

            $response["error"] = TRUE;
            $response["message"] = "Something Went Wrong!!!";
            echo json_encode($response);
            exit;
        }
    } else {
        $userBlockQuery = "INSERT INTO blocked_op_user (`OP_USER_ID`, `BLOCK_REASON`, `BLOCK_DATE`, `STATUS`) VALUES ('$OP_USER_ID ','$BLOCK_REASON','$BLOCK_DATE','BLOCKED')";
        $userBlockResult = mysqli_query($db_con, $userBlockQuery);

        $UPDATEQuery = "UPDATE `login_table` SET `STATUS` = 'BLOCKED' WHERE `USERID` = '$OP_USER_ID'";
        $UPDATEResult = mysqli_query($db_con, $UPDATEQuery);
        if ($userBlockResult && $UPDATEResult) {

            $response["error"] = FALSE;
            $response["message"] = "Blocked Succesfully...";
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
