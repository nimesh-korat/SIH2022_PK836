<?php
header('Content-Type: application/json');
include('db.php');

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

//$response = array();

if (isset($_POST["PHONE_NO"])) {

    $PHONE_NO = $_POST["PHONE_NO"];

    $checkQuery = "SELECT * FROM `login_table` WHERE `PHONE_NO` = '$PHONE_NO'"; // change here.
    $result = mysqli_query($db_con, $checkQuery);
    $numrow = mysqli_num_rows($result);

    if ($result->num_rows == 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry no User linked With $PHONE_NO Found.";
        echo json_encode($response);
        exit;
    } else {

        $pass = 6;
        $otp = (generateNumericOTP($pass));

        $msg = "Use $otp to reset your password";


        //change here
        $confirm =  $client->messages->create(
            // Where to send a text message (your cell phone?)
            $PHONE_NO,
            array(
                'from' => $twilio_number,
                'body' => $msg
            )
        );

        // $fields = array(
        //     "sender_id" => "FTWSMS",
        //     "message_text" => $link,
        //     "language" => "english",
        //     "route" => "v3",
        //     "numbers" => $PHONE_NO,
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

        if ($confirm) {
            $response1["error"] = TRUE;
            $response1["message"] = "Not able to send message.";
            echo json_encode($response1);
            exit;
        } else {

            $addOtpToServer = "INSERT INTO `otp_table`(`PHONE_NO`, `OTP`, `STATUS`) VALUES ('$PHONE_NO', '$otp', 'ACTIVE')";
            $signupResult = mysqli_query($db_con, $addOtpToServer);
            if ($signupResult) {
                $response1["error"] = FALSE;
                $response1["message"] = "OTP ADDED SUCCESSFULLY.";
                echo json_encode($response1);
            }

            $response1["error"] = FALSE;
            $response1["message"] = "Message Sent.";
            echo json_encode($response1);
            exit;
        }
    }
} else {

    // Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid parameters";
    echo json_encode($response);
    exit;
}
