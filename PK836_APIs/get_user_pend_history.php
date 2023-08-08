<?php
header('Content-Type: application/json');
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

    $response = array();

    $checkQuery = "SELECT * FROM  `appointment_details`  WHERE `APNT_STATUS`= 'PENDING' AND `USERID` = '$USERID'"; // change here.
    $result = mysqli_query($db_con, $checkQuery);

    $numrow = mysqli_num_rows($result);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no Appointments found.";
        echo json_encode($response);
        exit;
    } else {
        $data = array();

        for ($i = 1; $i <= $numrow; $i++) {
            while ($val = mysqli_fetch_assoc($result)) {

                $details['APNT_ID'] = $val['APNT_ID'];
                $details['USERID'] = $val['USERID'];
                $details['OP_ID'] = $val['OP_ID'];
                $details['F_NAME'] = $val['F_NAME'];
                $details['L_NAME'] = $val['L_NAME'];
                $details['U_PHONE_NO'] = $val['U_PHONE_NO'];
                $details['U_ADDRESS'] = $val['U_ADDRESS'];
                $details['U_CITY'] = $val['U_CITY'];
                $details['U_STATE'] = $val['U_STATE'];
                $details['U_PINCODE'] = $val['U_PINCODE'];
                $details['U_LATITUDE'] = $val['U_LATITUDE'];
                $details['U_LONGITUDE'] = $val['U_LONGITUDE'];
                $details['APNT_DETAIL'] = $val['APNT_DETAIL'];
                $details['APNT_STATUS'] = $val['APNT_STATUS'];
                $details['APNT_PEND_DT'] = $val['APNT_PEND_DT'];
                $details['APNT_ACPT_DT'] = $val['APNT_ACPT_DT'];
                $details['APNT_CMPT_DT'] = $val['APNT_CMPT_DT'];



                array_push($data, $details);
            }
        }
        $response["Appointments"] = $data; // change in response name.
        $response["error"] = FALSE;
        $response["message"] = "Successfully Appointments data Found.";
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
?>