<?php
	/**
    * Erzeugt aus dem eingegebenen Benutzernamen und dem
    * eingegebenen Namen den eindeutigen Key.
    *
    * @param string $name Der Benutzername
    * @param string $pwd Das Passwort
    *
    * @return string
    */
	function build_key($name, $pwd)
    {
    	$p = false;

		if(strlen($pwd) >= 8) {
			$p = true;
		}

		if($p) {
			#return md5($name.$pwd);
			return md5($name.$pwd);
		}
		else {
			return false;
		}
    }

?>