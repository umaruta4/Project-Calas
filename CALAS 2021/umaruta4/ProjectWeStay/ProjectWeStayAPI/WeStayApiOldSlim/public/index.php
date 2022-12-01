<?php

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;

require __DIR__ . '/../vendor/autoload.php';
require_once '../include/DbOperation.php';

$header = '/WeStayApiOldSlim/public';

$app =  AppFactory::create();
$app->addRoutingMiddleware();
$errorMiddleware = $app->addErrorMiddleware(true, true, true);

$app->get($header, function (Request $request, Response $response){
    $response->getBody()->write("Hello ");
    return $response;
});

$app->post($header."/registerPengguna", function(Request $request, Response $response){
    include "./config.php";
    $requestData = $request->getParsedBody();
    if(isTheParametersAvailable(array("nama_lengkap", "email", "no_ktp", "username", "password"),$requestData)){

        $namaLengkap = $requestData ['nama_lengkap'];
        $email = $requestData['email'];
        $no_ktp = $requestData['no_ktp'];
        $username =$requestData['username'];
        $password =$requestData['password'];
        $wasFound = False;
        $responseData = array();

        $query = "SELECT * FROM users WHERE username='$username';";

        foreach($conn->query($query) as $d){
            $wasFound = True;

            // gagal register
            $responseData['status'] = 0;
        }

        if(!$wasFound){
            $query = "INSERT INTO users (nama_lengkap, email, no_ktp, username, password, level) VALUES ('$namaLengkap','$email', '$no_ktp', '$username', '$password', 'pengguna');";
            $conn->query($query);

            // berhasil register
            $responseData['status'] = 1;
        }

        $response->getBody()->write(makeSuccess($responseData));
    }
    return $response;
});

$app->post($header."/registerPenyewa", function(Request $request, Response $response){
    include "./config.php";
    $requestData = $request->getParsedBody();
    if(isTheParametersAvailable(array("nama_lengkap", "email", "no_ktp", "username", "password"),$requestData)){

        $namaLengkap = $requestData ['nama_lengkap'];
        $email = $requestData['email'];
        $no_ktp = $requestData['no_ktp'];
        $username =$requestData['username'];
        $password =$requestData['password'];
        $wasFound = False;
        $responseData = array();

        $query = "SELECT * FROM users WHERE username='$username';";

        foreach($conn->query($query) as $d){
            $wasFound = True;

            // gagal register
            $responseData['status'] = 0;
        }

        if(!$wasFound){
            $query = "INSERT INTO users (nama_lengkap, email, no_ktp, username, password, level) VALUES ('$namaLengkap','$email', '$no_ktp', '$username', '$password', 'penyewa');";
            $conn->query($query);

            // berhasil register
            $responseData['status'] = 1;
        }

        $response->getBody()->write(makeSuccess($responseData));
    }
    return $response;
});

$app->post($header."/login", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getParsedBody();
    if (isTheParametersAvailable(array("username", "password"), $requestData)){
        $username = $requestData['username'];
        $password = $requestData['password'];
        $wasFound = False;
        $responseData = array();

        $query = "SELECT * FROM users WHERE username='$username' AND password='$password' LIMIT 1";

        foreach($conn->query($query) as $d){
            $wasFound = True;
            
            $responseData['status'] = 1;
            $responseData['user_id'] = (int)$d['id'];
            $responseData['level'] = $d['level'];
            $responseData['nama_lengkap'] = $d['nama_lengkap'];
        }

        if (!$wasFound){
            $responseData['status'] = 0;
            $responseData['user_id'] = 0;
            $responseData['level'] = "";
            $responseData['nama_lengkap'] = "";
        }
        
        $response->getBody()->write(makeSuccess($responseData));
    }
    return $response;
});


$app->post($header."/tambahRumah", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getParsedBody();
    if (isTheParametersAvailable(array("user_id", "nama_homestay", "fasilitas", "jenis_kamar", "lokasi", "harga", "thumbnail", "nama_thumbnail"), $requestData)){
        $userId = $requestData['user_id'];
        $namaHomestay = $requestData['nama_homestay'];
        $fasilitas = $requestData['fasilitas'];
        $jenisKamar = $requestData['jenis_kamar'];
        $lokasi = $requestData['lokasi'];
        $harga = $requestData['harga'];
        $thumbnail = $requestData['thumbnail'];
        $namaThumbnail = $requestData['nama_thumbnail'];
        
        $query = "INSERT INTO homestay (user_id, nama_homestay, fasilitas, jenis_kamar, lokasi, harga, thumbnail, nama_thumbnail) VALUES('$userId','$namaHomestay', '$fasilitas', '$jenisKamar', '$lokasi', '$harga', '$thumbnail', '$namaThumbnail')";


        $res = $conn->query($query);

        $response->getBody()->write(makeSuccess(array("status"=>1)));
    }
    return $response;
});

$app->post($header."/hapusHomestay",function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getParsedBody();
    if (isTheParametersAvailable(array("homestay_id"), $requestData)){
        $homestayId = $requestData['homestay_id'];
        
        $query = "DELETE FROM homestay WHERE id='$homestayId'";

        $res = $conn->query($query);

        $response->getBody()->write(makeSuccess(array("status"=>1)));
    }
    return $response;
});

$app->post($header."/tambahPesanan", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getParsedBody();
    if (isTheParametersAvailable(array("homestay_id", "user_id", "check_in", "check_out", "catatan"), $requestData)){
        $homestayId = $requestData['homestay_id'];
        $userId = $requestData['user_id'];
        $checkIn = $requestData['check_in'];
        $checkOut = $requestData['check_out'];
        $catatan = $requestData['catatan'];
        
        $query = "INSERT INTO pesanan (homestay_id, user_id, check_in, check_out, catatan) VALUES ('$homestayId', '$userId', '$checkIn', '$checkOut', '$catatan')";

        $res = $conn->query($query);

        $response->getBody()->write(makeSuccess(array("status"=>1)));
    }
    return $response;
});


$app->get($header."/ambilSemuaHomestay", function(Request $request, Response $response){
    include_once "./config.php";

    $data = array();
    $query = "SELECT * FROM homestay;";
    foreach($conn->query($query) as $d){
        $temp = array();
        $temp['homestay_id'] = $d['id'];
        $temp['user_id'] = $d['user_id'];
        $temp['nama_homestay'] = $d['nama_homestay'];
        $temp['fasilitas'] = $d['fasilitas'];
        $temp['jenis_kamar'] = $d['jenis_kamar'];
        $temp['lokasi'] = $d['lokasi'];
        $temp['harga'] = $d['harga'];
        $temp['thumbnail'] = $d['thumbnail'];
        $temp['nama_thumbnail'] = $d['nama_thumbnail'];
        array_push($data, $temp);
    }
    //var_dump($data);
    $response->getBody()->write(makeSuccess($data));
    return $response;
});

$app->get($header."/ambilSemuaHomestayId", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getQueryParams();
    if (isTheParametersAvailable(array("user_id"), $requestData)){
        $data = array();
        $userId = $requestData['user_id'];
        $query = "SELECT * FROM homestay WHERE user_id='$userId'";
        foreach($conn->query($query) as $d){
            $temp = array();
            $temp['homestay_id'] = $d['id'];
            $temp['user_id'] = $d['user_id'];
            $temp['nama_homestay'] = $d['nama_homestay'];
            $temp['fasilitas'] = $d['fasilitas'];
            $temp['jenis_kamar'] = $d['jenis_kamar'];
            $temp['lokasi'] = $d['lokasi'];
            $temp['harga'] = $d['harga'];
            $temp['thumbnail'] = $d['thumbnail'];
            $temp['nama_thumbnail'] = $d['nama_thumbnail'];
            array_push($data, $temp);
        }
        //var_dump($data);
        $response->getBody()->write(makeSuccess($data));
    }
    
    return $response;
});

$app->get($header."/ambilSatuHomestay", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getQueryParams();
    if (isTheParametersAvailable(array("homestay_id"), $requestData)){
        $temp = array();
        $homestayId = $requestData['homestay_id'];
        $query = "SELECT * FROM homestay WHERE id='$homestayId' LIMIT 1";
        foreach($conn->query($query) as $d){
            $temp['homestay_id'] = $d['id'];
            $temp['user_id'] = $d['user_id'];
            $temp['nama_homestay'] = $d['nama_homestay'];
            $temp['fasilitas'] = $d['fasilitas'];
            $temp['jenis_kamar'] = $d['jenis_kamar'];
            $temp['lokasi'] = $d['lokasi'];
            $temp['harga'] = $d['harga'];
            $temp['thumbnail'] = $d['thumbnail'];
            $temp['nama_thumbnail'] = $d['nama_thumbnail'];
        }
        //var_dump($data);
        $response->getBody()->write(makeSuccess($temp));
    }
    
    return $response;
});

$app->get($header."/ambilNotifikasi", function(Request $request, Response $response){
    include_once "./config.php";
    $requestData = $request->getQueryParams();
    if (isTheParametersAvailable(array("user_id"), $requestData)){
        $data = array();
        $userId = $requestData['user_id'];
        $query = "SELECT * FROM pesanan WHERE homestay_id IN (SELECT id FROM homestay WHERE user_id='$userId')";

        foreach($conn->query($query) as $d){
            $temp = array();
            $penggunaId = $d['user_id'];
            $query = "SELECT nama_lengkap FROM users WHERE id='$penggunaId' LIMIT 1;";
            foreach($conn->query($query) as $d2){
                $temp['nama_lengkap'] = $d['nama_lengkap'];
            }
            $homestayId = $d['homestay_id'];
            $query = "SELECT * FROM homestay WHERE id='$homestayId' LIMIT 1;";
            foreach($conn->query($query) as $d2){
                $temp['fasilitas'] = $d['fasilitas'];
                $temp['jenis_kamar'] = $d['jenis_kamar'];
            }
            $temp['homestay_id'] = $d['id'];
            $temp['user_id'] = $d['user_id'];
            $temp['nama_homestay'] = $d['nama_homestay'];
            $temp['fasilitas'] = $d['fasilitas'];
            $temp['jenis_kamar'] = $d['jenis_kamar'];
            $temp['lokasi'] = $d['lokasi'];
            $temp['harga'] = $d['harga'];
            $temp['thumbnail'] = $d['thumbnail'];
            $temp['nama_thumbnail'] = $d['nama_thumbnail'];
        }
        //var_dump($data);
        $response->getBody()->write(makeSuccess($temp));
    }
    
    return $response;
});

$app->run();
