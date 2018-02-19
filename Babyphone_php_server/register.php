<?php
	require 'tools.php';

	process_input();

    /**
    * Verarbeitet die übergebenen Daten für die Registrierung.
    * Dabei wird, falls alle benötigten Daten korrekt angegeben
    * wurden, ein Eintrag mit der neuen Registrierung in der
    * Datenbank angelegt. Eine entsprechende Rückmeldung
    * für den Benutzer, ob und wie die Eintragung erfolgt ist
    * oder welche Fehler ggf. aufgetreten sind, wird für den
    * Benutzer als HTML-Seite geschrieben.
    */
	function process_input()
	{
		require 'config.php';

		echo '<!DOCTYPE html>';
		echo '<html>';
			echo '<body bgcolor="#fcd6f6">';
				echo '<table width="50%" align="center" bgcolor="#debaf4">';
					echo '<tr>';
						echo '<td>';
							echo '<h1>Key Generator</h1>';
							echo '<hr>';

		# Es sollte für realen Betrieb geprüft werden, ob diese ID
		# werksseitig überhaupt an ein Gerät vergeben wurde.
		# Hier weggelassen!
		$mid_ok = true;

		if(strlen($_POST["password"]) >= 8 && $mid_ok) {

			$db = mysqli_connect($host_name, $user_name, $password, $database);

			$monitorid = $_POST["monitorid"];
			$result = mysqli_query($db, "SELECT COUNT(USERKEY) FROM loginkey WHERE MONITORID = '".$monitorid."'");
			$count = mysqli_fetch_all($result)[0][0];

			if($count == 0) {
				$new_key = build_key($_POST["monitorid"], $_POST["password"]);
				$result = mysqli_query($db, "INSERT INTO loginkey(MONITORID, USERKEY) VALUES ('".$monitorid."','".$new_key."')");
				$key_exists = false;
			}
			else {
				$key_exists = true;
			}

			mysqli_close($db);

			if(!$key_exists) {
				echo '<h2>Congratulations!</h2>';
				echo 'You have registered successfully.';
				echo '<br>';
				echo '<br>';
				echo '<center>';
					echo '<form action="index.php">';
						echo '<input type="submit" value="Register another monitor">';
					echo '</form>';
				echo '</center>';
				echo '<br>';
				echo '<br>';
			}
			else {
				echo '<h2>This monitor has already been registered!</h2>';
				echo 'Please check your data.';
				echo '<br>';
				echo '<br>';
				echo '<center>';
					echo '<form action="index.php">';
						echo '<input type="submit" value="Try again!">';
					echo '</form>';
				echo '</center>';
				echo '<br>';
				echo '<br>';
			}
		}
		else {
			echo '<h2>An error has occurred!</h2>';
			echo 'Please do not forget to provide the following
			      information and check your entries:';
			echo '<br>';
			echo '<ul>';
			if(strlen($_POST["monitorid"]) < 8) {
				echo '<li>Monitor-ID</li>';
			}
			if(strlen($_POST["password"]) < 8) {
				echo '<li>Your password</li>';
			}
			echo '</ul>';
			echo '<br>';
			echo '<center>';
				echo '<form action="index.php">';
					echo '<input type="submit" value="Try again!">';
				echo '</form>';
			echo '</center>';
			echo '<br>';
			echo '<br>';
		}

						echo '</td>';
					echo '</tr>';
				echo '</table>';
			echo '</body>';
		echo '</html>';
	}
?>