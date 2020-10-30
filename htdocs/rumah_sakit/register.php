<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST'){

    $nama = isset($_POST['nama']) ? $_POST['nama'] : '';
    $jenis_kelamin = isset($_POST['jenis_kelamin']) ? $_POST['jenis_kelamin'] : '';
    $tgl_lahir = isset($_POST['tgl_lahir']) ? $_POST['tgl_lahir'] : '';
    $no_telp = isset($_POST['no_telp']) ? $_POST['no_telp'] : '';
    $alamat = isset($_POST['alamat']) ? $_POST['alamat'] : '';
    $username = isset($_POST['username']) ? $_POST['username'] : '';
    $password = isset($_POST['password']) ? $_POST['password'] : '';

    $password = password_hash($password, PASSWORD_DEFAULT);

    require_once 'connect.php';

    $sql_check = "SELECT * FROM tb_users WHERE username='$username' ";

    $response = mysqli_query($conn, $sql_check);

    if (mysqli_num_rows($response) > 0) {
        $result["success"] = "2";
        $result["message"] = "Registrasi gagal, username sudah terdafatar";

        echo json_encode($result);
        mysqli_close($conn);
    } else {
        $sql = "INSERT INTO tb_users(nama, jenis_kelamin, tgl_lahir, no_telp, alamat, username, password) VALUES ('$nama','$jenis_kelamin', '$tgl_lahir', '$no_telp', '$alamat', '$username', '$password')";

        if(mysqli_query($conn, $sql) ) {
            $result["success"] = "1";
            $result["message"] = "success";

            echo json_encode($result);
            mysqli_close($conn);
        } else {
            $result["success"] = "0";
            $result["message"] = "error";

            echo json_encode($result);
            mysqli_close($conn);
        }
    }

}

?>