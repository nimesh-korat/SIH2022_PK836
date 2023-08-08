<?php
header('Content-Type: application/json');
include('db.php');

$response = array();

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["USERID"]) && isset($_POST["U_PINCODE"]) && isset($_POST["U_PHONE_NO"])) {

    $USERID = $_POST["USERID"];
    $U_PINCODE = $_POST["U_PINCODE"];
    $U_PHONE_NO = $_POST["U_PHONE_NO"];

    $check = "SELECT * FROM `area_without_op` WHERE `U_PINCODE`='$U_PINCODE' AND `U_PHONE_NO` = '$U_PHONE_NO'";
    $result = mysqli_query($db_con, $check);

    if ($result->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "Already Sent Request";
        echo json_encode($response);
        exit;
    } else {

        $requestOp = "INSERT INTO `area_without_op`(`USERID`, `U_PINCODE`,`U_PHONE_NO`) VALUES ('$USERID', '$U_PINCODE','$U_PHONE_NO')";
        $requestOpResult = mysqli_query($db_con, $requestOp);

        if ($requestOpResult) {
            $response["error"] = FALSE;
            $response["message"] = "Successfully Request Sent";
            echo json_encode($response);
            exit;
        } else {
            $response["error"] = TRUE;
            $response["message"] = "Something Went Wrong";
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
