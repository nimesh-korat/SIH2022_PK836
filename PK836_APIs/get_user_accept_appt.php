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

    $checkQuery = "SELECT login_table.F_NAME AS OP_F_NAME, login_table.L_NAME AS OP_L_NAME, login_table.PHONE_NO AS OP_PHONE_NO, op_active_status.OP_LAT, op_active_status.OP_LONG, ROUND(AVG(op_reviews.BEHAVIOUR),2) as 'BEHAVIOUR', ROUND(AVG(op_reviews.ACCURACY),2) as 'ACCURACY', appointment_details.* FROM login_table LEFT JOIN appointment_details ON login_table.USERID = appointment_details.OP_ID LEFT JOIN op_active_status ON appointment_details.OP_ID = op_active_status.OP_ID LEFT JOIN op_reviews ON appointment_details.OP_ID = op_reviews.OP_ID WHERE appointment_details.APNT_STATUS= 'ACCEPTED' AND appointment_details.USERID = '$USERID' GROUP BY op_reviews.OP_ID"; // change here.
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
                $details['OP_F_NAME'] = $val['OP_F_NAME'];
                $details['OP_L_NAME'] = $val['OP_L_NAME'];
                $details['OP_PHONE_NO'] = $val['OP_PHONE_NO'];
                $details['OP_LAT'] = $val['OP_LAT'];
                $details['OP_LONG'] = $val['OP_LONG'];
                $details['BEHAVIOUR'] = $val['BEHAVIOUR'];
                $details['ACCURACY'] = $val['ACCURACY'];
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
