<?php

include_once '../include/DbOperation.php';
include_once '../include/DbConstant.php';

$url = "https://infant-annihilator.000webhostapp.com/";
$header = $url . "slimapi-test/public/";

$target = $header . "createroom";

$curl = curl_init($target);
$data = ["nm_ruangan" => "babikfuck"];


// 1. Set the CURLOPT_RETURNTRANSFER option to true
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
// 2. Set the CURLOPT_POST option to true for POST request
curl_setopt($curl, CURLOPT_POST, true);
// 3. Set the request data as JSON using json_encode function
curl_setopt($curl, CURLOPT_POSTFIELDS,  json_encode($data));
// 4. Set custom headers for RapidAPI Auth and Content-Type header
curl_setopt($curl, CURLOPT_HTTPHEADER, [
  'Content-Type: application/json'
]);

$response = curl_exec($curl);

curl_close($curl);

echo gettype($response);


?>