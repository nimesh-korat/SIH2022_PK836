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

if (isset($_POST["CITY"]) && isset($_POST["LAT"]) && isset($_POST["LONG"])) {


    $CITY = $_POST["CITY"];
    $LAT = $_POST["LAT"];
    $LONG = $_POST["LONG"];

    $get_CityOpQuery = "select login_table.USERID as OP_ID,login_table.CITY as OP_CITY, op_active_status.OP_LAT as OP_LAT, op_active_status.OP_LONG as OP_LONG FROM login_table LEFT join op_active_status on op_active_status.OP_ID = login_table.USERID LEFT JOIN op_attendence ON op_attendence.OP_ID = login_table.USERID where login_table.CITY = '$CITY' AND login_table.ROLE = 'OPERATOR' AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE()";
    
    $result = mysqli_query($db_con, $get_CityOpQuery);
    $numrow = mysqli_num_rows($result);

    function distance($LAT, $LONG, $olat, $olong)
    {
        $pi80 = M_PI / 180;
        $LAT *= $pi80;
        $LONG *= $pi80;
        $olat *= $pi80;
        $olong *= $pi80;
        $r = 6372.797; // mean radius of Earth in km 
        $dlat = $olat - $LAT;
        $dlon = $olong - $LONG;
        $a = sin($dlat / 2) * sin($dlat / 2) + cos($LAT) * cos($olat) * sin($dlon / 2) * sin($dlon / 2);
        $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
        $km = $r * $c;
        //echo ' '.$km;
        $km = round($km);
        return $km;
    }


    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "No nearby Operator found.";
        echo json_encode($response);
        exit;
    } else {
        for ($i = 1; $i <= $numrow; $i++) {
            while ($val = mysqli_fetch_assoc($result)) {
                $olat = $val['OP_LAT'];
                $olong = $val['OP_LONG'];


                $getdistance = distance($LAT, $LONG, $olat, $olong);
                //  $response["km"] = $getdistance;
                //  $response["lat"] = $olat;
                //  $response["long"] = $olong;

                if ($getdistance > 20) {
                    $response["error"] = TRUE;
                    $response["message"] = "There is no nearby operator found!!!";
                    echo json_encode($response);
                    exit;
                } else {
                    $response["error"] = FALSE;
                    $response["message"] = "Nearby Operator available";
                    echo json_encode($response);
                    exit;
                }
            }
        }
    }
} else {

    // Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid parameters";
    echo json_encode($response);
    exit;
}
