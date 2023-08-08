<?php
include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

$response = array();

$checkQuery = "SELECT DATE(APNT_PEND_DT) as 'DATE', COUNT(*) as 'COUNT' FROM appointment_details WHERE APNT_PEND_DT BETWEEN NOW() - INTERVAL 30 DAY AND NOW() GROUP BY DAY(APNT_PEND_DT)"; // change here.

$result = mysqli_query($db_con, $checkQuery);
$numrow = mysqli_num_rows($result);

if ($result->num_rows == 0) {
	$response["error"] = TRUE;
	$response["message"] = "Sorry no data found.";
	echo json_encode($response);
	exit;
} else {
	$data = array();

	for ($i = 1; $i <= $numrow; $i++) {
		while ($val = mysqli_fetch_assoc($result)) {

			$details['DATE'] = $val['DATE'];
			$details['COUNT'] = $val['COUNT'];

			array_push($data, $details);
		}
	}
	$response["MONTHY_DATA"] = $data; // change in response name.
	$response["error"] = FALSE;
	$response["message"] = "Successfully data Found.";
	echo json_encode($response);
	exit;
}
