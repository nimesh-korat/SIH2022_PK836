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

if (isset($_POST["OP_ACT_STATUS"]) && isset($_POST["OP_LAT"]) && isset($_POST["OP_LONG"])  && isset($_POST["OP_ID"])  && isset($_POST["OP_ACT_STATUS"])) {

	$OP_ACT_STATUS = $_POST["OP_ACT_STATUS"];
	$OP_LAT = $_POST["OP_LAT"];
	$OP_LONG = $_POST["OP_LONG"];
	$OP_ID = $_POST["OP_ID"];
	$STATUS_DATE_TIME = $_POST["STATUS_DATE_TIME"];

	$checkOpExistQuery = "SELECT `OP_ID` FROM `op_active_status` WHERE `OP_ID` = '$OP_ID'";
	$checkOpExistresult = mysqli_query($db_con, $checkOpExistQuery);

	if ($checkOpExistresult->num_rows == 0) {
		$addLocatonQuery = "INSERT INTO `op_active_status`(`OP_ACT_STATUS`, `OP_LAT`, `OP_LONG`, `OP_ID`, `STATUS_DATE_TIME`) VALUES ('$OP_ACT_STATUS','$OP_LAT', '$OP_LONG', '$OP_ID', '$STATUS_DATE_TIME')";

		$addLocatonResult = mysqli_query($db_con, $addLocatonQuery);

		if ($addLocatonResult) {
			$response["error"] = FALSE;
			$response["message"] = "NEW DATA INSERTED SUCCESSFULLY :)";
			echo json_encode($response);
			exit;
		} else {
			$response["error"] = TRUE;
			$response["message"] = "SOMETHING WENT WRONG :(";
			echo json_encode($response);
		}
	} else {
		$updateLocatonQuery = "UPDATE `op_active_status` SET `OP_ACT_STATUS` = '$OP_ACT_STATUS', `OP_LAT` = '$OP_LAT', `OP_LONG` = '$OP_LONG', `STATUS_DATE_TIME` = '$STATUS_DATE_TIME' WHERE `OP_ID` = '$OP_ID'";
		$updateLocatonResult = mysqli_query($db_con, $updateLocatonQuery);
		if ($updateLocatonResult) {
			$response["error"] = FALSE;
			$response["message"] = "OLD DATA UPDATED SUCCESSFULLY :)";
			echo json_encode($response);
			exit;
		} else {
			$response["error"] = TRUE;
			$response["message"] = "SOMETHING WENT WRONG :(";
			echo json_encode($response);
		}
	}
} else {
	//Invalid parameters
	$response["error"] = TRUE;
	$response["message"] = "Invalid Parameters";
	echo json_encode($response);
	exit;
}
