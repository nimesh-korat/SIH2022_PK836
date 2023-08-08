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

if (isset($_POST["APNT_ID"]) && isset($_POST["OP_ID"]) && isset($_POST["OP_NAME"])) { //OP_NAME get by sharedPref from app

	$APNT_ID = $_POST["APNT_ID"];
	$OP_ID = $_POST["OP_ID"];
	$OP_NAME = $_POST["OP_NAME"];

	$checkApptQuery = "select `APNT_ID` from `appointment_details` where `APNT_ID` = '$APNT_ID'";
	$checkApptresult = mysqli_query($db_con, $checkApptQuery);

	$checkStatusQuery = "select `APNT_STATUS`, `USERID`, `U_PHONE_NO` from `appointment_details` where `APNT_STATUS` = 'PENDING' AND `APNT_ID` = '$APNT_ID'";
	$result = mysqli_query($db_con, $checkStatusQuery);

	$val = mysqli_fetch_assoc($result);
	$U_PHONE = $val['U_PHONE_NO'];
	$USERID = $val['USERID'];

	$checkOpStatusQuery = "select `APNT_ID` from `appointment_details` where `APNT_STATUS` = 'ACCEPTED' AND `OP_ID` = '$OP_ID'";
	$resultOp = mysqli_query($db_con, $checkOpStatusQuery);


	if ($checkApptresult->num_rows == 0) {
		$response["error"] = TRUE;
		$response["message"] = "Appointment Does't Exist!!!";
		echo json_encode($response);
		exit;
	} elseif ($resultOp->num_rows > 0) {
		$response["error"] = TRUE;
		$response["message"] = "You have already ACTIVE Appointment.";
		echo json_encode($response);
		exit;
	} elseif ($result->num_rows == 0) {
		$response["error"] = TRUE;
		$response["message"] = "Appointment Already Accepted.";
		echo json_encode($response);
		exit;
	} else {
		$acceptQuery = "UPDATE `appointment_details` SET `OP_ID` = '$OP_ID', `APNT_STATUS`= 'ACCEPTED', `APNT_ACPT_DT`= NOW() WHERE `APNT_ID` = '$APNT_ID'";
		$acceptResult = mysqli_query($db_con, $acceptQuery);

		if ($acceptResult) {

			$updateStatusOwnQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'ACCEPTED' WHERE `OP_ID` = '$OP_ID' AND `APPT_ID` = '$APNT_ID' AND `APPT_STATUS` = 'PENDING'";
			$updateStatusOwnresult = mysqli_query($db_con, $updateStatusOwnQuery);

			$updateStatusOtherQuery = "UPDATE `op_fetch_appt` SET `APPT_STATUS` = 'ACCEPTED_BY_OTHER' WHERE `APPT_ID` = '$APNT_ID' AND `APPT_STATUS` = 'PENDING'";

			$updateStatusOtherresult = mysqli_query($db_con, $updateStatusOtherQuery);

			$response["error"] = FALSE;
			$response["message"] = "Appointment Successfully Accepted";
			echo json_encode($response);

			$pass = 6;
			$otp = (generateNumericOTP($pass));

			$msg = "Your Appointment has been accepted by " . $OP_NAME . " And Your OTP is " . $otp;

			//change here
			$confirm =  $client->messages->create(
				// Where to send a text message (your cell phone?)
				'+91' . $U_PHONE,
				array(
					'from' => $twilio_number,
					'body' => $msg
				)
			);

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
