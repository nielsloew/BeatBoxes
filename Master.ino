#include <Wire.h>
#include <SimpleTimer.h>

SimpleTimer timer;
String s[4];

void setup() {
  Wire.begin(4);
  Wire.onReceive(receiveEvent);
  Serial.begin(9600);
  timer.setInterval(100, repeatMe);
  s[0] = "1.x";
  s[1] = "2.x";
  s[2] = "3.x";
  s[3] = "4.x";
}

void loop() {
  timer.run();  
}

void receiveEvent(int howMany) {
  String buffer;

  while (Wire.available() != 0) {
    char c = Wire.read();
    buffer += c;
  }
  
  String id = "";
  String tagUID = "";
  int cPosition = buffer.indexOf('.');
   
  if (cPosition != -1) {
    id = buffer.substring(0,cPosition);
    tagUID = buffer.substring(cPosition + 1, buffer.length());
  }
    
  if (id.equals("1")) s[0] = id + "." + tagUID;
  else if (id.equals("2")) s[1] = id + "." + tagUID;
  else if (id.equals("3")) s[2] = id + "." + tagUID;
  else if (id.equals("4")) s[3] = id + "." + tagUID;
}

void repeatMe() {
  Serial.println(s[0]);
  Serial.println(s[1]);
  Serial.println(s[2]);
  Serial.println(s[3]);
}
