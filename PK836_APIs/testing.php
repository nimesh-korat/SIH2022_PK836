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

if (
    isset($_POST["name"]) && isset($_POST["surname"]) && isset($_POST["email"]) && isset($_POST["password"])  && isset($_POST["contact"]) && isset($_POST["address"]) && isset($_POST["role"])
    && isset($_POST["city"]) && isset($_POST["state"]) && isset($_POST["pincode"]) && isset($_POST["dob"]) && isset($_POST["gender"])
) {

    $name = $_POST["name"];
    $surname = $_POST["surname"];
    $mail = $_POST["email"];
    $pass = $_POST["password"];
    $contact = $_POST["contact"];
    $addres = $_POST["address"];
    $rol = $_POST["role"];
    $city = $_POST["city"];
    $state = $_POST["state"];
    $pincode = $_POST["pincode"];
    $dob = $_POST["dob"];
    $gender = $_POST["gender"];

    //check user phoneno whether its already registered
    $checkEmailQuery = "select * from login_table where PHONE_NO = '$contact'";
    $result = mysqli_query($db_con, $checkEmailQuery);


    if ($result->num_rows > 0) {
        $response["error"] = TRUE;
        $response["message"] = "Sorry phone number already found.";
        echo json_encode($response);
        exit;
    } else {
        $signupQuery = "INSERT INTO `login_table`(`F_NAME`, `L_NAME`,`EMAIL_ID`, `PASSWORD`, `PHONE_NO`, `ADDRESS`, `ROLE`, `CITY`, `STATE`, `PINCODE`, `DOB`, `GENDER`) VALUES ('$name', '$surname','$mail', MD5('$pass'),'$contact', '$addres', '$rol', '$city', '$state', '$pincode', '$dob', '$gender')";
        $signupResult = mysqli_query($db_con, $signupQuery);
        $getLastId =  mysqli_insert_id($db_con);

        $AddDailyQuotaQuery = "INSERT INTO `op_daily_quota`(`OP_ID`, `DAILY_QUOTA`) VALUES ('$getLastId', '5')";
        $AddDailyQuotaResult = mysqli_query($db_con, $AddDailyQuotaQuery);

        if ($signupResult) {

            $response["error"] = FALSE;
            $response["message"] = "Successfully Signed Up.";
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
