#include <SoftwareSerial.h>

#define BAUD_RATE       9600
#define rxPin            12
#define txPin            13

SoftwareSerial myBluetooth(rxPin, txPin); // RX, TX

int ungle_array;
boolean start_read = false;

void setup()  
{
  Serial.begin(9600);
  Serial.println("AT");
  pinMode(rxPin,  INPUT);
  pinMode(txPin,  OUTPUT);
  myBluetooth.begin(BAUD_RATE);
}

void get_data()
{
  unsigned char data;
  if (myBluetooth.available() > 0)
  {
      data = (unsigned char)myBluetooth.read();
      Serial.println(data);
      if (data == 's')
      {
        start_read = true;
        Serial.println("s sent");
      }
      else if(data == 'd')
      {
        start_read = false;
      }
      else
      {
        //date
      }        
  }
  else
  {
    //something wrong
    //Serial.println("ERROR");
  }      
}

void loop() // run over and over
{
   //Serial.println("ERROR");
  get_data();//get data
}
