<?php 

include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

if(mysqli_connect_errno())
{
    $response["error"] = TRUE;
    $response["message"] ="Faild to connect to database";
    echo json_encode($response);
    exit;
}

    $getPendCountQuery = "SELECT COUNT(*) AS `TOTAL_PENDING_APPT` FROM `appointment_details` WHERE `APNT_STATUS` = 'PENDING'";

    $getPendCountresult = mysqli_query($db_con,$getPendCountQuery);

    if($getPendCountresult==null){
        $response["error"] = TRUE;
        $response["message"] ="Can't getting Data!!!";
	    echo json_encode($response);
        exit;
    }else
        $response = mysqli_fetch_assoc($getPendCountresult);
		$response["error"] = FALSE;
        $response["message"] = "Data Got Succesfully";
		echo json_encode($response);
        exit;

?>