<?php

class DbConnection
{
    private $con;

    function __construct()
    {
        

    }

    function connect()
    {
        include_once __DIR__ . '/DbConstant.php';
        $this->con = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

        if ($this->con->connect_errno) {
            echo "Failed to connect to MySQL: " . $this->con->connect_error;
            return null;
        }

        return $this->con;
    }
}

?>