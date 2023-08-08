<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");
include('db.php');

$response = array();

$checkQuery = "SELECT * FROM `login_table` WHERE ROLE = 'OPERATOR'"; // change here.
$result = mysqli_query($db_con, $checkQuery);

$numrow = mysqli_num_rows($result);

if ($result->num_rows == 0) {
    $response["error"] = TRUE;
    $response["message"] = "Sorry no appointment found.";
    echo json_encode($response);
    exit;
} else {

    $data = array();

    for ($i = 1; $i <= $numrow; $i++) {
        while ($val = mysqli_fetch_assoc($result)) {

            $details['USERID'] = $val['USERID'];
            $details['F_NAME'] = $val['F_NAME'];
            $details['L_NAME'] = $val['L_NAME'];
            $details['EMAIL_ID'] = $val['EMAIL_ID'];
            $details['PASSWORD'] = $val['PASSWORD'];
            $details['PHONE_NO'] = $val['PHONE_NO'];
            $details['ADDRESS'] = $val['ADDRESS'];
            $details['CITY'] = $val['CITY'];
            $details['STATE'] = $val['STATE'];
            $details['PINCODE'] = $val['PINCODE'];
            $details['DOB'] = $val['DOB'];
            $details['GENDER'] = $val['GENDER'];
            $details['STATUS'] = $val['STATUS'];
            $details['REG_DATE_TIME'] = $val['REG_DATE_TIME'];

            array_push($data, $details);
        }
    }
    $response["OP_DATA"] = $data; // change in response name.
    $response["error"] = FALSE;
    $response["message"] = "Successfully operator data Found.";
    echo json_encode($response);
    exit;
}
