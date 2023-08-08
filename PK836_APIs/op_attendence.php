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

if (isset($_POST["OP_ID"])  && isset($_POST["OP_ATTENDENCE"]) && isset($_POST["ATTEND_DATE"]) && isset($_POST["OP_LAT"]) && isset($_POST["OP_LONG"])) {

	$OP_ID = $_POST["OP_ID"];
	$OP_ATTENDENCE = $_POST["OP_ATTENDENCE"];
	$ATTEND_DATE = $_POST["ATTEND_DATE"];
	$OP_LAT = $_POST["OP_LAT"];
	$OP_LONG = $_POST["OP_LONG"];


	$checkTodayAttendenceQuery = "select DATE(`ATTEND_DATE`) from op_attendence where DATE(`ATTEND_DATE`) = DATE('$ATTEND_DATE') AND `OP_ID` = '$OP_ID'";

	$checkTodayAttendenceResult = mysqli_query($db_con, $checkTodayAttendenceQuery);

	if ($checkTodayAttendenceResult->num_rows > 0) {
		$response["error"] = TRUE;
		$response["message"] = "You have already made attendence!!!";
		echo json_encode($response);
		exit;
	} else {
		$addAttendenceQuery = "INSERT INTO `op_attendence`(`OP_ID`, `OP_ATTENDENCE`, `ATTEND_DATE`, `OP_LAT`, `OP_LONG`) VALUES ('$OP_ID','$OP_ATTENDENCE','$ATTEND_DATE','$OP_LAT','$OP_LONG')";

		$addAttendenceResult = mysqli_query($db_con, $addAttendenceQuery);
		
		if ($addAttendenceResult) {


			$response["error"] = FALSE;
			$response["message"] = "Attendence Added Successfully.";
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
