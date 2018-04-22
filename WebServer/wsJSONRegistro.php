<?PHP

require 'mysql_login.php';

$json=array();

	if(isset($_GET["nombre"]) && isset($_GET["pass"])){
		$nombre=$_GET['nombre'];
		$pass=$_GET['pass'];
		
		$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);
		
		$insert="INSERT INTO users(nombre,pass) VALUES ('{$nombre}','{$pass}')";
		$resultado_insert=mysqli_query($conexion,$insert);
		
		if($resultado_insert){
			$consulta="SELECT * FROM users WHERE nombre = '{$nombre}'";
			$resultado=mysqli_query($conexion,$consulta);
			
			if($registro=mysqli_fetch_array($resultado)){
				$json['usuario'][]=$registro;
			}
			mysqli_close($conexion);
			echo json_encode($json);
		}
		else{
			$resulta["nombre"]='No Registra';
			$resulta["pass"]='No Registra';
			$json['usuario'][]=$resulta;
			echo json_encode($json);
		}
		
	}
	else{
			$resulta["nombre"]='WS No retorna';
			$resulta["pass"]='WS No retorna';
			$json['usuario'][]=$resulta;
			echo json_encode($json);
		}

?>

