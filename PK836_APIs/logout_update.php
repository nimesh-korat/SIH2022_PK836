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

if (isset($_POST["USERID"])) {

	$USERID = $_POST["USERID"];


	$checkUserQuery = "SELECT * FROM `login_table` WHERE `USERID` = '$USERID'";
	$checkUserResult = mysqli_query($db_con, $checkUserQuery);

	if ($checkUserResult->num_rows == 0) {
		$response["error"] = TRUE;
		$response["message"] = "No such user with this credentials found!!!";
		echo json_encode($response);
		exit;
	} else {
		$updateStatusQuery = "UPDATE `login_table` SET `STATUS` = 'LOGGED_OUT' WHERE `USERID` = '$USERID'";

		$updateStatusResult = mysqli_query($db_con, $updateStatusQuery);

		if ($updateStatusResult) {
			$response["error"] = FALSE;
			$response["message"] = "Logged_out Successfully...";
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
