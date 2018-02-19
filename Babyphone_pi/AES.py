# -*- coding: cp1252 -*-

from base64 import b64decode
from base64 import b64encode
from Crypto import Random
from Crypto.Cipher import AES


BLOCK_SIZE = 16


class KeyLengthError(Exception):
    """
    Wird geworfen, wenn das für die AES Verschlüsselung
    benötigte Passwort die falsche Länge hat.
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
    Realisiert die AES 128 CBC Verschlüsselung.
    """

    def check_key(self, raw_key):
        """
        Überprüft, ob der übergebene Schlüssel die
        richtige Länge hat.

        Argumente:
            raw_key (str): Schlüssel der geprüft wird.

        Rueckgabe:
            str: Der Schlüssel wenn korrekt, ansonsten wird
                 ein Fehler geworfen.
        """
        
        key = str(raw_key)
        if len(key) in [16, 24, 32]:
            return key
        else:
            raise KeyLengthError(len(key))

    def encrypt(self, raw_text, key):
        """
        Verschlüsselt einen String.

        Erhält als Parameter den zu verschlüsselnden Text, sowie
        das dafür zu verwendende Passwort.
        
        Argumente:
            raw_text (str): Zu verschlüsselnder String.
            key (str): Passwort für Verschlüsselung.

        Rueckgabe:
            str: Verschlüsselter String.
        """
        
        pad_text = pad(raw_text)
        iv = Random.new().read(AES.block_size)
        cipher = AES.new(key, AES.MODE_CBC, iv)
        return b64encode(iv + cipher.encrypt(pad_text))

    def decrypt(self, raw_encoded, key):
        """
        Entschlüsselt einen String.

        Erhält als Parameter den zu entschlüsselnden Text, sowie
        das dafür zu verwendende Passwort.

        Argumente:
            raw_encoded (str): Zu entschlüsselnder String.
            key (str): Passwort für Entschlüsselung.

        Rueckgabe:
            str: Entschlüsselter String.
        """
        
        encoded = b64decode(raw_encoded)
        iv = encoded[:16]
        cipher = AES.new(key, AES.MODE_CBC, iv)
        return unpad(cipher.decrypt(encoded[16:])).decode('utf8')
