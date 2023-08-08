<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header("Access-Control-Allow-Headers: *");
header('Access-Control-Allow-Credentials', 'true');
include('db.php');

$response = array();

$checkQuery = "SELECT op_resign.RESIGN_ID, op_resign.OP_ID, login_table.F_NAME, login_table.L_NAME, login_table.DP_FILE, op_resign.RESIGN_REASON ,  DATE(login_table.REG_DATE_TIME) AS REG_DATE_TIME, DATE(op_resign.RESIGN_ADD_DATE) AS RESIGN_ADD_DATE FROM op_resign LEFT JOIN login_table ON login_table.USERID=op_resign.OP_ID WHERE op_resign.RESIGN_STATUS='PENDING'"; // change here.

$result = mysqli_query($db_con, $checkQuery);
$numrow = mysqli_num_rows($result);

if ($result->num_rows == 0) {
    $response["error"] = TRUE;
    $response["message"] = "Sorry no data found.";
    echo json_encode($response);
    exit;
} else {
    $data = array();

    for ($i = 1; $i <= $numrow; $i++) {
        while ($val = mysqli_fetch_assoc($result)) {

            $details['RESIGN_ID'] = $val['RESIGN_ID'];
            $details['F_NAME'] = $val['F_NAME'];
            $details['L_NAME'] = $val['L_NAME'];
            $details['DP_FILE'] = $val['DP_FILE'];
            $details['OP_ID'] = $val['OP_ID'];
            $details['RESIGN_REASON'] = $val['RESIGN_REASON'];
            $details['REG_DATE_TIME'] = $val['REG_DATE_TIME'];
            $details['RESIGN_ADD_DATE'] = $val['RESIGN_ADD_DATE'];

            array_push($data, $details);
        }
    }
    $response["RESIGN_REQUESTS"] = $data; // change in response name.
    $response["error"] = FALSE;
    $response["message"] = "Successfully data Found.";
    echo json_encode($response);
    exit;
}
