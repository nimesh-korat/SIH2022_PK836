<?php

header('Content-Type: application/json');
include('db.php');


$response = array();

if (isset($_FILES["foodimage"]["name"])) {

	$filename = addslashes($_FILES['foodimage']['name']);
	$tmpname = addslashes($_FILES['foodimage']['tmp_name']);
	date_default_timezone_set("Asia/Calcutta");
	$date = date('Y-m-d H:i:s');
	$iname = strtotime(date('Y-m-d H:i:s'));
	$extension = pathinfo($filename, PATHINFO_EXTENSION);
	$image_path = $iname . "." . $extension;

	if ($filename) {
		move_uploaded_file($_FILES["foodimage"]["tmp_name"], "images/" . $image_path);
	}

	$query = "INSERT INTO `userData`(`image_path`) VALUES ('$image_path')";
	$res = mysqli_query($db_con, $query);

	if ($res) {
		$response["error"] = FALSE;
		$response["message"] = "Food added successfully.";
		echo json_encode($response);
		exit;
	} else {
		$response["error"] = TRUE;
		$response["message"] = "Error while inserting.";
		echo json_encode($response);
		exit;
	}
} else {
	$response["error"] = TRUE;
	$response["message"] = "Invalid paramters";
	echo json_encode($response);
	exit;
}
