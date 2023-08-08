<?php

include('db.php');
header('Content-Type: application/json');
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: *");

if (mysqli_connect_errno()) {
    $response["error"] = TRUE;
    $response["message"] = "Faild to connect to database";
    echo json_encode($response);
    exit;
}

$getStateCountPendingQuery = "SELECT
    SUM(IF(U_STATE = 'Andaman & Nicobar', 1, 0)) AS ANDAMAN_AND_NICOBAR,
    SUM(IF(U_STATE = 'Andhra Pradesh', 1, 0)) AS ANDHRA_PRADESH,
    SUM(IF(U_STATE = 'Arunachal Pradesh', 1, 0)) AS ARUNACHAL_PRADESH,
    SUM(IF(U_STATE = 'Assam', 1, 0)) AS ASSAM,
    SUM(IF(U_STATE = 'Bihar', 1, 0)) AS BIHAR,
    SUM(IF(U_STATE = 'Chandigarh', 1, 0)) AS CHANDIGARH,
    SUM(IF(U_STATE = 'Chattisgarh', 1, 0)) AS CHATTISGARH,
    SUM(IF(U_STATE = 'Dadra & Nagar Haveli', 1, 0)) AS DADRA_AND_NAGAR_HAVELI,
    SUM(IF(U_STATE = 'Daman & Diu', 1, 0)) AS DAMAN_AND_DIU,
    SUM(IF(U_STATE = 'Delhi', 1, 0)) AS DELHI,
    SUM(IF(U_STATE = 'Goa', 1, 0)) AS GOA,
    SUM(IF(U_STATE = 'Gujarat', 1, 0)) AS GUJARAT,
    SUM(IF(U_STATE = 'Haryana', 1, 0)) AS HARYANA,
    SUM(IF(U_STATE = 'Himachal Pradesh', 1, 0)) AS HIMACHAL_PRADESH,
    SUM(IF(U_STATE = 'Jammu & Kashmir', 1, 0)) AS JAMMU_AND_KASHMIR,
    SUM(IF(U_STATE = 'Jharkhand', 1, 0)) AS JHARKHAND,
    SUM(IF(U_STATE = 'Karnataka', 1, 0)) AS KARNATAKA,
    SUM(IF(U_STATE = 'Kerala', 1, 0)) AS KERALA,
    SUM(IF(U_STATE = 'Lakshadweep', 1, 0)) AS LAKSHADWEEP,
    SUM(IF(U_STATE = 'Madhya Pradesh', 1, 0)) AS MADHYA_PRADESH,
    SUM(IF(U_STATE = 'Maharashtra', 1, 0)) AS MAHARASHTRA,
    SUM(IF(U_STATE = 'Manipur', 1, 0)) AS MANIPUR,
    SUM(IF(U_STATE = 'Meghalaya', 1, 0)) AS MEGHALAYA,
    SUM(IF(U_STATE = 'Mizoram', 1, 0)) AS MIZORAM,
    SUM(IF(U_STATE = 'Nagaland', 1, 0)) AS NAGALAND,
    SUM(IF(U_STATE = 'Odisha', 1, 0)) AS ODISHA,
    SUM(IF(U_STATE = 'Pondicherry', 1, 0)) AS PONDICHERRY,
    SUM(IF(U_STATE = 'Punjab', 1, 0)) AS PUNJAB,
    SUM(IF(U_STATE = 'Rajasthan', 1, 0)) AS RAJASTHAN,
    SUM(IF(U_STATE = 'Sikkim', 1, 0)) AS SIKKIM,
    SUM(IF(U_STATE = 'Tamil Nadu', 1, 0)) AS TAMIL_NADU,
    SUM(IF(U_STATE = 'Telangana', 1, 0)) AS TELANGANA,
    SUM(IF(U_STATE = 'Tripura', 1, 0)) AS TRIPURA,
    SUM(IF(U_STATE = 'Uttar Pradesh', 1, 0)) AS UTTAR_PRADESH,
    SUM(IF(U_STATE = 'Uttarakhand', 1, 0)) AS UTTARAKHAND,
    SUM(IF(U_STATE = 'West Bengal', 1, 0)) AS WEST_BENGAL
     FROM appointment_details  WHERE APNT_STATUS = 'PENDING'";

$getStateCountPendingresult = mysqli_query($db_con, $getStateCountPendingQuery);
$numrowPending = mysqli_num_rows($getStateCountPendingresult);

$getStateCountActiveQuery = "SELECT
    SUM(IF(U_STATE = 'Andaman & Nicobar', 1, 0)) AS ANDAMAN_AND_NICOBAR,
    SUM(IF(U_STATE = 'Andhra Pradesh', 1, 0)) AS ANDHRA_PRADESH,
    SUM(IF(U_STATE = 'Arunachal Pradesh', 1, 0)) AS ARUNACHAL_PRADESH,
    SUM(IF(U_STATE = 'Assam', 1, 0)) AS ASSAM,
    SUM(IF(U_STATE = 'Bihar', 1, 0)) AS BIHAR,
    SUM(IF(U_STATE = 'Chandigarh', 1, 0)) AS CHANDIGARH,
    SUM(IF(U_STATE = 'Chattisgarh', 1, 0)) AS CHATTISGARH,
    SUM(IF(U_STATE = 'Dadra & Nagar Haveli', 1, 0)) AS DADRA_AND_NAGAR_HAVELI,
    SUM(IF(U_STATE = 'Daman & Diu', 1, 0)) AS DAMAN_AND_DIU,
    SUM(IF(U_STATE = 'Delhi', 1, 0)) AS DELHI,
    SUM(IF(U_STATE = 'Goa', 1, 0)) AS GOA,
    SUM(IF(U_STATE = 'Gujarat', 1, 0)) AS GUJARAT,
    SUM(IF(U_STATE = 'Haryana', 1, 0)) AS HARYANA,
    SUM(IF(U_STATE = 'Himachal Pradesh', 1, 0)) AS HIMACHAL_PRADESH,
    SUM(IF(U_STATE = 'Jammu & Kashmir', 1, 0)) AS JAMMU_AND_KASHMIR,
    SUM(IF(U_STATE = 'Jharkhand', 1, 0)) AS JHARKHAND,
    SUM(IF(U_STATE = 'Karnataka', 1, 0)) AS KARNATAKA,
    SUM(IF(U_STATE = 'Kerala', 1, 0)) AS KERALA,
    SUM(IF(U_STATE = 'Lakshadweep', 1, 0)) AS LAKSHADWEEP,
    SUM(IF(U_STATE = 'Madhya Pradesh', 1, 0)) AS MADHYA_PRADESH,
    SUM(IF(U_STATE = 'Maharashtra', 1, 0)) AS MAHARASHTRA,
    SUM(IF(U_STATE = 'Manipur', 1, 0)) AS MANIPUR,
    SUM(IF(U_STATE = 'Meghalaya', 1, 0)) AS MEGHALAYA,
    SUM(IF(U_STATE = 'Mizoram', 1, 0)) AS MIZORAM,
    SUM(IF(U_STATE = 'Nagaland', 1, 0)) AS NAGALAND,
    SUM(IF(U_STATE = 'Odisha', 1, 0)) AS ODISHA,
    SUM(IF(U_STATE = 'Pondicherry', 1, 0)) AS PONDICHERRY,
    SUM(IF(U_STATE = 'Punjab', 1, 0)) AS PUNJAB,
    SUM(IF(U_STATE = 'Rajasthan', 1, 0)) AS RAJASTHAN,
    SUM(IF(U_STATE = 'Sikkim', 1, 0)) AS SIKKIM,
    SUM(IF(U_STATE = 'Tamil Nadu', 1, 0)) AS TAMIL_NADU,
    SUM(IF(U_STATE = 'Telangana', 1, 0)) AS TELANGANA,
    SUM(IF(U_STATE = 'Tripura', 1, 0)) AS TRIPURA,
    SUM(IF(U_STATE = 'Uttar Pradesh', 1, 0)) AS UTTAR_PRADESH,
    SUM(IF(U_STATE = 'Uttarakhand', 1, 0)) AS UTTARAKHAND,
    SUM(IF(U_STATE = 'West Bengal', 1, 0)) AS WEST_BENGAL
     FROM appointment_details  WHERE APNT_STATUS = 'ACCEPTED'";

$getStateCountActiveresult = mysqli_query($db_con, $getStateCountActiveQuery);
$numrowActive = mysqli_num_rows($getStateCountActiveresult);

$getStateCountCompletedQuery = "SELECT
    SUM(IF(U_STATE = 'Andaman & Nicobar', 1, 0)) AS ANDAMAN_AND_NICOBAR,
    SUM(IF(U_STATE = 'Andhra Pradesh', 1, 0)) AS ANDHRA_PRADESH,
    SUM(IF(U_STATE = 'Arunachal Pradesh', 1, 0)) AS ARUNACHAL_PRADESH,
    SUM(IF(U_STATE = 'Assam', 1, 0)) AS ASSAM,
    SUM(IF(U_STATE = 'Bihar', 1, 0)) AS BIHAR,
    SUM(IF(U_STATE = 'Chandigarh', 1, 0)) AS CHANDIGARH,
    SUM(IF(U_STATE = 'Chattisgarh', 1, 0)) AS CHATTISGARH,
    SUM(IF(U_STATE = 'Dadra & Nagar Haveli', 1, 0)) AS DADRA_AND_NAGAR_HAVELI,
    SUM(IF(U_STATE = 'Daman & Diu', 1, 0)) AS DAMAN_AND_DIU,
    SUM(IF(U_STATE = 'Delhi', 1, 0)) AS DELHI,
    SUM(IF(U_STATE = 'Goa', 1, 0)) AS GOA,
    SUM(IF(U_STATE = 'Gujarat', 1, 0)) AS GUJARAT,
    SUM(IF(U_STATE = 'Haryana', 1, 0)) AS HARYANA,
    SUM(IF(U_STATE = 'Himachal Pradesh', 1, 0)) AS HIMACHAL_PRADESH,
    SUM(IF(U_STATE = 'Jammu & Kashmir', 1, 0)) AS JAMMU_AND_KASHMIR,
    SUM(IF(U_STATE = 'Jharkhand', 1, 0)) AS JHARKHAND,
    SUM(IF(U_STATE = 'Karnataka', 1, 0)) AS KARNATAKA,
    SUM(IF(U_STATE = 'Kerala', 1, 0)) AS KERALA,
    SUM(IF(U_STATE = 'Lakshadweep', 1, 0)) AS LAKSHADWEEP,
    SUM(IF(U_STATE = 'Madhya Pradesh', 1, 0)) AS MADHYA_PRADESH,
    SUM(IF(U_STATE = 'Maharashtra', 1, 0)) AS MAHARASHTRA,
    SUM(IF(U_STATE = 'Manipur', 1, 0)) AS MANIPUR,
    SUM(IF(U_STATE = 'Meghalaya', 1, 0)) AS MEGHALAYA,
    SUM(IF(U_STATE = 'Mizoram', 1, 0)) AS MIZORAM,
    SUM(IF(U_STATE = 'Nagaland', 1, 0)) AS NAGALAND,
    SUM(IF(U_STATE = 'Odisha', 1, 0)) AS ODISHA,
    SUM(IF(U_STATE = 'Pondicherry', 1, 0)) AS PONDICHERRY,
    SUM(IF(U_STATE = 'Punjab', 1, 0)) AS PUNJAB,
    SUM(IF(U_STATE = 'Rajasthan', 1, 0)) AS RAJASTHAN,
    SUM(IF(U_STATE = 'Sikkim', 1, 0)) AS SIKKIM,
    SUM(IF(U_STATE = 'Tamil Nadu', 1, 0)) AS TAMIL_NADU,
    SUM(IF(U_STATE = 'Telangana', 1, 0)) AS TELANGANA,
    SUM(IF(U_STATE = 'Tripura', 1, 0)) AS TRIPURA,
    SUM(IF(U_STATE = 'Uttar Pradesh', 1, 0)) AS UTTAR_PRADESH,
    SUM(IF(U_STATE = 'Uttarakhand', 1, 0)) AS UTTARAKHAND,
    SUM(IF(U_STATE = 'West Bengal', 1, 0)) AS WEST_BENGAL
     FROM appointment_details  WHERE APNT_STATUS = 'COMPLETED'";

$getStateCountCompletedresult = mysqli_query($db_con, $getStateCountCompletedQuery);
$numrowCompleted = mysqli_num_rows($getStateCountCompletedresult);

if ($getStateCountPendingresult == null) {
    $response["error"] = TRUE;
    $response["message"] = "Can't getting Data!!!";
    echo json_encode($response);
    exit;
} else {

    $data = array();
    $data1 = array();
    $data2 = array();

    for ($i = 1; $i <= $numrowPending; $i++) {
        while ($val = mysqli_fetch_assoc($getStateCountPendingresult)) {

            $details['ANDAMAN_AND_NICOBAR'] = $val['ANDAMAN_AND_NICOBAR'];
            $details['ANDHRA_PRADESH'] = $val['ANDHRA_PRADESH'];
            $details['ARUNACHAL_PRADESH'] = $val['ARUNACHAL_PRADESH'];
            $details['ASSAM'] = $val['ASSAM'];
            $details['BIHAR'] = $val['BIHAR'];
            $details['CHANDIGARH'] = $val['CHANDIGARH'];
            $details['CHATTISGARH'] = $val['CHATTISGARH'];
            $details['DADRA_AND_NAGAR_HAVELI'] = $val['DADRA_AND_NAGAR_HAVELI'];
            $details['DAMAN_AND_DIU'] = $val['DAMAN_AND_DIU'];
            $details['DELHI'] = $val['DELHI'];
            $details['GOA'] = $val['GOA'];
            $details['GUJARAT'] = $val['GUJARAT'];
            $details['HARYANA'] = $val['HARYANA'];
            $details['HIMACHAL_PRADESH'] = $val['HIMACHAL_PRADESH'];
            $details['MAHARASHTRA'] = $val['MAHARASHTRA'];
            $details['JAMMU_AND_KASHMIR'] = $val['JAMMU_AND_KASHMIR'];
            $details['JHARKHAND'] = $val['JHARKHAND'];
            $details['KARNATAKA'] = $val['KARNATAKA'];
            $details['KERALA'] = $val['KERALA'];
            $details['LAKSHADWEEP'] = $val['LAKSHADWEEP'];
            $details['MADHYA_PRADESH'] = $val['MADHYA_PRADESH'];
            $details['MANIPUR'] = $val['MANIPUR'];
            $details['MEGHALAYA'] = $val['MEGHALAYA'];
            $details['MIZORAM'] = $val['MIZORAM'];
            $details['NAGALAND'] = $val['NAGALAND'];
            $details['ODISHA'] = $val['ODISHA'];
            $details['PONDICHERRY'] = $val['PONDICHERRY'];
            $details['PUNJAB'] = $val['PUNJAB'];
            $details['RAJASTHAN'] = $val['RAJASTHAN'];
            $details['SIKKIM'] = $val['SIKKIM'];
            $details['TAMIL_NADU'] = $val['TAMIL_NADU'];
            $details['TELANGANA'] = $val['TELANGANA'];
            $details['TRIPURA'] = $val['TRIPURA'];
            $details['UTTAR_PRADESH'] = $val['UTTAR_PRADESH'];
            $details['UTTARAKHAND'] = $val['UTTARAKHAND'];
            $details['WEST_BENGAL'] = $val['WEST_BENGAL'];

            array_push($data, $details);
        }
    }

    for ($i = 1; $i <= $numrowActive; $i++) {
        while ($val = mysqli_fetch_assoc($getStateCountActiveresult)) {

            $details['ANDAMAN_AND_NICOBAR'] = $val['ANDAMAN_AND_NICOBAR'];
            $details['ANDHRA_PRADESH'] = $val['ANDHRA_PRADESH'];
            $details['ARUNACHAL_PRADESH'] = $val['ARUNACHAL_PRADESH'];
            $details['ASSAM'] = $val['ASSAM'];
            $details['BIHAR'] = $val['BIHAR'];
            $details['CHANDIGARH'] = $val['CHANDIGARH'];
            $details['CHATTISGARH'] = $val['CHATTISGARH'];
            $details['DADRA_AND_NAGAR_HAVELI'] = $val['DADRA_AND_NAGAR_HAVELI'];
            $details['DAMAN_AND_DIU'] = $val['DAMAN_AND_DIU'];
            $details['DELHI'] = $val['DELHI'];
            $details['GOA'] = $val['GOA'];
            $details['GUJARAT'] = $val['GUJARAT'];
            $details['HARYANA'] = $val['HARYANA'];
            $details['HIMACHAL_PRADESH'] = $val['HIMACHAL_PRADESH'];
            $details['MAHARASHTRA'] = $val['MAHARASHTRA'];
            $details['JAMMU_AND_KASHMIR'] = $val['JAMMU_AND_KASHMIR'];
            $details['JHARKHAND'] = $val['JHARKHAND'];
            $details['KARNATAKA'] = $val['KARNATAKA'];
            $details['KERALA'] = $val['KERALA'];
            $details['LAKSHADWEEP'] = $val['LAKSHADWEEP'];
            $details['MADHYA_PRADESH'] = $val['MADHYA_PRADESH'];
            $details['MANIPUR'] = $val['MANIPUR'];
            $details['MEGHALAYA'] = $val['MEGHALAYA'];
            $details['MIZORAM'] = $val['MIZORAM'];
            $details['NAGALAND'] = $val['NAGALAND'];
            $details['ODISHA'] = $val['ODISHA'];
            $details['PONDICHERRY'] = $val['PONDICHERRY'];
            $details['PUNJAB'] = $val['PUNJAB'];
            $details['RAJASTHAN'] = $val['RAJASTHAN'];
            $details['SIKKIM'] = $val['SIKKIM'];
            $details['TAMIL_NADU'] = $val['TAMIL_NADU'];
            $details['TELANGANA'] = $val['TELANGANA'];
            $details['TRIPURA'] = $val['TRIPURA'];
            $details['UTTAR_PRADESH'] = $val['UTTAR_PRADESH'];
            $details['UTTARAKHAND'] = $val['UTTARAKHAND'];
            $details['WEST_BENGAL'] = $val['WEST_BENGAL'];

            array_push($data1, $details);
        }
    }

    for ($i = 1; $i <= $numrowCompleted; $i++) {
        while ($val = mysqli_fetch_assoc($getStateCountCompletedresult)) {

            $details['ANDAMAN_AND_NICOBAR'] = $val['ANDAMAN_AND_NICOBAR'];
            $details['ANDHRA_PRADESH'] = $val['ANDHRA_PRADESH'];
            $details['ARUNACHAL_PRADESH'] = $val['ARUNACHAL_PRADESH'];
            $details['ASSAM'] = $val['ASSAM'];
            $details['BIHAR'] = $val['BIHAR'];
            $details['CHANDIGARH'] = $val['CHANDIGARH'];
            $details['CHATTISGARH'] = $val['CHATTISGARH'];
            $details['DADRA_AND_NAGAR_HAVELI'] = $val['DADRA_AND_NAGAR_HAVELI'];
            $details['DAMAN_AND_DIU'] = $val['DAMAN_AND_DIU'];
            $details['DELHI'] = $val['DELHI'];
            $details['GOA'] = $val['GOA'];
            $details['GUJARAT'] = $val['GUJARAT'];
            $details['HARYANA'] = $val['HARYANA'];
            $details['HIMACHAL_PRADESH'] = $val['HIMACHAL_PRADESH'];
            $details['MAHARASHTRA'] = $val['MAHARASHTRA'];
            $details['JAMMU_AND_KASHMIR'] = $val['JAMMU_AND_KASHMIR'];
            $details['JHARKHAND'] = $val['JHARKHAND'];
            $details['KARNATAKA'] = $val['KARNATAKA'];
            $details['KERALA'] = $val['KERALA'];
            $details['LAKSHADWEEP'] = $val['LAKSHADWEEP'];
            $details['MADHYA_PRADESH'] = $val['MADHYA_PRADESH'];
            $details['MANIPUR'] = $val['MANIPUR'];
            $details['MEGHALAYA'] = $val['MEGHALAYA'];
            $details['MIZORAM'] = $val['MIZORAM'];
            $details['NAGALAND'] = $val['NAGALAND'];
            $details['ODISHA'] = $val['ODISHA'];
            $details['PONDICHERRY'] = $val['PONDICHERRY'];
            $details['PUNJAB'] = $val['PUNJAB'];
            $details['RAJASTHAN'] = $val['RAJASTHAN'];
            $details['SIKKIM'] = $val['SIKKIM'];
            $details['TAMIL_NADU'] = $val['TAMIL_NADU'];
            $details['TELANGANA'] = $val['TELANGANA'];
            $details['TRIPURA'] = $val['TRIPURA'];
            $details['UTTAR_PRADESH'] = $val['UTTAR_PRADESH'];
            $details['UTTARAKHAND'] = $val['UTTARAKHAND'];
            $details['WEST_BENGAL'] = $val['WEST_BENGAL'];

            array_push($data2, $details);
        }
    }


    $response["STATE_P_DATA"] = $data;
    $response["STATE_A_DATA"] = $data1;
    $response["STATE_C_DATA"] = $data2;
    $response["error"] = FALSE;
    $response["message"] = "PENDING Data Got Succesfully";
    echo json_encode($response);
    exit;
}
