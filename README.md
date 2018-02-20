### iot_project (Babyphone)

Projekt für das Wahlfach 'Internet der Dinge' an der HSRM. Diese Readme-Datei dient gleichzeitig als Dokumentation bzw. Wiki für das Projekt.

## Idee
Grundsätzlich soll im Laufe des Projekts ein smartes Babyphone entstehen, welches neben dem Baby selbst ggf. auch einen Babysitter überwachen kann. Verwendet wird dafür ein Raspberry Pi inklusive einiger Sensoren, die Laute und Bewegungen aufzeichnen sollen. Diese Signale werden nach der Verarbeitung in einer Datenbank zwischengespeichert, um dann mit der zugehörigen Android-App eingesehen werden zu können.

Auf dieser App können die Eltern dann jederzeit den Status des Babys einsehen und so außerdem prüfen, ob der Babysitter sich richtig um das Baby kümmert. Des Weiteren können hier Statistiken eingesehen werden, um den Zustand des Babys über einen Zeitraum hinweg zu beobachten.

## Verwendete Hardware
Der Hauptbestandteil der verwendeten Hardware ist der Raspberry Pi 2 Model B. Dieser soll die Daten der verschiedenen Sensoren verarbeiten und mit der entsprechenden Geschäftslogik auswerten können. 

Der Raspberry Pi ist ein Einplatinencomputer, der über vier USB Ports, 40 Pins, einen HDMI-Ausgang, einen Kopfhörer-Ausgang, ein MicroSD Lesegerät und einen Micro USB Anschluss verfügt. Der Micro-USB-Anschluss dient zum Anschließen des Netzteils, sodass der Raspberry Pi mit Strom versorgt werden kann. Damit eine Verbindung zum Internet hergestellt werden kann, existieren zwei Möglichkeiten: Zum Einen per LAN, zum Anderen mit einem WLAN Stick. Dieser ist in unserem Fall der Wi-Fi Nano USB-Adapter und belegt einen der vier USB-Ports.

Der wichtigste Sensor für dieses Projekt ist das USB-Mikrofon. Es besitzt eine hohe Empfindlichkeit und hat eine effektive Reichweite von etwa zwei Metern, in denen Geräusche aufgenommen werden können. Es unterdrückt unerwünschte Hintergrundgeräusche, sodass es sich für unseren Einsatz sehr gut eignet, denn es soll registrieren können, wenn das Baby schreit.

Um zu erfahren, ob sich der Babysitter schnellstmöglich um das Baby kümmert, wird ein weiterer Sensor benötigt. Dabei handelt es sich um einen Bewegungsmelder. In unserem Fall wird der Mini-Bewegungssensor HC-SR505 verwendet, dieser besitzt eine Reichweite von drei Metern, hat einen Abtastwinkel von 100 Grad, besitzt eine Einschaltverzögerung und funktioniert mit Infrarot. Dieser Mini-Bewegungssensor benötigt eine Platine auf die er aufgesteckt werden kann. Die Platine wurde von Herrn Beckmann bereits für ein anderes Projekt montiert und wird in gleicher Konfiguration in diesem Projekt ebenfalls eingesetzt. Die Platine besitzt einen Drehencoder, einen Schiebeschalter, einen Knopf, die Halterung für den Bewegungsmelder und vier LEDs. 

## Funktionsweise
Mithilfe der eingehenden Daten des Mikrofons und des Bewegungsmelders wird der Status des Babys ermittelt. Das Mikrofon soll hierbei erkennen, ob das Baby am Schlafen (fast keine Geräusche), am Ruhen (nur leise Geräusche) oder am Schreien (laute Geräusche) ist. Der Bewegungsmelder erkennt, ob sich der Babysitter über der Babykrippe befindet und sich um das Baby kümmert.

Die aufgezeichneten Daten werden an den Server gesendet, verarbeitet und in eine Datenbank eingetragen. Nachdem man sich in der Android-App eingeloggt hat, kann man diese Daten auch auf seinem Handy einsehen und weiß so auch unterwegs immer über den Zustand des Babys Bescheid. Die App zeigt hauptsächlich eine Liste der Zustände des Babys an, wo betrachtet werden kann, zu welchen Zeitpunkten der Zustand des Babys (bzw. des Sitters) sich verändert hat.

## Server
Für das vorliegende Projekt wird ein Server verwendet. Dieser soll mehrere Babysitter-Monitore verwalten können und die Daten empfangen, sowie in einer Datenbank speichern. Im Folgenden können die Daten mit einem Smartphone per Android-App abgefragt werden.

Mit diesem Server soll nicht nur der eigentliche Babysitter-Monitor das Produkt sein, sondern vielmehr die Dienstleistung, die Überwachung zu ermöglichen. Der eigentliche Babysitter-Monitor soll einen Support von einem Jahr inklusive haben, dafür muss man sich am Server unter der URL `iot.tutoring-team.de` mit seinem Babysitter-Monitor registrieren. Dazu ist eine Webanwendung, welche auf dem Server läuft, notwendig. Für diesen Zweck ist auf dem Server PHP Version 5.6 installiert, sowie ein Apache-Service. Die neuen Funktionen in PHP Version 7.0 werden in diesem Projekt nicht benötigt, weswegen sich für die etwas ältere Version 5.6 entschieden wurde. Den Support kann man in der Webanwendung jeder Zeit verlängern.

Um die Daten des Babysitter-Monitors und dessen Sensoren zu speichern, wird eine Datenbank benötigt. In diesem Fall wird eine MySQL Datenbank Version 5.5 verwendet, dies ist die aktuellste Version auf dem Server.
Da auch der Sicherheitsaspekt bei diesem Projekt berücksichtigt werden muss, werden die Daten des Monitors in AES 128 mit dem CBC-Verfahren verschlüsselt.

## Sicherheit
# Allgemeine Maßnahmen
Die Sicherheitsaspekte für diese Anwendung umfassen unter anderem das sichere Versenden und Speichern der Daten zur späteren Verwendung. Es wurde sich dafür entschieden, möglichst wenige persönliche Daten über das Netz zu übertragen. Aus diesem Grund wurde unter anderem von einer Implementierung abgesehen, die das Aufzeichnen und gegebenenfalls Versenden von Ton- und / oder Bildaufnahmen vorsieht.

# Senden der Sensordaten an den Server
Um die Vertraulichkeit der gesendeten Daten zu gewährleisten, werden diese als verschlüsselter String gesendet. Als Verschlüsselung wird AES 128 nach dem CBC-Verfahren verwendet. Allgemein handelt es sich bei dem DES-Nachfolger AES um eine Blockchiffre, die Texte nach dem Rijndal-Algorithmus verschlüsselt. Es handelt sich dabei um ein symmetrisches Verfahren, was bedeutet, dass zum Ver- und Entschlüsseln der selbe Schlüssel benutzt wird. Allgemein lässt das Rijndal-Verfahren mehrere verschiedene Schlüssellängen zu. Die in diesem Projekt verwendete Implementierung PyCrypto schränkt die möglichen Schlüssellängen auf 128, 192 oder 256 Bit ein. Konkret wurde eine Schlüssellänge von 128 Bit, das entspricht 16 Zeichen, gewählt.

Das AES-Verfahren gilt als sehr sicher und für diese Anwendung bietet eine Schlüssellänge von 128 Bit ausreichend Sicherheit. Das Programm ist jedoch so konzipiert, dass eine andere Art der Verschlüsselung möglichst einfach nachgerüstet werden kann, falls dies zu einem späteren Zeitpunkt notwendig werden sollte. Sowohl das AES-Verfahren an sich, als auch das entsprechende Python-Modul PyCrypto dürfen frei und ohne anfallende Lizenzgebühren verwendet werden. PyCrypto wird in Version 2.6.1 im Projekt verwendet. Außer der bereits erwähnten AES-Verschlüsselung in verschiedenen Varianten bietet es auch weitere Verfahren (zum Beispiel DES und RSA), sowie verschiedene Hash-Funktionen an. Das Modul wird zur Zeit von seinen Entwickeln als „vollständig und fertig“ eingestuft. Vorhandene Teile werden sich also voraussichtlich in Zukunft nicht mehr ändern, sodass die Kompatibilität gewährleistet bleibt. Allerdings wird es noch aktiv gepflegt, was bedeutet, dass im Falle neuer Entwicklungen diese in das Modul integriert werden.

Der hier verwendete CBC (Cypher Block Chaining) Modus stellt eine weitere Absicherung dar. Eine AES Verschlüsselung in ihrer einfachsten Form erlaubt es anhand des verschlüsselten Textes zu erkennen, wie oft ein bestimmtes Zeichen im Klartext vorkommt. Dies wird durch die Verwendung von CBC vermieden. Hierbei sorgt ein zusätzlicher Initialisierungsvektor (IV) dafür, dass nicht in jedem Block ein bestimmtes Klartextzeichen auf immer das selber Zeichen bei der Verschlüsselung abgebildet wird. Dadurch ist es nicht mehr möglich am verschlüsselten Text zu erkennen, wie oft ein Zeichen im Klartext vorkommt. Der IV sollte für jede Verschlüsselung neu gewählt werden und darf zufällig sein. Er muss nicht geheimgehalten werden und kann zusammen mit der Nachricht im Klartext an den Empfänger übertragen werden.

# Empfangen der Daten in der Android-App
Die Android-App verwendet Klassen aus dem `javax.crypto`-package, welches sich als Standard für Java-Crypto-APIs durchgesetzt hat. Die App empfängt die verschlüsselten Texte des Servers und entschlüsselt diese mithilfe von Methoden, die von dieser API bereitgestellt werden. Nachdem ein `Cipher`-Objekt mit entsprechenden Parametern initialisiert wurde, kann der Text über dessen Methoden dekodiert werden, um schließlich ein [`BabyEntry`](/Babyphone/app/src/main/java/marvin/babyphone/model/BabyEntry.java)-Objekt aus dem Eintrag zu machen. Diese Funktionalität wird von der Klasse [`AesCrypt`](/Babyphone/app/src/main/java/marvin/babyphone/security/AesCrypt.java) bereitgestellt und im [`ResponseHandler`](/Babyphone/app/src/main/java/marvin/babyphone/ResponseHandler.java) verwendet.

# Weitere Quellen
AES  
https://de.m.wikipedia.org/wiki/Advanced_Encryption_Standard

CBC (Cypher Block Chaining)  
https://de.m.wikipedia.org/wiki/Cipher_Block_Chaining_Mode

PyCrypto  
https://pypi.python.org/pypi/pycrypto

javax.crypto  
https://docs.oracle.com/javase/7/docs/api/javax/crypto/package-summary.html

## Datenbank
Die Datenbank besteht aus insgesamt zwei Tabellen:

# Loginkey
Diese Tabelle enthält die Monitorid als Fremdschlüssel und den Userkey als Primärschlüssel.  Die Monitorid ist vom Typ varchar und entspricht dem Pi-Namen. Er wird für das Registrieren der Applikation und zum Abrufen der Daten benötigt. Der Userkey ist ebenfalls vom Typ varchar, dieser Key wird bei der Registrierung erstellt und ist MD5 verschlüsselt. Der Key muss in die Applikation eingegeben werden, nur so kann die Applikation auf die Daten des dazugehörigen Pis zugreifen. Die notwendigen Daten liegen in der Log-Tabelle.

# Log
Diese Tabelle enthält die ID als Primärschlüssel, User als Fremdschlüssel, sowie die Spalten Date und Data. Die ID ist vom Typ bigint und wird per Auto-Increment in der Datenbank erzeugt. Die Spalte User ist vom Typ varchar und ist identisch mit der Monitorid in der Loginkey-Tabelle. Über diesen Fremdschlüssel wird eine Verbindung zwischen den beiden Tabellen hergestellt. Die Spalte Date ist vom Typ bigint, hier wird das Datum vom Eintragungszeitpunkt in die Datenbank gespeichert. Gespeichert wird das Datum als Unix Timestamp. Die Spalte Data ist vom Typ varchar und enthält einen AES 128 CBC verschlüsselten String. Dieser String enthält alle Daten, die vom PI über die jeweiligen Sensoren erfasst werden.

