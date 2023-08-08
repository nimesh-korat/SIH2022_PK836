<?php
header('Content-Type: application/json');
include('db.php');

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

$response = array();


$checkQuery = "SELECT login_table.F_NAME, login_table.L_NAME, area_without_op.* FROM area_without_op LEFT JOIN login_table ON area_without_op.USERID = login_table.USERID;"; // change here.

$result = mysqli_query($db_con, $checkQuery);
$numrow = mysqli_num_rows($result);

if ($result->num_rows == 0) {
    $response["error"] = TRUE;
    $response["message"] = "Sorry no request found.";
    echo json_encode($response);
    exit;
} else {
    $data = array();

    for ($i = 1; $i <= $numrow; $i++) {
        while ($val = mysqli_fetch_assoc($result)) {

            $details['F_NAME'] = $val['F_NAME'];
            $details['L_NAME'] = $val['L_NAME'];
            $details['REQUEST_ID'] = $val['REQUEST_ID'];
            $details['USERID'] = $val['USERID'];
            $details['U_PINCODE'] = $val['U_PINCODE'];
            $details['U_PHONE_NO'] = $val['U_PHONE_NO'];

            array_push($data, $details);
        }
    }
    $response["NO_OP_AREA"] = $data; // change in response name.
    $response["error"] = FALSE;
    $response["message"] = "Successfully data Found.";
    echo json_encode($response);
    exit;
}
