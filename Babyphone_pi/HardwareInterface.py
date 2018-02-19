#!/usr/bin/env python
# -*- coding: cp1252 -*-

from time import sleep
from random import randint

import alsaaudio
import audioop
import RPi.GPIO as GPIO

#Fehlercodes vom Mikro
mikro_errors = [0, -32]

#Nimmt nur die Pin-Zahl anstatt die GPIO
GPIO.setmode(GPIO.BOARD)

#Warnungen aus für alle Funktionen
GPIO.setwarnings(False)

#Schiebeschalter
GPIO.setup(37, GPIO.IN)
GPIO.setup(33, GPIO.IN)

#LEDs
#gruen
GPIO.setup(38, GPIO.OUT)
#gelb
GPIO.setup(36, GPIO.OUT)
#rot
GPIO.setup(31, GPIO.OUT)
#blau
GPIO.setup(24, GPIO.OUT)

# Bewegungsmelder
GPIO.setup(21, GPIO.IN)

# Taster zum Herunterfahren
GPIO.setup(35, GPIO.IN)


class HardwareInterface(object):
    """
    Regelt den Zugriff auf die unterliegende Hardware.

    get-Methoden fragen den erkannten Zustand an den Eingabe-Geraeten ab.
    set-Methoden setzen den Zustand der Ausgabe Geraete.
    """

    def __init__(self):
        """
        Initialisiert die Einstellungen fuer das Mikrofon.
        """

        ## Objekt fuer Einstellungen und Zugriff auf das Mikrofon
        self.mic_input = alsaaudio.PCM(alsaaudio.PCM_CAPTURE, alsaaudio.PCM_NONBLOCK, 'sysdefault:CARD=Device')
        self.mic_input.setchannels(1)
        self.mic_input.setrate(8000)
        self.mic_input.setformat(alsaaudio.PCM_FORMAT_S16_LE)
        self.mic_input.setperiodsize(160)


    def get_state_taster(self):
        """
        Liest den Zustand des Tasters.

        Gibt 1 zurueck, wenn dieser zum Zeitpunkt des Aufrufs gedrueckt ist,
        sonst 0.

        Rueckgabe:
            bool: True wenn Taster gedrueckt, sonst False.
        """
        
        if GPIO.input(35):
            return 0
        else:
            return 1
        

    def get_state_schalter(self):
        """
        Liest den Zustand des Schalters aus.

        Gibt 0 zurueck, wenn er auf AUS bzw. Position 1 steht und 1, wenn er
        auf EIN bzw. Position 2 steht.

        Rueckgabe:
            bool: True wenn Schalter auf EIN, sonst False.
        """
        
        if GPIO.input(33):
            return 0
        elif GPIO.input(37):
            return 1


    def get_state_bewegung(self):
        """
        Liest den Zustand des Bewegungsmelders aus.

        Gibt 0 zurueck, wenn er keine Bewegung erkennt und 1, wenn er Bewegung
        erkennt.

        Rueckgabe:
            bool: True bei erkannter Bewegung, sonst False.
        """
        
        state = GPIO.input(21)
        if state == 0:
            return 0
        elif state == 1:
            return 1


    def get_state_lautstarke(self):
        """
        Liest die Mikrofon-Information ein.

        Wandelt die erkannte Laustaerke in einen ganzzahligen Wert von 0 bis 5
        um. Je lauter, desto hoeher der Wert.

        Rueckgabe:
            int: Gemessene Lautstärke, direkt vom Mikrophon.
        """
	
        global mikro_errors
		
        l, data = self.mic_input.read()
        while l in mikro_errors:
            l, data = self.mic_input.read()
            sleep(0.01)
                            
        try:
            maxi = audioop.max(data, 2)   
            sleep(0.01)
            return maxi
        except:
            pass


    def set_state_led_gruen(self, new_state):
        """
        Setzt den Zustand der gruenen LED.

        Der neue Zustand entspricht dem Wert des Arguments.

        Argumente:
            new_state (bool): Neuer Zustand der gruenen LED
        """
        
        if(not isinstance(new_state, bool) and new_state != 1 and new_state != 0):
            raise Exception("new_state is not a valid value")

        if new_state == 0:
            GPIO.output(38, False)
        else:
            GPIO.output(38, True)
            

    def set_state_led_gelb(self, new_state):
        """
        Setzt den Zustand der gelben LED.

        Der neue Zustand entspricht dem Wert des Arguments.

        Argumente:
            new_state (bool): Neuer Zustand der gelben LED
        """

        if(not isinstance(new_state, bool) and new_state != 1 and new_state != 0):
            raise Exception("new_state is not a valid value")

        if new_state == 0:
            GPIO.output(36, False)
        else:
            GPIO.output(36, True)
            

    def set_state_led_rot(self, new_state):
        """
        Setzt den Zustand der roten LED.

        Der neue Zustand entspricht dem Wert des Arguments.

        Argumente:
            new_state (bool): Neuer Zustand der roten LED
        """
        
        if(not isinstance(new_state, bool) and new_state != 1 and new_state != 0):
            raise Exception("new_state is not a valid value")

        if new_state == 0:
            GPIO.output(31, False)
        else:
            GPIO.output(31, True)


    def set_state_led_blau(self, new_state):
        """
        Setzt den Zustand der blauen LED.

        Der neue Zustand entspricht dem Wert des Arguments.

        Argumente:
            new_state (bool): Neuer Zustand der blauen LED
        """
        
        if(not isinstance(new_state, bool) and new_state != 1 and new_state != 0):
            raise Exception("new_state is not a valid value")

        if new_state == 0:
            GPIO.output(24, False)
        else:
            GPIO.output(24, True)


    def clean(self):
        GPIO.cleanup()
