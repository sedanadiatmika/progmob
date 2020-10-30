<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $id = isset($_POST['id']) ? $_POST['id'] : '';

    require_once 'connect.php';

    $sql = "SELECT * FROM tb_users WHERE id='$id' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['read'] = array();

    if (mysqli_num_rows($response) === 1) {

        if ($row = mysqli_fetch_assoc($response)) {

            $h['nama']      = $row['nama'];
            $h['username']  = $row['username'];
            $h['jenis_kelamin']      = $row['jenis_kelamin'];
            $h['tgl_lahir']      = $row['tgl_lahir'];
            $h['no_telp']  = $row['no_telp'];
            $h['alamat']      = $row['alamat'];

            array_push($result["read"], $h);

            $result["success"] = "1";
            echo json_encode($result);
        }

    }

} else {

    $result["success"] = "0";
    $result["message"] = "Error!";
    echo json_encode($result);

    mysqli_close($conn);

}

?>