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

if (isset($_POST["APNT_ID"]) && isset($_POST["OP_ID"]) && isset($_POST["OP_NAME"])) { //OP_NAME get by sharedPref from app

    $APNT_ID = $_POST["APNT_ID"];
    $OP_ID = $_POST["OP_ID"];
    $OP_NAME = $_POST["OP_NAME"];


    $checkStatusQuery = "select `APNT_STATUS`, `USERID`, `U_PHONE_NO` from `appointment_details` where `APNT_STATUS` = 'COMPLETED'  AND `APNT_ID` = '$APNT_ID'";
    $result = mysqli_query($db_con, $checkStatusQuery);

    $checkApptQuery = "select `APNT_ID`, `U_PHONE_NO`, `USERID` from `appointment_details` where `APNT_ID` = '$APNT_ID'";
    $checkApptresult = mysqli_query($db_con, $checkApptQuery);
    $val = mysqli_fetch_assoc($checkApptresult);
    $U_PHONE = $val['U_PHONE_NO'];
    $USERID = $val['USERID'];

    $checkOpStatusQuery = "select `APNT_ID` from `appointment_details` where `APNT_STATUS` = 'COMPLETED' AND `OP_ID` = '$OP_ID' AND `APNT_ID` = '$APNT_ID'";
    $resultOp = mysqli_query($db_con, $checkOpStatusQuery);


    if ($checkApptresult->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Appointment Does't Exist!!!";
        echo json_encode($response);
        exit;
    } elseif ($resultOp->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "You have already Completed Appointment.";
        echo json_encode($response);
        exit;
    } elseif ($result->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "Appointment Already Completed.";
        echo json_encode($response);
        exit;
    } else {
        $acceptQuery = "UPDATE `appointment_details` SET  `APNT_STATUS`= 'COMPLETED', `APNT_CMPT_DT`= NOW() WHERE `APNT_ID` = '$APNT_ID' AND `OP_ID` = '$OP_ID'";
        $acceptResult = mysqli_query($db_con, $acceptQuery);

        if ($acceptResult) {

            $updateStatusOwnQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'COMPLETED' WHERE `OP_ID` = '$OP_ID' AND `APPT_ID` = '$APNT_ID' AND `APPT_STATUS` = 'ACCEPTED'";
            $updateStatusOwnresult = mysqli_query($db_con, $updateStatusOwnQuery);

            $updateStatusOtherQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'COMPLETED_BY_OTHER' WHERE `APPT_ID` = '$APNT_ID' AND `APPT_STATUS` = 'ACCEPTED_BY_OTHER'";

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

            $addOtpToServer = "INSERT INTO `otp_table` (`USER_ID`,`OP_ID`,`APPT_ID`, `OTP`, `STATUS`) 
			VALUES ('$USERID','$OP_ID','$APNT_ID','$otp','ACTIVE')";
            $addOtpToServerresult = mysqli_query($db_con, $addOtpToServer);

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
