<?php

include('db.php');
header('Content-Type: application/json');

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}
if (isset($_POST["OP_ID"])) {

    $OP_ID = $_POST["OP_ID"];

    $getCompCountQuery = "SELECT SUM(IF(APPT_STATUS = 'PENDING', 1, 0)) AS PENDING_COUNT, SUM(IF(APPT_STATUS = 'ACCEPTED', 1, 0)) AS ACCEPTED_COUNT, SUM(IF(APPT_STATUS = 'COMPLETED', 1, 0)) AS COMPLETED_COUNT FROM op_fetch_appt WHERE OP_ID = '$OP_ID'";
    $getCompCountresult = mysqli_query($db_con, $getCompCountQuery);
    $numrow = mysqli_num_rows($getCompCountresult);

    if ($getCompCountresult == null) {
        $response["error"] = TRUE;
        $response["message"] = "Can't getting Data!!!";
        echo json_encode($response);
        exit;
    } else {

        // $data = array();

        // for ($i = 1; $i <= $numrow; $i++) {
        //     while ($val = mysqli_fetch_assoc($getCompCountresult)) {

        //         $details['PENDING_COUNT'] = $val['PENDING_COUNT'];
        //         $details['ACCEPTED_COUNT'] = $val['ACCEPTED_COUNT'];
        //         $details['COMPLETED_COUNT'] = $val['COMPLETED_COUNT'];

        //         array_push($data, $details);
        //     }
        // }

        $user = mysqli_fetch_assoc($getCompCountresult);
        $response["error"] = FALSE;
        $response["message"] = "Data Found Successfully";
        $response["TOTAL_OP_COUNT"] = $user;
        echo json_encode($response);
        exit;
    }
} else {
    //Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid Parameters";
    echo json_encode($response);
    exit;
}
