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

    $getOpCountQuery = "SELECT COUNT(*) AS `TOTAL_OPERATOR` FROM `login_table` WHERE `ROLE` = 'OPERATOR'";

    $getOpCountresult = mysqli_query($db_con,$getOpCountQuery);

    if($getOpCountresult==null){
        $response["error"] = TRUE;
        $response["message"] ="Can't getting Data!!!";
	    echo json_encode($response);
        exit;
    }else
        $response = mysqli_fetch_assoc($getOpCountresult);
		$response["error"] = FALSE;
        $response["message"] = "Data Got Succesfully";
		echo json_encode($response);
        exit;

?>