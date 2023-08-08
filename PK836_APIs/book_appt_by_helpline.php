<?php
include('db.php');

$response = array();
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header("Access-Control-Allow-Headers: *");
header('Access-Control-Allow-Credentials', 'true');

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

if (isset($_POST["F_NAME"]) && isset($_POST["L_NAME"]) && isset($_POST["U_PHONE_NO"])  && isset($_POST["U_ADDRESS"]) && isset($_POST["U_PINCODE"]) && isset($_POST["APNT_DETAIL"])) {

    $F_NAME = $_POST["F_NAME"];
    $L_NAME = $_POST["L_NAME"];
    $U_PHONE_NO = $_POST["U_PHONE_NO"];
    $U_ADDRESS = $_POST["U_ADDRESS"];
    $U_CITY = $_POST["U_CITY"];
    $U_STATE = $_POST["U_STATE"];
    $U_PINCODE = $_POST["U_PINCODE"];
    $APNT_DETAIL = $_POST["APNT_DETAIL"];



    $book_apntQuery = "INSERT INTO `appointment_details`(`USERID`,`F_NAME`,`L_NAME`, `U_PHONE_NO`, `U_ADDRESS`, `U_CITY`, `U_STATE`, `U_PINCODE`, `APNT_DETAIL`, `APNT_STATUS`) 
           VALUES ('2','$F_NAME','$L_NAME','$U_PHONE_NO','$U_ADDRESS','$U_CITY','$U_STATE','$U_PINCODE','$APNT_DETAIL','PENDING')";

    $book_apntResult = mysqli_query($db_con, $book_apntQuery);

    $get_apntLastId =  mysqli_insert_id($db_con);

    if ($book_apntResult) {
        $response["error"] = FALSE;
        $response["message"] = "Your Appointment Registered Succesfully";
        echo json_encode($response);

        $response = array();

        // $get_CityOpQuery = "select login_table.USERID as OP_ID, appointment_details.APNT_ID as APNT_ID, appointment_details.U_CITY as APNT_CITY, op_attendence.OP_LAT as OP_LAT, op_attendence.OP_LONG as OP_LONG FROM login_table RIGHT join appointment_details on appointment_details.U_CITY = login_table.CITY LEFT join op_attendence on op_attendence.OP_ID = login_table.USERID where login_table.ROLE = 'OPERATOR' AND appointment_details.APNT_ID = $get_apntLastId AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE()";


        //without checking daily quota
        // $get_CityOpQuery = "select login_table.USERID as OP_ID, login_table.PHONE_NO, appointment_details.APNT_ID as APNT_ID, appointment_details.U_CITY as APNT_CITY, op_active_status.OP_LAT as OP_LAT, op_active_status.OP_LONG as OP_LONG FROM login_table RIGHT join appointment_details on appointment_details.U_CITY = login_table.CITY LEFT join op_active_status on op_active_status.OP_ID = login_table.USERID LEFT JOIN op_attendence ON op_attendence.OP_ID = login_table.USERID where login_table.ROLE = 'OPERATOR' AND appointment_details.APNT_ID = $get_apntLastId AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE()";

        $get_CityOpQuery = "select login_table.USERID as OP_ID, login_table.PHONE_NO, appointment_details.APNT_ID as APNT_ID, appointment_details.U_CITY as APNT_CITY, appointment_details.U_PINCODE as U_PINCODE, op_active_status.OP_LAT as OP_LAT, op_active_status.OP_LONG as OP_LONG FROM login_table RIGHT join appointment_details on appointment_details.U_CITY = login_table.CITY LEFT join op_active_status on op_active_status.OP_ID = login_table.USERID LEFT JOIN op_attendence ON op_attendence.OP_ID = login_table.USERID LEFT JOIN op_daily_quota ON op_daily_quota.OP_ID = login_table.USERID where login_table.ROLE = 'OPERATOR' AND appointment_details.APNT_ID = $get_apntLastId AND op_attendence.OP_ATTENDENCE = 'PRESENT' AND DATE(op_attendence.ATTEND_DATE) = CURDATE() AND op_daily_quota.DAILY_QUOTA > 0";
        $get_CityOpresult = mysqli_query($db_con, $get_CityOpQuery);
        $numrow = mysqli_num_rows($get_CityOpresult);

        $urlForPincode = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyAwoYRkSGxYH9XRYFQ7ynp37hKrJLbq1-c&components=postal_code:$U_PINCODE";
        $detailspin = file_get_contents($urlForPincode);
        $urlForPincode = json_decode($detailspin, true);


        $lat = $urlForPincode['results'][0]['geometry']['location']['lat'];
        $lng = $urlForPincode['results'][0]['geometry']['location']['lng'];


        echo $lat;
        echo $lng;
        function distance($lat, $lng, $olat, $olong)
        {
            $pi80 = M_PI / 180;
            $lat *= $pi80;
            $lng *= $pi80;
            $olat *= $pi80;
            $olong *= $pi80;
            $r = 6372.797; // mean radius of Earth in km 
            $dlat = $olat - $lat;
            $dlon = $olong - $lng;
            $a = sin($dlat / 2) * sin($dlat / 2) + cos($lat) * cos($olat) * sin($dlon / 2) * sin($dlon / 2);
            $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
            $km = $r * $c;
            //echo ' '.$km;
            $km = round($km);
            return $km;
        }

        if ($get_CityOpresult->num_rows == 0) {
            $response["error"] = TRUE;
            $response["message"] = "Sorry no operator at city found.";
            echo json_encode($response);
            exit;
        } else {
            $data = array();
            $dataof5km = array();
            $dataof10km = array();
            $dataof15km = array();
            $dataof20km = array();

            $phoneOf5km = array();
            $phoneOf10km = array();
            $phoneOf15km = array();
            $phoneOf20km = array();

            for ($i = 1; $i <= $numrow; $i++) {
                while ($val = mysqli_fetch_assoc($get_CityOpresult)) {

                    $olat = $val['OP_LAT'];
                    $olong = $val['OP_LONG'];

                    $getdistance = distance($lat, $lng, $olat, $olong);

                    if ($getdistance >= 0 && $getdistance <= 5) {

                        $opid = $val['OP_ID'];
                        $apid = $val['APNT_ID'];

                        $phone = $val['PHONE_NO'];

                        array_push($dataof5km, $opid);
                        array_push($phoneOf5km, $phone);
                    } else if ($getdistance > 5 && $getdistance <= 10) {
                        $opid = $val['OP_ID'];
                        $apid = $val['APNT_ID'];

                        $phone = $val['PHONE_NO'];

                        array_push($dataof10km, $opid);
                        array_push($phoneOf10km, $phone);
                    } else if ($getdistance > 10 && $getdistance <= 15) {
                        $opid = $val['OP_ID'];
                        $apid = $val['APNT_ID'];

                        $phone = $val['PHONE_NO'];

                        array_push($dataof15km, $opid);
                        array_push($phoneOf15km, $phone);
                    } else if ($getdistance > 15 && $getdistance <= 20) {
                        $opid = $val['OP_ID'];
                        $apid = $val['APNT_ID'];

                        $phone = $val['PHONE_NO'];

                        array_push($dataof20km, $opid);
                        array_push($phoneOf20km, $phone);
                    } else {
                        $response["error"] = FALSE;
                        $response["message"] = "Operator within 20 Kms Not Found. :(";
                        echo json_encode($response);
                    }
                }
            }


            // if 
            $count1 = count($dataof5km);
            $count2 = count($dataof10km);
            $count3 = count($dataof15km);
            $count4 = count($dataof20km);


            if ($count1 > 0) {

                for ($i = 0; $i <= $count1 - 1; $i++) {
                    //echo $dataof5km[$i];

                    $op5km = $dataof5km[$i];
                    $phone5km = $phoneOf5km[$i];

                    echo $op5km;

                    $add_ToOpQuery = "INSERT INTO `op_fetch_appt`(`APPT_ID`, `OP_ID`, `APPT_STATUS`) VALUES ('$apid','$op5km', 'PENDING')";

                    $add_ToOpResult = mysqli_query($db_con, $add_ToOpQuery);

                    $response["message1"] = "Successfully APPT aadded to less than 5.";

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
            } else if ($count2 > 0) {

                for ($i = 0; $i <= $count2 - 1; $i++) {
                    //echo $dataof5km[$i];

                    $op10km = $dataof10km[$i];
                    $phone10km = $phoneOf10km[$i];

                    echo $op10km;
                    $add_ToOpQuery = "INSERT INTO `op_fetch_appt`(`APPT_ID`, `OP_ID`, `APPT_STATUS`) VALUES ('$apid','$op10km', 'PENDING')";

                    $add_ToOpResult = mysqli_query($db_con, $add_ToOpQuery);

                    $response["message2"] = "Successfully APPT aadded to less than 10.";

                    $msg = "Hey! Check Out... You Got An Appointment...";


                    //change here
                    $confirm =  $client->messages->create(
                        // Where to send a text message (your cell phone?)
                        '+91' . $phone10km,
                        array(
                            'from' => $twilio_number,
                            'body' => $msg
                        )
                    );

                    
                }
            } else if ($count3 > 0) {

                for ($i = 0; $i <= $count3 - 1; $i++) {
                    //echo $dataof5km[$i];

                    $op15km = $dataof15km[$i];
                    $phone15km = $phoneOf15km[$i];

                    echo $op15km;
                    $add_ToOpQuery = "INSERT INTO `op_fetch_appt`(`APPT_ID`, `OP_ID`, `APPT_STATUS`) VALUES ('$apid','$op15km', 'PENDING')";

                    $add_ToOpResult = mysqli_query($db_con, $add_ToOpQuery);


                    $response["message3"] = "Successfully APPT aadded to less than 15.";


                    $msg = "Hey! Check Out... You Got An Appointment...";


                    //change here
                    $confirm =  $client->messages->create(
                        // Where to send a text message (your cell phone?)
                        '+91' . $phone15km,
                        array(
                            'from' => $twilio_number,
                            'body' => $msg
                        )
                    );

                    
                }
            } else if ($count4 > 0) {

                for ($i = 0; $i <= $count4 - 1; $i++) {
                    //echo $dataof5km[$i];

                    $op20km = $dataof20km[$i];
                    $phone20km = $phoneOf20km[$i];

                    echo $op20km;
                    $add_ToOpQuery = "INSERT INTO `op_fetch_appt`(`APPT_ID`, `OP_ID`, `APPT_STATUS`) VALUES ('$apid','$op20km', 'PENDING')";

                    $add_ToOpResult = mysqli_query($db_con, $add_ToOpQuery);


                    $response["message4"] = "Successfully APPT aadded to less than 20.";

                    $msg = "Hey! Check Out... You Got An Appointment...";


                    //change here
                    $confirm =  $client->messages->create(
                        // Where to send a text message (your cell phone?)
                        '+91' . $phone20km,
                        array(
                            'from' => $twilio_number,
                            'body' => $msg
                        )
                    );

                }
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
} else {
    //Invalid parameters
    $response["error"] = TRUE;
    $response["message"] = "Invalid Parameters";
    echo json_encode($response);
    exit;
}
