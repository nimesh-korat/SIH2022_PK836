<?php

include 'db.php';

// Create connection
//$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
$conn =new mysqli("localhost","id19279270_aadhaarbooking2022","Adi@15Nimesh","id19279270_aadhaar2022");


if ($_SERVER['REQUEST_METHOD'] == 'POST') {
	$DefaultId = 0;

	$ImageData = $_POST['image_path'];

	$ImageName = $_POST['image_name'];

	$GetOldIdSQL = "SELECT id FROM userData ORDER BY id ASC";

	$Query = mysqli_query($conn, $GetOldIdSQL);

	while ($row = mysqli_fetch_array($Query)) {

		$DefaultId = $row['id'];
	}

	$ImagePath = "images/$DefaultId.png";

	$ServerURL = "https://aadhaaroperatorbooking.000webhostapp.com/$ImagePath";

	$InsertSQL = "insert into userData (image_path,image_name) values ('$ServerURL','$ImageName')";

	if (mysqli_query($conn, $InsertSQL)) {

		file_put_contents($ImagePath, base64_decode($ImageData));

		echo "Your Image Has Been Uploaded.";
	}

	mysqli_close($conn);
} else {
	echo "Not Uploaded";
}
