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
if (isset($_GET["STATE"])) {

    $STATE = $_GET["STATE"];
    $response1 = array();

    $monthlyQuery = "SELECT DATE(APNT_PEND_DT) as 'DATE', COUNT(*) as 'COUNT' FROM appointment_details WHERE U_STATE='$STATE' AND APNT_PEND_DT BETWEEN NOW() - INTERVAL 30 DAY AND NOW() GROUP BY DAY(APNT_PEND_DT)"; // change here.
    $monthlyresult = mysqli_query($db_con, $monthlyQuery);
    $monthlynumrow = mysqli_num_rows($monthlyresult);

    $getGenderCountQuery = "SELECT `GENDER`, COUNT(*) AS 'COUNT' FROM `login_table` WHERE STATE='$STATE' GROUP BY GENDER";
    $getGenderCountresult = mysqli_query($db_con, $getGenderCountQuery);
    $genderNumrow = mysqli_num_rows($getGenderCountresult);

    $getStateCountStatusQuery = "SELECT SUM(IF(`APNT_STATUS` = 'PENDING', 1, 0)) AS PENDING_APPT, SUM(IF(`APNT_STATUS` = 'ACTIVE', 1, 0)) AS ACTIVE_APPT, SUM(IF(`APNT_STATUS` = 'COMPLETED', 1, 0)) AS COMPLETED_APPT FROM appointment_details  WHERE `U_STATE` = '$STATE'";
    $getStateCountStatusresult = mysqli_query($db_con, $getStateCountStatusQuery);
    $numrowStatus = mysqli_num_rows($getStateCountStatusresult);

    $getOpStateCount = "SELECT COUNT(*) AS 'COUNT' FROM `login_table` WHERE `STATE` = '$STATE' AND `ROLE` = 'OPERATOR'";
    $getOpStateCountresult = mysqli_query($db_con, $getOpStateCount);
    $opStateNumrow = mysqli_num_rows($getOpStateCountresult);


    if ($monthlyQuery == null) {
        $response["error"] = TRUE;
        $response["message"] = "Can't getting Data!!!";
        echo json_encode($response);
        exit;
    } else {

        $monthCountdata = array();
        for ($i = 1; $i <= $monthlynumrow; $i++) {
            while ($val = mysqli_fetch_assoc($monthlyresult)) {

                $details1['DATE'] = $val['DATE'];
                $details1['COUNT'] = $val['COUNT'];

                array_push($monthCountdata, $details1);
            }
        }

        $genderdata = array();
        for ($i = 1; $i <= $genderNumrow; $i++) {
            while ($val = mysqli_fetch_assoc($getGenderCountresult)) {

                $details2['GENDER'] = $val['GENDER'];
                $details2['COUNT'] = $val['COUNT'];

                array_push($genderdata, $details2);
            }
        }

        $stateStatusS = array();
        for ($i = 1; $i <= $opStateNumrow; $i++) {
            while ($val = mysqli_fetch_assoc($getStateCountStatusresult)) {

                $details3['PENDING_APPT'] = $val['PENDING_APPT'];
                $details3['ACTIVE_APPT'] = $val['ACTIVE_APPT'];
                $details3['COMPLETED_APPT'] = $val['COMPLETED_APPT'];
                array_push($stateStatusS, $details3);
            }
        }

        $statewiseOpCount = array();
        for ($i = 1; $i <= $opStateNumrow; $i++) {
            while ($val = mysqli_fetch_assoc($getOpStateCountresult)) {
                $details4['COUNT'] = $val['COUNT'];
                array_push($statewiseOpCount, $details4);
            }
        }

        $response1["STATUS_COUNT"] = $stateStatusS;
        $response1["MONTHLY_DATA"] = $monthCountdata;
        $response1["GENDER_DATA"] = $genderdata;
        $response1["TOTAL_OP"] = $statewiseOpCount;
        $response1["error"] = FALSE;
        $response1["message"] = "Successfully user data Found.";
        echo json_encode($response1);
        exit;
    }
} else {
    //Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid Parameters";
    echo json_encode($response);
    exit;
}
