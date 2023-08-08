<?php
include('db.php');

$response = array();
header('Content-Type: application/json');

if(mysqli_connect_errno())
{
    $response["error"] = TRUE;
    $response["message"] ="Faild to connect to database";
    echo json_encode($response);
    exit;
}

if(isset($_POST["USERID"]) && isset($_POST["OP_ID"]) && isset($_POST["APPT_ID"]) && isset($_POST["BEHAVIOUR"])  && isset($_POST["ACCURACY"]) && isset($_POST["COMMENTS" ]))
  {
	  
	  $USERID = $_POST["USERID"];
	  $OP_ID = $_POST["OP_ID"];
	  $APPT_ID = $_POST["APPT_ID"];
	  $BEHAVIOUR = $_POST["BEHAVIOUR"];
	  $ACCURACY = $_POST["ACCURACY"];
	  $COMMENTS = $_POST["COMMENTS"];
	  
		  $addReviewQuery = "INSERT INTO `op_reviews`(`USERID`, `OP_ID`,`APPT_ID`, `BEHAVIOUR`, `ACCURACY`, `COMMENTS`) VALUES ('$USERID', '$OP_ID','$APPT_ID', '$BEHAVIOUR','$ACCURACY', '$COMMENTS')";
		  $addReviewResult = mysqli_query($db_con,$addReviewQuery);
		  if($addReviewResult)
		  {

			  $response["error"] = FALSE;
			  $response["message"] = "Thank you for your valuable review :)";
			  echo json_encode($response);
			  exit;
		  }
  }
  else
  {
	  //Invalid parameters
	  $response["error"] = TRUE;
	  $response["message"] = "Invalid Parameters";
	  echo json_encode($response);
	  exit;
  }
 ?> 
// <div class="3
// .
// ."></div>