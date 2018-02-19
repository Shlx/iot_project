# -*- coding: cp1252 -*-

from base64 import b64decode
from base64 import b64encode
from Crypto import Random
from Crypto.Cipher import AES


BLOCK_SIZE = 16


class KeyLengthError(Exception):
    """
    Wird geworfen, wenn das f�r die AES Verschl�sselung
    ben�tigte Passwort die falsche L�nge hat.
    """

    def __init__(self, length):
        self.length = length

    def __str__(self):
        return "Key length should be 16, 24 or 32, but was " + str(self.length) + "."
    

def pad(raw_text):
    return raw_text + (BLOCK_SIZE - len(raw_text) % BLOCK_SIZE) * \
                chr(BLOCK_SIZE - len(raw_text) % BLOCK_SIZE)

def unpad(raw_text):
    return raw_text[:-ord(raw_text[len(raw_text) - 1:])]


class AES_128_CBC(object):
    """
    Realisiert die AES 128 CBC Verschl�sselung.
    """

    def check_key(self, raw_key):
        """
        �berpr�ft, ob der �bergebene Schl�ssel die
        richtige L�nge hat.

        Argumente:
            raw_key (str): Schl�ssel der gepr�ft wird.

        Rueckgabe:
            str: Der Schl�ssel wenn korrekt, ansonsten wird
                 ein Fehler geworfen.
        """
        
        key = str(raw_key)
        if len(key) in [16, 24, 32]:
            return key
        else:
            raise KeyLengthError(len(key))

    def encrypt(self, raw_text, key):
        """
        Verschl�sselt einen String.

        Erh�lt als Parameter den zu verschl�sselnden Text, sowie
        das daf�r zu verwendende Passwort.
        
        Argumente:
            raw_text (str): Zu verschl�sselnder String.
            key (str): Passwort f�r Verschl�sselung.

        Rueckgabe:
            str: Verschl�sselter String.
        """
        
        pad_text = pad(raw_text)
        iv = Random.new().read(AES.block_size)
        cipher = AES.new(key, AES.MODE_CBC, iv)
        return b64encode(iv + cipher.encrypt(pad_text))

    def decrypt(self, raw_encoded, key):
        """
        Entschl�sselt einen String.

        Erh�lt als Parameter den zu entschl�sselnden Text, sowie
        das daf�r zu verwendende Passwort.

        Argumente:
            raw_encoded (str): Zu entschl�sselnder String.
            key (str): Passwort f�r Entschl�sselung.

        Rueckgabe:
            str: Entschl�sselter String.
        """
        
        encoded = b64decode(raw_encoded)
        iv = encoded[:16]
        cipher = AES.new(key, AES.MODE_CBC, iv)
        return unpad(cipher.decrypt(encoded[16:])).decode('utf8')
