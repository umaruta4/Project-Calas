<?php

function makeError($msg){
    $responseData = array();
    $responseData['status'] = 0;
    $data = array();
    $data['error'] = true;
    $data['message'] = $msg;
    $data['response'] = $responseData;
    return json_encode($data);
}

function makeSuccess($responseData){
    $data = array();
    $data['error'] = false;
    $data['message'] = "";
    $data['response'] = $responseData;
    return json_encode($data);
}

function isTheParametersAvailable($required_fields, $requestData)
{
    global $app;
    $error = false;
    $error_fields = "";
    $request_params = $requestData;

    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
 
    if ($error) {
        $response = array();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echo json_encode($response);
        return false;
    }
    return true;
}

$server = "localhost";
$username = "root";
$password = "";
$database = "labib_test";

try {
    $conn = mysqli_connect($server, $username, $password, $database) or die(makeError("Server gagal koneksi ke mysql!"));
} catch (Exception $e){
    die(makeError(mysqli_connect_error()));
}



?>