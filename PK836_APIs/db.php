<?php

$response = array();
$db_con = mysqli_connect("localhost","root","","aadhaar_2022");




if(mysqli_connect_errno())
{
	$response["error"] = TRUE;
	$response["message"] = "Failed to connect to database";
	echo json_encode($response);
	exit;
}


?>
