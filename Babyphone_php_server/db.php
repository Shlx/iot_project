<?php

    # An mehreren Stellen verwendete Funktionen laden

    require 'tools.php';

    # Datenbankkonfiguration laden

    require 'config.php';

    # Parameter auslesen und entsprechende Variablen anlegen

    if(isset($_GET["mode"])) {
        $m = $_GET["mode"];
    } 

    if(isset($_GET["user"])) {
        $u = $_GET["user"];
    }

    if(isset($_GET["pass"])) {
        $p = $_GET["pass"];
    }

    if(isset($_GET["data"])) {
        $d = $_GET["data"];
    }

    if(isset($_GET["amount"])) {
        $a = $_GET["amount"];
    }

    if(isset($u) && isset($p)) {
        $k = build_key($u, $p);
    }

    # Verbindung zur Datenbank aufbauen

    $db = mysqli_connect($host_name, $user_name, $password, $database);

    # Verarbeitung beginnen

    if (mysqli_connect_errno()) {
        print_r("DB_ERROR");
    } else {
        if(isset($m)) {
            # Standard-Info vom Pi empfangen
            if ($m == "s0") {
                if(isset($u) && isset($d)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."'");
                    $count = mysqli_fetch_all($result)[0][0];
                    if($count == 1) {
                        $tstamp = time();    
                        $result = mysqli_query($db, "INSERT INTO log(ID, USER, DATETIME, DATA) VALUES (NULL,'".$u."',".$tstamp.",'".$d."')");
                        print_r($tstamp);
                    }
                    else {
                        print_r("NAME_ERROR");
                    }
                }
                else {
                    print_r("INSERT_ERROR");
                }
            }
            # Standard-Info abfragen, dabei werden die 3 aktuellsten
            # Einträge zurückgegeben
            else if ($m == "r0") {
                if(isset($u) && isset($k)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."' AND userkey = '".$k."'");
                    $rows = mysqli_fetch_all($result);
                    if($rows[0][0] == 0) {
                        print_r("KEY_ERROR");
                    }
                    else if ($rows[0][0] == 1) {              
                        $result = mysqli_query($db, "SELECT * FROM log WHERE USER = '".$u."' ORDER BY DATETIME DESC LIMIT 3");
                        $rows = mysqli_fetch_all($result);
                        foreach ($rows as $key => $value) {
                            print_r($value[2].";".$value[3]."#");
                        }
                    }
                    else {
                        print_r("REGISTER_ERROR");
                    }
                }
                else {
                    print_r("READ_ERROR");
                }
            }
            # n Datensätze abfragen, es werden die n aktuellsten
            # Einträge zurückgegeben
            else if ($m == "r1") {
                if(isset($u) && isset($k)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."' AND userkey = '".$k."'");
                    $rows = mysqli_fetch_all($result);
                    if ($rows[0][0] == 0) {
                        print_r("KEY_ERROR");
                    }
                    else if ($rows[0][0] == 1) {          
                        if(isset($a)) {   
                            $piname = $rows[0][0];
                            $result = mysqli_query($db, "SELECT * FROM log WHERE USER = '".$u."' ORDER BY DATETIME DESC LIMIT ".$a);
                            $rows = mysqli_fetch_all($result);
                            foreach ($rows as $key => $value) {
                                print_r($value[2].";".$value[3]."#");
                            }
                        }
                        else {
                            print_r("AMOUNT_ERROR");
                        }
                    }
                    else {
                        print_r("REGISTER_ERROR");
                    }
                }
                else {
                    print_r("READ_ERROR");
                }
            }
            # Neue Datensätze abfragen, es werden die Einträge zurückgegeben,
            # die seit dem mitgelieferten Zeitpunkt erfolgt sind.
            # Der Zeitpunkt muss dabei als Unix Timestamp angegeben werden.
            else if ($m == "r2") {
                if(isset($u) && isset($k)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."' AND userkey = '".$k."'");
                    $rows = mysqli_fetch_all($result);
                    if ($rows[0][0] == 0) {
                        print_r("KEY_ERROR");
                    }
                    else if ($rows[0][0] == 1) {          
                        if(isset($a)) {   
                            $piname = $rows[0][0];
                            $result = mysqli_query($db, "SELECT * FROM log WHERE USER = '".$u."' and DATETIME > ".$a." ORDER BY DATETIME DESC");
                            $rows = mysqli_fetch_all($result);
                            foreach ($rows as $key => $value) {
                                print_r($value[2].";".$value[3]."#");
                            }
                        }
                        else {
                            print_r("AMOUNT_ERROR");
                        }
                    }
                    else {
                        print_r("REGISTER_ERROR");
                    }
                }
                else {
                    print_r("READ_ERROR");
                }
            }
            # Alle Infos zu einer ID / einem Key löschen, dabei werden alle
            # durch den entsprechenden Pi erfolgten Statuseinträge gelöscht.
            else if ($m == "d0") {
                if(isset($u) && isset($k)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."' AND userkey = '".$k."'");
                    $rows = mysqli_fetch_all($result);
                    if ($rows[0][0] == 0) {
                        print_r("KEY_ERROR");
                    }
                    else if ($rows[0][0] == 1) {
                        $result = mysqli_query($db, "DELETE FROM log WHERE user = '".$u."'");
                        print_r(time());
                    }
                    else {
                        print_r("DELETE_ERROR");
                    }
                }
            }
            # Registrierung aufheben, dabei werden sowohl alle durch den
            # entsprechenden Pi erfolgten Statuseinträge, als auch die
            # Registrierung selbst gelöscht. Macht eine erneute Registrierung
            # des Pis notwendig.
            else if ($m == "u0") {
                if(isset($u) && isset($k)) {
                    $result = mysqli_query($db, "SELECT COUNT(monitorid) FROM loginkey WHERE monitorid = '".$u."' AND userkey = '".$k."'");
                    $rows = mysqli_fetch_all($result);
                    if ($rows[0][0] == 0) {
                        print_r("KEY_ERROR");
                    }
                    else if ($rows[0][0] == 1) {
                        $result = mysqli_query($db, "DELETE FROM loginkey WHERE monitorid = '".$u."'");
                        $result = mysqli_query($db, "DELETE FROM log WHERE user = '".$u."'");
                        print_r(time());
                    }
                    else {
                        print_r("UNREGISTER_ERROR");
                    }
                }
            }
            else {
                print_r("UNKNOWN_MODE_ERROR");
            }
        }
        else {
            print_r("MISSING_MODE_ERROR");
        }

        # Verbindung zur Datenbank schliessen

        mysqli_close($db);
    }

?>