# -*- coding: cp1252 -*-

import requests

from random import random
from time import sleep, time, localtime, asctime

from HardwareInterface import HardwareInterface
from AES import AES_128_CBC as core


class PasswordNotFoundError(Exception):
    """
    Wird geworfen, wenn das f�r die AES Verschl�sselung
    ben�tigte Passwort nicht in der config-Datei gefunden
    wird.
    """

    def __init__(self):
        pass

    def __str__(self):
        return "No password found in the config file."
    

class Sound(object):
    """
    Wertet die erkannte Laust�rke aus.

    Die Vorgehensweise zur Ermittlung des aktuellen Zustands des
    Babys ist in dieser Klasse gekapselt.
    """

    def __init__(self, low, high, size):
        """
        Konstruktor der Klasse.

        Nimmt die Grenzwerte f�r die Erkennung der verschiedenen
        Zust�nde, in denen sich das �berwachte Baby befinden kann,
        entgegen. Sp�tere KLassifizierungen basieren auf diesen
        Grenzwerten.

        size gibt an, wie viele aufeinander folgende gemessene
        Lautst�rken benutzt werden, um durch Bilden des Durch-
        schnitts den aktuellen Wert / die aktuelle Stufe zu
        bestimmen.

        Argumente:
            low (int): Grenzwert zwischen 'schlafen' und 'wach'.
            high (int): Grenzwert zwischen 'wach' und 'schreiend'.
            size (int): Anzahl der Lautst�rken, die sich gemerkt
                        werden.
        """
        
        self.low = low
        self.high = high
        self.size = size
        self.memory = []


    def percept(self, p):
        """
        Verarbeitet eine Audio-Wahrnehmung.

        Erh�lt als Parameter einen vom Mikrophon stammenden
        Messwert. F�r eine korrekte Auswertung m�ssen die Grenz-
        werte (siehe __init__) korrekt gew�hlt werden. Die Auswertung
        erfolgt auf Basis der letzten size �bergebenen Wahrnehmungen.

        Argumente:
            p (int): Vom Mikrophon gemessener Lautst�rke-Wert.

        Rueckgabe:
            int: Werte von 0 bis 2, dabei gilt 0 = schlafend,
                 1 = wach (leise Ger�usche m�glich) und
                 2 = schreiend.
        """
		
		if len(self.memory) < self.size:
			self.memory.append(p)
		else:
			self.memory.pop(0)
			self.memory.append(p)
			
		sound_in = sum(self.memory) / len(self.memory)
		if sound_in < self.low:
			return 0
		else:
			if sound_in < self.high:
				return 1
			else:
				return 2


class Movement(object):
    """
    Wertet die erkannte Bewegung aus.

    Die Vorgehensweise zur Ermittlung des aktuellen Zustands des
    Babys ist in dieser Klasse gekapselt.
    """

    def percept(self, p):
        """
        Verarbeitet eine Bewegungs-Wahrnehmung.

        Erh�lt als Parameter einen vom Bewegungsmelder stammenden
        Messwert. True, bzw. 1, wenn eine Bewegung erkannt wurde,
        sonst 0. Dieser Wert wird gem�� der verwendeten Logik in
        eine der verwendeten Stufen umgesetzt.

        Argumente:
            p (int): Vom Bewegungsmelder erkannte Bewegung.

        Rueckgabe:
            int: Werte von 0 oder 1, dabei gilt 0 = keine Bewegung
                 im Raum erkannt, 1 = Bewegung im Raum erkannt.
        """
        
        move_in = p
        if move_in == True:
            return 1
        else:
            return 0



class CryptoWrapper(object):
    """
    Kapselt ein Objekt, dass die Verschl�sselung realisiert.

    Durch die Kapselung des f�r die Verschl�sselung verwendeten
    Objekts soll erreicht werden, dass dieses bei Bedarf, wenn
    zum Beispiel eine andere Verschl�sselung verwendet werden
    soll, ausgetauscht werden kann, ohne weitere �nderungen am
    Programm notwendig zu machen.
    """

    def __init__(self, core):
        """
        Konstruktor der Klasse.

        Argumente:
            core (Klasse f�r Verschl�sselung): Objekt einer Klasse,
                 die eine Verschl�sselung realisiert.
        """
        
        self.core = core

    def encrypt(self, raw_text, key):
        """
        Verschl�sselt einen String.

        Erh�lt als Parameter den zu verschl�sselnden Text, sowie
        das daf�r zu verwendende Passwort. Wie die Verschl�sselung
        im Einzelnen stattfindet, wird nicht von dieser Klasse
        implementiert, sondern ist Teil des core-Objekts.

        Argumente:
            raw_text (str): Zu verschl�sselnder String.
            key (str): Passwort f�r Verschl�sselung.

        Rueckgabe:
            str: Verschl�sselter String.
        """
        
        return self.core.encrypt(raw_text, key)

    def decrypt(self, raw_encoded, key):
        """
        Entschl�sselt einen String.

        Erh�lt als Parameter den zu entschl�sselnden Text, sowie
        das daf�r zu verwendende Passwort. Wie die Entschl�sselung
        im Einzelnen stattfindet, wird nicht von dieser Klasse
        implementiert, sondern ist Teil des core-Objekts.

        Argumente:
            raw_encoded (str): Zu entschl�sselnder String.
            key (str): Passwort f�r Entschl�sselung.

        Rueckgabe:
            str: Entschl�sselter String.
        """
        
        return self.core.decrypt(raw_encoded, key)

    

class Sender(object):
    """
    Sendet die Daten an den Server.

    Erh�lt beim Erzeugen die Adresse des Servers an den die
    Daten gesendet werden sollen. Der Datenstring wird zusammen-
    gebaut und anschliessend mit Hilfe des CryptoWrapper-Objekts
    verschl�sselt. Danach erfolgt das Senden an den Server.
    """

    def __init__(self, crypto, address, memory_size = 10):
        """
        Konstruktor der Klasse.

        Argumente:
            crypto (CryptoWrapper): Dient der Verschl�sselung.
            adress (str): Adresse des Servers.
            memory_size (int): Anzahl an Sendungen, die der Sender sich
                               merkt. Haupts�chlich f�r Testzwecke.
        """
        
        self.crypto = crypto
        self.address = address
        self.memory_size = memory_size
        self.memory = []

    def info_string(self):
        """
        Setzt den Sttring f�r das Versenden zusammen.
        """
        
        latest = self.memory[len(self.memory) - 1]
        # date, movement, sound
        return str(latest[0]) + ";" + str(latest[2]) + ";" + str(latest[3])

    def remember(self, sound, movement):
        """
        Sorgt daf�r, dass ein neuer Datensatz dem Memory hinzugef�gt
        wird. Dabei wird der �lteste, sofern das maximum erreicht
        wurde, entfernt.

        Argumente:
            sound (int): Audio-Wahrnehmung.
            movement (int): Wahrnehmung Bewegungsmelder.
        """
        
		timestamp = time()
		timestruct = localtime(timestamp)      
		if len(self.memory) < self.memory_size:
			self.memory.append((timestamp, timestruct, sound, movement))
		else:
			self.memory.pop(0)
			self.memory.append((timestamp, timestruct, sound, movement))

    def send(self, name, data, mode, password):
        """
        �bernimmt das eigentlich Senden.

        Aktuell nur ein Modus implementiert, weitere k�nnen mit
        minimalem Aufwand hinzugef�gt werden.

        Argumente:
            name (str): Pi-Name.
            data (str): Wahrnehmungen und Zeitstempel = Daten.
            mode (str): Modus mit dem gesendet werden soll.
            password (str): Passwort f�r AES Verschl�sselung.
        """
        
        if mode == "STD_UPDATE":
            encrypted = self.crypto.encrypt(data, password)
            answer = requests.get(self.address, params={'mode': "s0", 'user': name, 'data': encrypted})

    def process(self, name, sound, movement, password, mode):
        """
        Verarbeitet eine Set an Wahrnehmungen.

        Argumente:
            name (str): Pi-Name.
            sound (int): Audio-Wahrnehmung.
            movement (int): Bewegungsmelder Wahrnehmung.
            password (str): Passwort f�r AES Verschl�sselung.
            mode (str): Modus mit dem gesendet werden soll.
        """
        
        self.remember(sound, movement)          
        if mode:
	    self.send(name, self.info_string(), "STD_UPDATE", password)

    def __str__(self):
        result = ""
        for entry in self.memory:
            result += "\tdate: " + str(entry[0]) + "\n\tsound: " + str(entry[2]) + "\n\tmovement: " + str(entry[3]) + "\n\n"
        return result


class Monitor(object):
    """
    Koordiniert die Abl�ufe.

    Holt Passwort und Username aus der config-Datei und
    initiert das Senden.
    """

    def __init__(self, sender):
        """
        Konstruktor der Klasse.

        Argumente:
            sender (Sender): Sender-Objekt.
        """
        
        self.sender = sender
        self.password = self.get_password()
        self.username = self.get_username()
        
    def info(self):
        """
        Gibt Auskunft �ber den Aktuellen Status.

        Rueckgabe:
            str: EAktueller Status des enthaltenen Senders.
        """
        
        return str(self.sender)

    def get_password(self):
        """
        Holt das Passwort aus der config-Datei.
        """
        
        configfile = file("config.monitor", "r")           
        for line in configfile:
            tmp = line.rstrip().split("=")
            if tmp[0] == "password":
                configfile.close()
                return tmp[1]
            configfile.close()
            raise PasswordNotFoundError()

    def get_username(self):
        """
        Holt den Usernamen aus der config-Datei.
        """
	configfile = file("config.monitor", "r")           
	for line in configfile:
	    tmp = line.rstrip().split("=")
	    if tmp[0] == "username":
		configfile.close()
		return tmp[1]
	    configfile.close()
	    raise PasswordNotFoundError()

    def process(self, sound, movement, mode):
        """
        Verarbeitet und sendet die �bergebenen Daten.

        Argumente:
            sound (int): Audio-Wahrnehmung.
            movement (int): Wahrnehmung Bewegungsmelder.
            mode (str): Modus in dem gesendet werden soll.
                        Aktuelle nur einer implementiert.
        """
        
	self.sender.process(self.username, sound, movement, self.password, mode)

        
def main():

    try:	
	hwr = HardwareInterface()
	sound_logic = Sound(250, 900, 5)
	movement_logic = Movement()
	monitor = Monitor(Sender(CryptoWrapper(core()), 'http://www.tutoring-team.de/IOT/db.php', 3))
	last_s = -1
	last_m = -1
	count_id = 0
		
	while True:
	    s = sound_logic.percept(hwr.get_state_lautstarke())
	    m = movement_logic.percept(hwr.get_state_bewegung())
	    if (last_s != s) or (last_m != m):	
		if hwr.get_state_schalter():
		    monitor.process(s, m, False)
		    last_s = s
		    last_m = m
		    print "ID:", count_id, "\n", monitor.info()
		else:
		    monitor.process(s, m, True)
		    last_s = s
		    last_m = m
		count_id += 1
	    if hwr.get_state_taster():
		break
	    sleep(1)
		
    except:
	pass
		
    finally:
	hwr.clean()


if __name__ == "__main__":
    main()
