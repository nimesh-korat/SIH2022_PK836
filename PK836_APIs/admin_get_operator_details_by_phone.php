<?php

include('db.php');

$response = array();
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

if (isset($_POST["PHONE_NO"])) {


    $PHONE_NO = $_POST["PHONE_NO"];

    $opQuery = "SELECT login_table.*, ROUND(AVG(op_reviews.BEHAVIOUR),2) as 'BEHAVIOUR', ROUND(AVG(op_reviews.ACCURACY),2) as 'ACCURACY' FROM login_table RIGHT JOIN op_reviews ON login_table.USERID = op_reviews.OP_ID WHERE login_table.ROLE = 'OPERATOR' AND login_table.PHONE_NO = '$PHONE_NO' GROUP BY op_reviews.OP_ID";


    $result = mysqli_query($db_con, $opQuery);

    $numrow = mysqli_num_rows($result);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no Operator found.";
        echo json_encode($response);
        exit;
    } else {
        $data = array();

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
                $details['PINCODE'] = $val['PINCODE'];
                $details['DOB'] = $val['DOB'];
                $details['GENDER'] = $val['GENDER'];
                $details['REG_DATE_TIME'] = $val['REG_DATE_TIME'];
                $details['BEHAVIOUR'] = $val['BEHAVIOUR'];
                $details['ACCURACY'] = $val['ACCURACY'];

                array_push($data, $details);
            }
        }
        $response["OP_DATA"] = $data; // change in response name.
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
