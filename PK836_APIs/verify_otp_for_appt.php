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

$account_sid = '...'; //sid
$auth_token = '...'; //token
$client = new Client($account_sid, $auth_token);
$twilio_number = "+14434197394";

if (isset($_POST["OP_ID"]) && isset($_POST["APPT_ID"]) && isset($_POST["OTP"]) && isset($_POST["USERID"]) && isset($_POST["OP_NAME"])) {

    $OP_ID = $_POST["OP_ID"];
    $APPT_ID = $_POST["APPT_ID"];
    $OTP = $_POST["OTP"];
    $USERID = $_POST["USERID"];
    $OP_NAME = $_POST["OP_NAME"];

    $userQuery = "SELECT * FROM `otp_table` WHERE  `OP_ID` = '$OP_ID' AND `APPT_ID` = '$APPT_ID' AND `OTP` = '$OTP' AND `STATUS` = 'ACTIVE'";
    $result = mysqli_query($db_con, $userQuery);


    $checkApptQuery = "select `APNT_ID`, `U_PHONE_NO`, `USERID` from `appointment_details` where `APNT_ID` = '$APPT_ID'";
    $checkApptresult = mysqli_query($db_con, $checkApptQuery);
    $val = mysqli_fetch_assoc($checkApptresult);
    $U_PHONE = $val['U_PHONE_NO'];
    $USERID = $val['USERID'];

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Incorrect OTP!!!";
        echo json_encode($response);
        exit;
    } else {

        $updateStatusQuery = "UPDATE `otp_table` SET `STATUS` = 'INACTIVE' WHERE  `OP_ID` = '$OP_ID' AND `APPT_ID` = '$APPT_ID' AND `OTP` = '$OTP' AND `STATUS` = 'ACTIVE'";
        $updateStatusresult = mysqli_query($db_con, $updateStatusQuery);

        $acceptQuery = "UPDATE `appointment_details` SET  `APNT_STATUS`= 'COMPLETED', `APNT_CMPT_DT`= NOW() WHERE `APNT_ID` = '$APPT_ID' AND `OP_ID` = '$OP_ID'";
        $acceptResult = mysqli_query($db_con, $acceptQuery);

        if ($acceptResult) {

            $updateStatusOwnQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'COMPLETED' WHERE `OP_ID` = '$OP_ID' AND `APPT_ID` = '$APPT_ID' AND `APPT_STATUS` = 'ACCEPTED'";
            $updateStatusOwnresult = mysqli_query($db_con, $updateStatusOwnQuery);

            $updateStatusOtherQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'COMPLETED_BY_OTHER' WHERE `APPT_ID` = '$APPT_ID' AND `APPT_STATUS` = 'ACCEPTED_BY_OTHER'";

            $updateStatusOtherresult = mysqli_query($db_con, $updateStatusOtherQuery);

            $response["error"] = FALSE;
            $response["message"] = "Appointment Successfully Completed";
            echo json_encode($response);

            $msg = "Your Appointment has Successfully Completed by $OP_NAME. Please add Review/Rating of Operator in app.";


            //change here
            $confirm =  $client->messages->create(
                // Where to send a text message (your cell phone?)
                '+91' . $U_PHONE,
                array(
                    'from' => $twilio_number,
                    'body' => $msg
                )
            );

            // $link = "Your Appointment has Successfully Completed. Please add Review/Rating of Operator in app.";

            // $fields = array(
            //     "sender_id" => "FTWSMS",
            //     "message_text" => $link,
            //     "language" => "english",
            //     "route" => "v3",
            //     "numbers" => $U_PHONE,
            // );

            // $curl = curl_init();

            // curl_setopt_array($curl, array(
            //     CURLOPT_URL => "https://www.fast2sms.com/dev/bulkV2",
            //     CURLOPT_RETURNTRANSFER => true,
            //     CURLOPT_ENCODING => "",
            //     CURLOPT_MAXREDIRS => 10,
            //     CURLOPT_TIMEOUT => 30,
            //     CURLOPT_SSL_VERIFYHOST => 0,
            //     CURLOPT_SSL_VERIFYPEER => 0,
            //     CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            //     CURLOPT_CUSTOMREQUEST => "POST",
            //     CURLOPT_POSTFIELDS => json_encode($fields),
            //     CURLOPT_HTTPHEADER => array(
            //         "authorization: g2U5rNZ3SAWOkGQvTxYRj6EMnF41py9JflCzVDubPqi7IBho0c75HlyLgcE12TBh8eKJbFjP9mrziNUX",
            //         "accept: */*",
            //         "cache-control: no-cache",
            //         "content-type: application/json"
            //     ),
            // ));

            // $response = curl_exec($curl);
            // $err = curl_error($curl);

            // curl_close($curl);

            exit;
        }

        $response["error"] = FALSE;
        $response["message"] = "OTP Successfully Verified.";
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
