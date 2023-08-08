<?php

include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

$response = array();
header('Content-Type: application/json');

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_GET["USER_ID"])) {


    $USER_ID = $_GET["USER_ID"];

    $opQuery = "SELECT login_table.*, ROUND(AVG(op_reviews.BEHAVIOUR),2) as 'BEHAVIOUR', ROUND(AVG(op_reviews.ACCURACY),2) as 'ACCURACY' FROM login_table LEFT JOIN op_reviews ON login_table.USERID = op_reviews.OP_ID WHERE login_table.ROLE = 'OPERATOR' AND login_table.USERID = '$USER_ID' GROUP BY op_reviews.OP_ID";
    $result = mysqli_query($db_con, $opQuery);
    $numrow = mysqli_num_rows($result);

    $opCommentQuery = "SELECT op_reviews.COMMENTS, op_reviews.BEHAVIOUR, op_reviews.ACCURACY, op_reviews.APPT_ID, op_reviews.USERID, login_table.F_NAME, login_table.L_NAME FROM op_reviews RIGHT JOIN login_table ON login_table.USERID = op_reviews.USERID WHERE op_reviews.OP_ID = '$USER_ID'";
    $opCommentResult = mysqli_query($db_con, $opCommentQuery);
    $commentnumrow = mysqli_num_rows($opCommentResult);

    $opAttedQuery = "SELECT `ATTEND_DATE` , `OP_ATTENDENCE` FROM `op_attendence` WHERE `OP_ID` = '$USER_ID'";
    $opAttedResult = mysqli_query($db_con, $opAttedQuery);
    $opAttednumrow = mysqli_num_rows($opAttedResult);

    $opApptCountQuery = "SELECT SUM(IF(APNT_STATUS = 'PENDING', 1, 0)) AS PENDING_APPT_COUNT, SUM(IF(APNT_STATUS = 'ACCEPTED', 1, 0)) AS ACCEPTED_APPT_COUNT, SUM(IF(APNT_STATUS = 'COMPLETED', 1, 0)) AS COMPLETED_APPT_COUNT FROM appointment_details WHERE OP_ID = '$USER_ID'";
    $opApptCountResult = mysqli_query($db_con, $opApptCountQuery);
    $opApptCountnumrow = mysqli_num_rows($opApptCountResult);

    $opRatingCountQuery = "SELECT
    SUM(IF(`BEHAVIOUR` = '1', 1, 0)) AS BEHAVIOUR_1,
    SUM(IF(`BEHAVIOUR` = '2', 1, 0)) AS BEHAVIOUR_2,
    SUM(IF(`BEHAVIOUR` = '3', 1, 0)) AS BEHAVIOUR_3,
    SUM(IF(`BEHAVIOUR` = '4', 1, 0)) AS BEHAVIOUR_4,
    SUM(IF(`BEHAVIOUR` = '5', 1, 0)) AS BEHAVIOUR_5,
    SUM(IF(`ACCURACY` = '1', 1, 0)) AS ACCURACY_1,
    SUM(IF(`ACCURACY` = '2', 1, 0)) AS ACCURACY_2,
    SUM(IF(`ACCURACY` = '3', 1, 0)) AS ACCURACY_3,
    SUM(IF(`ACCURACY` = '4', 1, 0)) AS ACCURACY_4,
    SUM(IF(`ACCURACY` = '5', 1, 0)) AS ACCURACY_5,
    COUNT(`ACCURACY`) AS TOTAL_ACCURACY,
    COUNT(`BEHAVIOUR`) AS TOTAL_BEHAVIOUR FROM `op_reviews` WHERE `OP_ID` = '$USER_ID'";
    $opRatingCountResult = mysqli_query($db_con, $opRatingCountQuery);
    $opRatingCountnumrow = mysqli_num_rows($opRatingCountResult);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no Operator found.";
        echo json_encode($response);
        exit;
    } else {

        $data = array();
        $data1 = array();
        $data2 = array();
        $data3 = array();
        $data4 = array();

        for ($i = 1; $i <= $numrow; $i++) {
            while ($val = mysqli_fetch_assoc($result)) {
                $details['USERID'] = $val['USERID'];
                $details['F_NAME'] = $val['F_NAME'];
                $details['L_NAME'] = $val['L_NAME'];
                $details['EMAIL_ID'] = $val['EMAIL_ID'];
                $details['PHONE_NO'] = $val['PHONE_NO'];
                $details['ADDRESS'] = $val['ADDRESS'];
                $details['CITY'] = $val['CITY'];
                $details['STATE'] = $val['STATE'];
                $details['DP_FILE'] = $val['DP_FILE'];
                $details['PINCODE'] = $val['PINCODE'];
                $details['DOB'] = $val['DOB'];
                $details['GENDER'] = $val['GENDER'];
                $details['STATUS'] = $val['STATUS'];
                $details['REG_DATE_TIME'] = $val['REG_DATE_TIME'];
                $details['BEHAVIOUR'] = $val['BEHAVIOUR'];
                $details['ACCURACY'] = $val['ACCURACY'];

                array_push($data, $details);
            }
        }

        for ($i = 1; $i <= $commentnumrow; $i++) {
            while ($val = mysqli_fetch_assoc($opCommentResult)) {
                $detail['F_NAME'] = $val['F_NAME'];
                $detail['L_NAME'] = $val['L_NAME'];
                $detail['COMMENTS'] = $val['COMMENTS'];
                $detail['BEHAVIOUR'] = $val['BEHAVIOUR'];
                $detail['ACCURACY'] = $val['ACCURACY'];
                $detail['APPT_ID'] = $val['APPT_ID'];
                $detail['USERID'] = $val['USERID'];

                array_push($data1, $detail);
            }
        }

        for ($i = 1; $i <= $opAttednumrow; $i++) {
            while ($val = mysqli_fetch_assoc($opAttedResult)) {
                $detailss['OP_ATTENDENCE'] = $val['OP_ATTENDENCE'];
                $detailss['ATTEND_DATE'] = $val['ATTEND_DATE'];

                array_push($data2, $detailss);
            }
        }

        for ($i = 1; $i <= $opApptCountnumrow; $i++) {
            while ($val = mysqli_fetch_assoc($opApptCountResult)) {
                $detailsss['PENDING_APPT_COUNT'] = $val['PENDING_APPT_COUNT'];
                $detailsss['ACCEPTED_APPT_COUNT'] = $val['ACCEPTED_APPT_COUNT'];
                $detailsss['COMPLETED_APPT_COUNT'] = $val['COMPLETED_APPT_COUNT'];
                array_push($data3, $detailsss);
            }
        }

        for ($i = 1; $i <= $opRatingCountnumrow; $i++) {
            while ($val = mysqli_fetch_assoc($opRatingCountResult)) {
                $detailssss['BEHAVIOUR_1'] = $val['BEHAVIOUR_1'];
                $detailssss['BEHAVIOUR_2'] = $val['BEHAVIOUR_2'];
                $detailssss['BEHAVIOUR_3'] = $val['BEHAVIOUR_3'];
                $detailssss['BEHAVIOUR_4'] = $val['BEHAVIOUR_4'];
                $detailssss['BEHAVIOUR_5'] = $val['BEHAVIOUR_5'];
                $detailssss['ACCURACY_1'] = $val['ACCURACY_1'];
                $detailssss['ACCURACY_2'] = $val['ACCURACY_2'];
                $detailssss['ACCURACY_3'] = $val['ACCURACY_3'];
                $detailssss['ACCURACY_4'] = $val['ACCURACY_4'];
                $detailssss['ACCURACY_5'] = $val['ACCURACY_5'];
                $detailssss['TOTAL_ACCURACY'] = $val['TOTAL_ACCURACY'];
                $detailssss['TOTAL_BEHAVIOUR'] = $val['TOTAL_BEHAVIOUR'];
                array_push($data4, $detailssss);
            }
        }

        $response["OP_DATA"] = $data; // change in response name.
        $response["REVIEWS"] = $data1; // change in response name.
        $response["ATTENDANCE"] = $data2; // change in response name.
        $response["APPT_COUNT"] = $data3; // change in response name.
        $response["OP_RATING"] = $data4; // change in response name.
        $response["error"] = FALSE;
        $response["message"] = "Successfully user data Found.";
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
