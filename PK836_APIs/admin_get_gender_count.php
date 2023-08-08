<?php

include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

$getGenderCountQuery = "SELECT `GENDER`, COUNT(*) AS 'COUNT' FROM `login_table` GROUP BY GENDER";

$getGenderCountresult = mysqli_query($db_con, $getGenderCountQuery);
$numrow = mysqli_num_rows($getGenderCountresult);

if ($getGenderCountresult == null) {
    $response["error"] = TRUE;
    $response["message"] = "Can't getting Data!!!";
    echo json_encode($response);
    exit;
} else {
    $data = array();

    for ($i = 1; $i <= $numrow; $i++) {
        while ($val = mysqli_fetch_assoc($getGenderCountresult)) {

            $details['GENDER'] = $val['GENDER'];
            $details['COUNT'] = $val['COUNT'];
 
            array_push($data, $details);
        }
    }
    $response["GENDER_DATA"] = $data;
    $response["error"] = FALSE;
    $response["message"] = "Data Got Succesfully";
    echo json_encode($response);
    exit;
}
