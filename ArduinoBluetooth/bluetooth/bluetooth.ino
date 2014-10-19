#include <SoftwareSerial.h>

SoftwareSerial myBluetooth(0, 1); // RX, TX

int ungle_array;
boolean start_read = false;

byte ledPin = 13;

void setup()  
{
  Serial.begin(9600);
  myBluetooth.begin(9600);
}

void get_data()
{
  unsigned char data;
  if (myBluetooth.available() > 0)
  {
      data = (unsigned char)myBluetooth.read();
      if (data == 's')
      {
        start_read = true;
        Serial.println(data);
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
  }      
}

void loop() // run over and over
{
  get_data();//get data
}
