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



require __DIR__ . '/vendor/autoload.php';

use Twilio\Rest\Client;

$account_sid = 'ACec8726f9321593194450941a47c40e19';
$auth_token = 'a412345cb274939c0c7b17195371a98d';
$client = new Client($account_sid, $auth_token);
$twilio_number = "+14434197394";

// Function to generate OTP
function generateNumericOTP($pass)
{

    // Take a generator string which consist of
    // all numeric digits
    $generator = "1357902468";

    // Iterate for n-times and pick a single character
    // from generator and append it to $result

    // Login for generating a random character from generator
    //     ---generate a random number
    //     ---take modulus of same with length of generator (say i)
    //     ---append the character at place (i) from generator to result

    $result = "";

    for ($i = 1; $i <= $pass; $i++) {
        $result .= substr($generator, (rand() % (strlen($generator))), 1);
    }

    // Return result
    return $result;
}

if (isset($_POST["USERID"]) && isset($_POST["F_NAME"]) && isset($_POST["L_NAME"]) && isset($_POST["U_PHONE_NO"])  && isset($_POST["U_ADDRESS"]) && isset($_POST["U_PINCODE"]) && isset($_POST["U_LATITUDE"]) && isset($_POST["U_LONGITUDE"]) && isset($_POST["APNT_DETAIL"]) && isset($_POST["APNT_STATUS"])) {

    $USERID = $_POST["USERID"];
    $F_NAME = $_POST["F_NAME"];
    $L_NAME = $_POST["L_NAME"];
    $U_PHONE_NO = $_POST["U_PHONE_NO"];
    $U_ADDRESS = $_POST["U_ADDRESS"];
    $U_CITY = $_POST["U_CITY"];
    $U_STATE = $_POST["U_STATE"];
    $U_PINCODE = $_POST["U_PINCODE"];
    $U_LATITUDE = $_POST["U_LATITUDE"];
    $U_LONGITUDE = $_POST["U_LONGITUDE"];
    $APNT_DETAIL = $_POST["APNT_DETAIL"];
    $APNT_STATUS = $_POST["APNT_STATUS"];

    $checkOnlyOneApntQuery = "select * from `appointment_details` where `USERID` = '$USERID' AND (`APNT_STATUS` = 'PENDING' OR `APNT_STATUS` = 'ACCEPTED')";
    $checkOnlyOneApntResult = mysqli_query($db_con, $checkOnlyOneApntQuery);

    if ($checkOnlyOneApntResult->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "You have already booked Appointment!!!";
        echo json_encode($response);
        exit;
    } else {
        $book_apntQuery = "INSERT INTO `appointment_details`(`USERID`,`F_NAME`,`L_NAME`, `U_PHONE_NO`, `U_ADDRESS`, `U_CITY`, `U_STATE`, `U_PINCODE`, `U_LATITUDE`, `U_LONGITUDE`, `APNT_DETAIL`, `APNT_STATUS`) 
           VALUES ('$USERID','$F_NAME','$L_NAME','$U_PHONE_NO','$U_ADDRESS','$U_CITY','$U_STATE','$U_PINCODE','$U_LATITUDE','$U_LONGITUDE','$APNT_DETAIL','$APNT_STATUS')";
        $book_apntResult = mysqli_query($db_con, $book_apntQuery);

        $get_apntLastId =  mysqli_insert_id($db_con);

        if ($book_apntResult) {
            $response["error"] = FALSE;
            $response["message"] = "Your Appointment Registered Succesfully";
            echo json_encode($response);

            $response = array();

            //without checking daily quota
            // $get_CityOpQuery = "select login_table.USERID as OP_ID, login_table.PHONE_NO, appointment_details.APNT_ID as APNT_ID, appointment_details.U_CITY as APNT_CITY, op_active_status.OP_LAT as OP_LAT, op_active_status.OP_LONG as OP_LONG FROM login_table RIGHT join appointment_details on appointment_details.U_CITY = login_table.CITY LEFT join op_active_status on op_active_status.OP_ID = login_table.USERID LEFT JOIN op_attendence ON op_attendence.OP_ID = login_table.USERID where login_table.ROLE = 'OPERATOR' AND appointment_details.APNT_ID = $get_apntLastId AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE()";

            $get_CityOpQuery = "select login_table.USERID as OP_ID, login_table.PHONE_NO, appointment_details.APNT_ID as APNT_ID, appointment_details.U_CITY as APNT_CITY, op_active_status.OP_LAT as OP_LAT, op_active_status.OP_LONG as OP_LONG FROM login_table RIGHT join appointment_details on appointment_details.U_CITY = login_table.CITY LEFT join op_active_status on op_active_status.OP_ID = login_table.USERID LEFT JOIN op_attendence ON op_attendence.OP_ID = login_table.USERID LEFT JOIN op_daily_quota ON op_daily_quota.OP_ID = login_table.USERID where login_table.ROLE = 'OPERATOR' AND appointment_details.APNT_ID = $get_apntLastId AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE() AND op_daily_quota.DAILY_QUOTA > 0";

            $get_CityOpresult = mysqli_query($db_con, $get_CityOpQuery);
            $numrow = mysqli_num_rows($get_CityOpresult);


            if ($get_CityOpresult->num_rows == 0) {
                $response["error"] = TRUE;
                $response["message"] = "Sorry no operator at city found.";
                echo json_encode($response);
                exit;
            } else {
                $data = array();
                $sameCityOp = array();

                $phoneOfsameCityOp = array();

                for ($i = 1; $i <= $numrow; $i++) {
                    while ($val = mysqli_fetch_assoc($get_CityOpresult)) {

                        $opid = $val['OP_ID'];
                        $apid = $val['APNT_ID'];
                        $phone = $val['PHONE_NO'];

                        array_push($sameCityOp, $opid);
                        array_push($phoneOfsameCityOp, $phone);
                    }
                }
            }

            $count1 = count($sameCityOp);

            if ($count1 > 0) {

                for ($i = 0; $i <= $count1 - 1; $i++) {
                    //echo $sameCityOp[$i];

                    $op5km = $sameCityOp[$i];
                    $phone5km = $phoneOfsameCityOp[$i];

                    echo $op5km;

                    $add_ToOpQuery = "INSERT INTO `op_fetch_appt`(`APPT_ID`, `OP_ID`, `APPT_STATUS`) VALUES ('$apid','$op5km', 'PENDING')";
                    $add_ToOpResult = mysqli_query($db_con, $add_ToOpQuery);

                    $response["message1"] = "Successfully Appointment added";

                    $msg = "Hey! Check Out... You Got An Appointment...";


                    //change here
                    $confirm =  $client->messages->create(
                        // Where to send a text message (your cell phone?)
                        '+91' . $phone5km,
                        array(
                            'from' => $twilio_number,
                            'body' => $msg
                        )
                    );
                }

                // change in response name.
                $response["error"] = FALSE;
                $response["message"] = "Successfully user data Found.";
                echo json_encode($response);
                exit;
            }
            exit;
        } else {
            $response["error"] = TRUE;
            $response["message"] = "Something Went Wrong";
            echo json_encode($response);
            exit;
        }
    }
} else {
    //Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid Parameters";
    echo json_encode($response);
    exit;
}
