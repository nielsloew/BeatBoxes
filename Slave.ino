/*
*
* Signal / Pin / Pin
* Reset / 9 / RST
* SPI SS / 10 / SDA
* SPI MOSI / 11 / MOSI
* SPI MISO / 12 / MISO
* SPI SCK / 13 / SCK
*
*/

#include <Wire.h>
#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 10
#define RST_PIN 9

MFRC522 mfrc522(SS_PIN, RST_PIN);

char id;

void setup() {
  Wire.begin();
  SPI.begin();
  mfrc522.PCD_Init();
  id = '4';  //Must be a unique identifier per Arduino master
}

int countX = 0;

void loop() {
  MFRC522::MIFARE_Key key;
  
  for (byte i = 0; i < 6; i++) key.keyByte[i] = 0xFF;
  if (mfrc522.PICC_IsNewCardPresent()) {
    if (mfrc522.PICC_ReadCardSerial()) {
  
      String s;
  
      for (byte i = 0; i < mfrc522.uid.size; i++) {
        s += String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
        s += String(mfrc522.uid.uidByte[i], HEX);
      }
    
      int j = s.length() + 1;
      char c[j];
      s.toCharArray(c, j);    
  
      Wire.beginTransmission(4);
      Wire.write(id);
      Wire.write('.');
      for (int i = 0; i < j; i++) if (c[i] >= 48 && c[i] <= 122) Wire.write(c[i]);
      Wire.endTransmission(); 
      countX = 0;
      delay(500); 
      return;
    }
  }
  countX++;
  if (countX > 5) {
    Wire.beginTransmission(4);
    Wire.write(id);
    Wire.write('.');
    Wire.write('x');
    Wire.endTransmission();
    countX = 0;
    delay(500);
    return;
  }
}
