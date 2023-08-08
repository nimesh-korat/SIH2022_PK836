<?php
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");
include('db.php');

$response = array();

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_GET["OP_ID"])) {

    $OP_ID = $_GET["OP_ID"];

    $response = array();

    $checkQuery = "SELECT DATE(ATTEND_DATE) AS ATTEND_DATE, TIME(`ATTEND_DATE`) AS ATTEND_TIME FROM op_attendence WHERE `OP_ID` = '19'AND OP_ATTENDENCE = 'PRESENT'"; // change here.
    $result = mysqli_query($db_con, $checkQuery);

    $numrow = mysqli_num_rows($result);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no attendence found.";
        echo json_encode($response);
        exit;
    } else {
        $data = array();

        for ($i = 1; $i <= $numrow; $i++) {
            while ($val = mysqli_fetch_assoc($result)) {

                $details['ATTEND_DATE'] = $val['ATTEND_DATE'];
                $details['ATTEND_TIME'] = $val['ATTEND_TIME'];

                array_push($data, $details);
            }
        }
        $response["Attendence"] = $data; // change in response name.
        $response["error"] = FALSE;
        $response["message"] = "Successfully Attendence Found.";
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
