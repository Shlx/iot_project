<?php

	write_index();

    /**
    * Schreibt den für die Registrierungs-Seite notwendigen
    * HTML-Code.
    */
	function write_index() 
	{
		echo '<!DOCTYPE html>';
		echo '<html>';
			echo '<body bgcolor="#fcd6f6">';
				echo '<table width="50%" align="center" border="0" bgcolor="#debaf4">';
					echo '<tr>';
						echo '<td>';
							echo '<h1>Babysitter-Monitor Registration 
							      Service</h1>';
							echo '<hr>';
							echo '<h2>Guide</h2>';
							echo 'Please enter the id of your monitor and 
							      your newly chosen password below.
							      After sending you will recieve a key. 
							      You have to enter said key into the 
							      supervisor app you use to retrieve the 
							      data from your monitor online.';
							echo '<h2>Registration</h2>';
							echo '<center>';							
								echo '<form action="register.php" method="post">';
									echo 'Your monitors id:<br>';
									echo '<input type="text" name="monitorid" value="">';
									echo '<br>';
									echo 'Your new password:<br>';
									echo '<input type="password" name="password" value="">';
									echo '<br>';
									echo '<br>';
									echo '<input type="submit" value="Send">';
								echo '</form>';
							echo '</center>';
							echo '<h2>Tips</h2>';
							echo '<ul>';
							echo '<li>You will find your monitors id on 
							      its backside.</li>';
							echo '<li>Your password has to be at least 
							      8 characters long.</li>';
							echo '</ul>';
						echo '</td>';
					echo '</tr>';
				echo '</table>';
			echo '</body>';
		echo '</html>';	
	}

?>