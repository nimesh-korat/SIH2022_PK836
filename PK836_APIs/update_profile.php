<?php

include('db.php');

$response = array();
header('Content-Type: application/json');

if (isset($_POST["F_NAME"]) && isset($_POST["L_NAME"]) && isset($_POST["EMAIL_ID"]) && isset($_POST["PHONE_NO"])  && isset($_POST["ADDRESS"]) && isset($_POST["DOB"]) && isset($_POST["GENDER"]) && isset($_POST["PINCODE"]) && isset($_POST["CITY"]) && isset($_POST["STATE"]) && isset($_POST["USERID"])) {

	$F_NAME = $_POST["F_NAME"];
	$L_NAME = $_POST["L_NAME"];
	$EMAIL_ID = $_POST["EMAIL_ID"];
	$PHONE_NO = $_POST["PHONE_NO"];
	$ADDRESS = $_POST["ADDRESS"];
	$DOB = $_POST["DOB"];
	$GENDER = $_POST["GENDER"];
	$PINCODE = $_POST["PINCODE"];
	$CITY = $_POST["CITY"];
	$STATE = $_POST["STATE"];
	$USERID = $_POST["USERID"];

	$Query = "UPDATE login_table SET F_NAME='$F_NAME' , L_NAME = '$L_NAME' , EMAIL_ID = '$EMAIL_ID' , PHONE_NO = '$PHONE_NO' , ADDRESS = '$ADDRESS', DOB='$DOB' , GENDER = '$GENDER' , PINCODE = '$PINCODE' , CITY = '$CITY', STATE = '$STATE' WHERE USERID='$USERID'";

	$result = mysqli_query($db_con, $Query);


	if ($result) {
		$response["error"] = FALSE;
		$response["message"] = "Data Updated.";
		echo json_encode($response);
		exit;
	} else {

		$response["error"] = TRUE;
		$response["message"] = "Unable to Update.";
		echo json_encode($response);
		exit;
	}
} else {

	$response["error"] = TRUE;
	$response["message"] = "Invalid Parameters";
	echo json_encode($response);
	exit;
}
