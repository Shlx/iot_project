# iot_project (Babyphone)

Projekt für das Wahlfach 'Internet der Dinge' an der HSRM

# Verwendete Hardware
Der Hauptbestandteil der verwendeten Hardware ist der Raspberry Pi 2 Model B. Dieser soll die Daten der verschiedenen Sensoren verarbeiten mit der entsprechenden Geschäftslogik auswerten können. 

Der Raspberry Pi ist ein Einplatinencomputer, der über 4 USB Ports, 40 Pins, einen HDMI Ausgang, einen Kopfhörer-Ausgang, ein MicroSD Lesegerät und einen Micro USB Anschluss verfügt. Der Micro USB Anschluss dient zum Anschließen des Netzteils, sodass der Raspberry Pi mit Strom versorgt werden kann. Damit eine Verbindung zum Internet mit dem Raspberry Pi hergestellt werden kann, existieren zwei Möglichkeiten: Zum einen per LAN, zum anderen mit einem WLAN Stick, dieser ist in unserem Fall der Wi-Fi Nano USB Adapter und belegt einen USB Port von insgesamt vier Stück.

Der wichtigste Sensor für dieses Projekt ist das USB Mikrofon. Es besitzt eine hohe Empfindlichkeit und hat eine effektive Entfernung von etwa zwei Metern, in denen es Geräusche aufnehmen kann. Es unterdrückt unerwünschte Hintergrundgeräusche, sodass es sich für unseren Einsatz sehr gut eignet, denn es soll registrieren können, wenn das Baby schreit.

Um zu erfahren, ob sich der Babysitter schnellstmöglich um das Baby kümmert, wird ein weiterer Sensor benötigt. Dabei handelt es sich im einen Bewegungsmelder. In unserem Fall wird der Mini Bewegungssensor HC-SR505 verwendet, dieser besitzt eine Reichweite von drei Metern, hat einen Abtastwinkel von 100 Grad, besitzt eine Einschaltverzögerung und funktioniert mit Infrarot. Dieser Mini Bewegungssensor benötigt eine Platine auf die er aufgesteckt werden kann. Die Platine wurde von Herrn Beckmann für ein anderes Projekt bereits montiert und wird in gleicher Konfiguration in diesem Projekt ebenfalls eingesetzt. Die Platine besitzt einen Drehencoder, einen Schiebeschalter, einen Knopf, die Halterung für den Bewegungsmelder und vier LEDs. 

