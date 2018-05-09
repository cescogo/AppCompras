<?PHP

require 'mysql_login.php';

$json=array();

	if(isset($_GET["nombre"]) && isset($_GET["pass"])){
		$nombre=$_GET["nombre"];
		$pass=$_GET["pass"];
				
		$conexion=mysqli_connect(HOSTNAME,USERNAME,PASSWORD,DATABASE);

		$consulta="select nombre,pass from users where nombre='{$nombre}' and pass='{$pass}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$json['usuario'][]=$registro;
		//	echo $registro['id'].' - '.$registro['nombre'].' - '.$registro['profesion'].'<br/>';
		}else{
			$resultar["id"]=0;
			$resultar["nombre"]='no registrado';
			$resultar["pass"]='no registrado';
			$json['usuario'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['usuario'][]=$resultar;
		echo json_encode($json);
	}
?>
