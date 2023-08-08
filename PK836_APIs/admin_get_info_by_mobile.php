<?php 

include('db.php');
 
$response = array();
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

if(isset($_POST["PHONE_NO"])){

 
    $PHONE_NO = $_POST["PHONE_NO"];
 
    $userQuery = "SELECT `USERID`, `F_NAME`, `L_NAME`, `EMAIL_ID`, `PHONE_NO`, `ADDRESS`, `CITY`, `STATE`, `PINCODE`, `DOB`, `GENDER`, `ROLE`, `REG_DATE_TIME` FROM `login_table` WHERE  `PHONE_NO` = '$PHONE_NO'";
    
    $result = mysqli_query($db_con,$userQuery);

    if($result->num_rows==0){
        $response["error"] = TRUE;
        $response["message"] ="No user found linked with Mobile No. $PHONE_NO";
	    echo json_encode($response);
        exit;
    }else
        $user = mysqli_fetch_assoc($result);
		$response["error"] = FALSE;
        $response["message"] = "User Data Found Successfully";
        $response["USER_DATA"] = $user;
		echo json_encode($response);
        exit;
    }
 
else {
	
    // Invalid parameters
    $response["error"] = TRUE;
    $response["message"] ="Invalid parameters";
    echo json_encode($response);
exit;}

?>