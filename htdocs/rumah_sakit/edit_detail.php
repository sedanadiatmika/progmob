<?php

if($_SERVER['REQUEST_METHOD']=='POST') {

    $nama = isset($_POST['nama']) ? $_POST['nama'] : '';
    $username = isset($_POST['username']) ? $_POST['username'] : '';
    $jenis_kelamin = isset($_POST['jenis_kelamin']) ? $_POST['jenis_kelamin'] : '';
    $tgl_lahir = isset($_POST['tgl_lahir']) ? $_POST['tgl_lahir'] : '';
    $no_telp = isset($_POST['no_telp']) ? $_POST['no_telp'] : '';
    $alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';
    $id = isset($_POST['id']) ? $_POST['id'] : '';


    require_once 'connect.php';

    $sql = "UPDATE tb_users SET nama='$nama', username='$username', jenis_kelamin='$jenis_kelamin', tgl_lahir='$tgl_lahir', no_telp='$no_telp', alamat='$alamat' WHERE id='$id' ";

    if(mysqli_query($conn, $sql)) {

        $result["success"] = "1";
        $result["message"] = "success";

        echo json_encode($result);
        mysqli_close($conn);

    }

} else {

    $result["success"] = "0";
    $result["message"] = "Error!";
    echo json_encode($result);

    mysqli_close($conn);

}

?>