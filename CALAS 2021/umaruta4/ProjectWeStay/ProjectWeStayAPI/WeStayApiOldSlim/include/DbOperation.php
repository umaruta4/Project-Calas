<?php

class DbOperation
{
    private $con;

    function __construct()
    {
        require_once __DIR__ . "/DbConnection.php";
        
        $db = new DbConnection();
        $this->con = $db->connect();
    }

    public function insertRuangan(array $ruangan)
    {   
        $nmRuangan = $ruangan['nm_ruangan'];
        if (!$this->isRuanganExist($nmRuangan)) {
            $stmt = $this->con->prepare("INSERT INTO ruangan(nm_ruangan) VALUES (?);");
            $stmt->bind_param("s",$nmRuangan);
            if ($stmt->execute()){
                return SUCCESSFUL_ENTRY;
            }
            return FAILED_ENTRY;
        }
        return DUPLICATE_ENTRY;
    }

    public function deleteRuangan(int $ruangan)
    {
        $rId = $ruangan;
        $stmt = $this->con->prepare("DELETE FROM ruangan WHERE id = ?");
        $stmt2 = $this->con->prepare("DELETE FROM kerusakan WHERE r_id = ?");
        $stmt->bind_param('i', $rId);
        $stmt2->bind_param('i', $rId);
        $stmt->execute();
        $stmt2->execute();

        return SUCCESSFUL_ENTRY;
    }

    public function updateRuangan(array $ruangan)
    {
        $rId = $ruangan['r_id'];
        $newNamaRuangan = $ruangan['nm_ruangan'];

        if (!$this->isRuanganExist($newNamaRuangan)){
            $stmt = $this->con->prepare("UPDATE ruangan SET nm_ruangan = ? WHERE id=?;"); 
            $stmt->bind_param("si", $newNamaRuangan, $rId);
            if ($stmt->execute()){
                return SUCCESSFUL_ENTRY;
            }
            return FAILED_ENTRY;
        }
        return DUPLICATE_ENTRY;
    }

    public function getListRuangan()
    {
        $stmt = $this->con->prepare("SELECT * FROM ruangan;");     
        if (!$stmt->execute()){
            return FAILED_ENTRY;
        }
        $stmt->bind_result($rId, $nmRuangan);
        
        $message = array();

        while($stmt->fetch()){
            $temp = array();
            $temp['r_id'] = (int)$rId;
            $temp['nm_ruangan'] = $nmRuangan;
            array_push($message, $temp);
        }

        return $message;
    }

    public function getRuangan(string $ruangan)
    {
        $rId = $ruangan;

        $stmt = $this->con->prepare("SELECT * FROM kerusakan WHERE r_id = ?");
        $stmt->bind_param("i", $rId);
        if (!$stmt->execute()){
            return FAILED_ENTRY;
        }
        $stmt->bind_result($kId, $_, $nmBarang, $jumlah, $tglLaporan, $jenisKerusakan);
        
        $messages = array();

        while ($stmt->fetch()){
            $temp = array();
            $temp['k_id'] = (int)$kId;
            $temp['nm_barang'] = $nmBarang;
            $temp['jumlah'] = $jumlah;
            $temp['tgl_laporan'] = $tglLaporan;
            $temp['jenis_kerusakan'] = $jenisKerusakan;

            array_push($messages, $temp);
        }

        return $messages;
    }

    public function insertKerusakan(array $kerusakan)
    {

        $rId = $kerusakan['r_id'];
        $nmBarang = $kerusakan['nm_barang'];
        $jumlah = $kerusakan['jumlah'];
        $tglLaporan = $kerusakan['tgl_laporan'];
        $jenisKerusakan = $kerusakan['jenis_kerusakan'];

        $stmt = $this->con->prepare("INSERT INTO kerusakan (
                                            r_id,
                                            nm_barang,
                                            jumlah,
                                            tgl_laporan,
                                            jenis_kerusakan)
                                        VALUES 
                                            (?,?,?,?,?)
                ");
        $stmt->bind_param("isiss", $rId, $nmBarang, $jumlah, $tglLaporan, $jenisKerusakan);
        if ($stmt->execute()){
            return SUCCESSFUL_ENTRY;
        }
        return FAILED_ENTRY;
    }

    public function deleteKerusakan(string $kerusakan)
    {
        $kId = $kerusakan;
        $stmt = $this->con->prepare("DELETE FROM kerusakan WHERE id = ?;");
        $stmt->bind_param("i", $kId);
        if ($stmt->execute()){
            return SUCCESSFUL_ENTRY;
        }
        return FAILED_ENTRY;
    }

    public function updateKerusakan(array $kerusakan)
    {
        $kId = $kerusakan['k_id'];
        $nmBarang = $kerusakan['nm_barang'];
        $jumlah = $kerusakan['jumlah'];
        $jenisKerusakan = $kerusakan['jenis_kerusakan'];

        $stmt = $this->con->prepare("UPDATE kerusakan 
                                        SET nm_barang = ?,
                                            jumlah = ?,
                                            jenis_kerusakan = ?
                                    WHERE id = ?");

        $stmt->bind_param("sisi", $nmBarang, $jumlah, $jenisKerusakan, $kId);
        if ($stmt->execute()){
            return SUCCESSFUL_ENTRY;
        }
        return FAILED_ENTRY;
    }

    public function getKerusakan(array $kerusakan){
        $kId = $kerusakan['k_id'];

        $stmt = $this->con->prepare("SELECT * FROM kerusakan WHERE id = ?");
        $stmt->bind_param("i", $kId);
        if (!$stmt->execute()){
            return FAILED_ENTRY;
        }
        
        $stmt->bind_result($kId, $rId, $nmBarang, $jumlah, $tglLaporan, $jenisKerusakan);

        $messages = array();

        while($stmt->fetch()){
            $temp = array();
            $temp['k_id'] = $kId;
            $temp['r_id'] = $rId;
            $temp['nm_barang'] = $nmBarang;
            $temp['jumlah'] = $jumlah;
            $temp['tgl_laporan'] = $tglLaporan;
            $temp['jenis_kerusakan'] = $jenisKerusakan;
            array_push($messages, $temp);
        }

        return $messages;
    }


    public function firstTime()
    {
        $this->_createTables();

        if(!$this->con->commit()){
            echo "Commit transaction failed!";
            return 0;
        }
        return 1;
    }

    public function isRuanganExist(string $nmRuangan)
    {
        $stmt = $this->con->prepare("SELECT nm_ruangan FROM ruangan WHERE nm_ruangan = ?");
        $stmt->bind_param("s", $nmRuangan);
        $stmt->execute();
        $stmt->store_result();
        return $stmt->num_rows > 0;
    }

    public function getIdRuangan(string $nmRuangan){
        $res = $this->con->query("SELECT id FROM ruangan WHERE nm_ruangan = '$nmRuangan'");
        return $res->fetch_array()['id'];
    }

    public function login(array $user){
        $username = $user['username'];
        $password = $user['password'];
        $res = $this->con->query("SELECT * FROM users WHERE username ='$username' AND password ='$password'");
        $count = $res->num_rows;
        $d = $res->fetch_array();
        
        // $stmt = $this->con->prepare("SELECT * FROM users WHERE username = ? AND password = ?");
        // $stmt->bind_param("ss", $username, $password);
        // if (!$stmt->execute()){
        //     return FAILED_ENTRY;
        // }
        // $stmt->bind_result($fetchedId, $fetchedUsername, $fetchedPassword, $fetchedPrivilege);
        $tempUser = array();

        if ($count == 0){
            $temp = array();
            $temp['login_status'] = "Failed";
            array_push($tempUser, $temp);
            return $tempUser;
        }


        $fetchedUsername = $d['username'];
        $fetchedPassword = $d['password'];
        $fetchedPrivilege = $d['privilege'];

        $temp = array();
        $temp['username'] = $fetchedUsername;
        $temp['password'] = $fetchedPassword;
        $temp['privilege'] = $fetchedPrivilege;
        $temp['login_status'] = "Success";
        array_push($tempUser, $temp);
        

        return $tempUser;
    }

    private function _mysqlExceptionHandler(mysqli_sql_exception $e)
    {
        require_once __DIR__ . "./DbConstant.php";

        $msg = strtolower($e->getMessage);
        
        if (strpos($msg, "duplicate") !== FALSE){
            return DUPLICATE_ENTRY;
        }else if($msg == ""){
            return SUCCESSFUL_ENTRY;
        }else{
            return FAILED_ENTRY;
        } 
    }

    private function _createTables()
    {

        $this->con->query("CREATE TABLE IF NOT EXISTS `kerusakan` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT,
            `r_id` bigint(20) NOT NULL,
            `nm_barang` text NOT NULL,
            `jumlah` tinyint(4) NOT NULL,
            `tgl_laporan` datetime NOT NULL,
            `jenis_kerusakan` text NOT NULL,
            PRIMARY KEY (`id`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
        ");

        $this->con->query("CREATE TABLE IF NOT EXISTS `ruangan` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT,
            `nm_ruangan` text NOT NULL,
            PRIMARY KEY (`id`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        ");

        $this->con->query("CREATE TABLE IF NOT EXISTS `users` (
            `id` bigint(20) NOT NULL AUTO_INCREMENT,
            `username` VARCHAR(20),
            `password` VARCHAR(32),
            `privilege` VARCHAR(10),
            PRIMARY KEY (`id`)
          )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        ");
    }
    
}

?>