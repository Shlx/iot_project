<?php
    /**
    * Enthält die Datenbankkonfigurationen für Test
    * und Betrieb. Durch setzen einer globalen
    * Variablen kann zwischen beiden gewechselt werden.
    */

	$is_on_1und1 = false;

	if ($is_on_1und1) {
	    $host_name = 'db708628189.db.1and1.com';
	    $database = 'db708628189';
	    $user_name = 'dbo708628189';
	    $password = 'iot_898742_MA';
	}
	else {
	    $host_name = 'localhost';
	    $database = 'bsmonitor';
	    $user_name = 'root';
	    $password = '';
	}

?>