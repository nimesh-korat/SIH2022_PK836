<?php
header('Content-Type: application/json');
include('db.php');

$response = array();

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["OP_ID"])) {

    $OP_ID = $_POST["OP_ID"];

    $response = array();

    $commentsQuery = "SELECT op_reviews.COMMENTS, op_reviews.APPT_ID, op_reviews.USERID, login_table.F_NAME, login_table.L_NAME FROM op_reviews RIGHT JOIN login_table ON login_table.USERID = op_reviews.USERID WHERE op_reviews.OP_ID = '$OP_ID'";

    $commentResult = mysqli_query($db_con, $commentsQuery);

    $numrow = mysqli_num_rows($commentResult);

    if ($commentResult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no Comments found.";
        echo json_encode($response);
        exit;
    } else {
        $data = array();

        for ($i = 1; $i <= $numrow; $i++) {
            while ($val = mysqli_fetch_assoc($commentResult)) {

                $details['COMMENTS'] = $val['COMMENTS'];
                $details['APPT_ID'] = $val['APPT_ID'];
                $details['USERID'] = $val['USERID'];
                $details['F_NAME'] = $val['F_NAME'];
                $details['L_NAME'] = $val['L_NAME'];



                array_push($data, $details);
            }
        }
        $response["COMMENTS"] = $data; // change in response name.
        $response["error"] = FALSE;
        $response["message"] = "Successfully Comments Found.";
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
