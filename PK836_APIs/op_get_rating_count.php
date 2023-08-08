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

if (isset($_POST["OP_ID"])) {


    $OP_ID = $_POST["OP_ID"];

    $behaviourQuery = "SELECT
    SUM(IF(BEHAVIOUR = '1', 1, 0)) AS BEHAVIOUR_1,
    SUM(IF(BEHAVIOUR = '2', 1, 0)) AS BEHAVIOUR_2,
    SUM(IF(BEHAVIOUR = '3', 1, 0)) AS BEHAVIOUR_3,
    SUM(IF(BEHAVIOUR = '4', 1, 0)) AS BEHAVIOUR_4,
    SUM(IF(BEHAVIOUR = '5', 1, 0)) AS BEHAVIOUR_5,
    COUNT(BEHAVIOUR) AS TOTAL_BEHAVIOUR FROM op_reviews WHERE OP_ID = '$OP_ID'";

    $behaviourResult = mysqli_query($db_con, $behaviourQuery);

    $accuracyQuery = "SELECT
    SUM(IF(ACCURACY = '1', 1, 0)) AS ACCURACY_1,
    SUM(IF(ACCURACY = '2', 1, 0)) AS ACCURACY_2,
    SUM(IF(ACCURACY = '3', 1, 0)) AS ACCURACY_3,
    SUM(IF(ACCURACY = '4', 1, 0)) AS ACCURACY_4,
    SUM(IF(ACCURACY = '5', 1, 0)) AS ACCURACY_5,
    COUNT(ACCURACY) AS TOTAL_ACCURACY FROM op_reviews WHERE OP_ID = '$OP_ID'";

    $accuracyResult = mysqli_query($db_con, $accuracyQuery);

    $commentsQuery = "SELECT op_reviews.COMMENTS, op_reviews.BEHAVIOUR, op_reviews.ACCURACY, op_reviews.APPT_ID, op_reviews.USERID, login_table.F_NAME, login_table.L_NAME FROM op_reviews RIGHT JOIN login_table ON login_table.USERID = op_reviews.USERID WHERE op_reviews.OP_ID = '$OP_ID'";

    $commentResult = mysqli_query($db_con, $commentsQuery);

    $numrow = mysqli_num_rows($commentResult);

    if ($behaviourResult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "No Data Found :(";
        echo json_encode($response);
        exit;
    } else {

        if ($commentResult->num_rows == 0) {
            $response["error"] = TRUE;
            $response["message"] = "Sorry no Comments found.";
            echo json_encode($response);
            exit;
            
        } else {
            $data = array();

            for ($i = 1; $i <= $numrow; $i++) {
                while ($val = mysqli_fetch_assoc($commentResult)) {

                    $details['F_NAME'] = $val['F_NAME'];
                    $details['L_NAME'] = $val['L_NAME'];
                    $details['COMMENTS'] = $val['COMMENTS'];
                    $details['BEHAVIOUR'] = $val['BEHAVIOUR'];
                    $details['ACCURACY'] = $val['ACCURACY'];
                    $details['APPT_ID'] = $val['APPT_ID'];
                    $details['USERID'] = $val['USERID'];

                    array_push($data, $details);
                }
            }
        }
        $behavior = mysqli_fetch_assoc($behaviourResult);
        $accuracy = mysqli_fetch_assoc($accuracyResult);
        $response["BEHAVIOUR"] = $behavior;
        $response["ACCURACY"] = $accuracy;
        $response["COMMENT"] = $data;
        $response["error"] = FALSE;
        $response["message"] = "Data Found SuccessFully :)";
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
