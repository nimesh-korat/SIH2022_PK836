<?php
require __DIR__ . '/vendor/autoload.php';



use Twilio\Rest\Client;

$account_sid = '...'; //sid
$auth_token = '...'; //token
$client = new Client($account_sid, $auth_token);
$twilio_number = "+14434197394";

$phone = '7567268072';



//change here
$client->messages->create(
    // Where to send a text message (your cell phone?)
    '+91' . $phone,
    array(
        'from' => $twilio_number,
        'body' => 'I sent this message in under 10 minutes!'
    )
);
