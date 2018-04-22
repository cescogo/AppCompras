<?PHP

require 'mysql_login.php';

$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);
	$nombre = $_POST["nombre"];
	$pass = $_POST["pass"];
	$sql="INSERT INTO users(nombre,pass) VALUES (?,?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('ss',$nombre,$pass);
		
	if($stm->execute()){
		echo "registra";
	}else{
		echo "noRegistra";
	}
	
	mysqli_close($conexion);
?>
