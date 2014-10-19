#include <Wire.h>
#include <HMC5883L.h>

#define OUT1PIN1 30     // H-bridge OUT1.1
#define OUT1PIN2 31   // H-bridge OUT1.2
#define ENABLE1 2    // H-bridge Ena 1
#define OUT2PIN1 32   // H-bridge OUT2.1
#define OUT2PIN2 33   // H-bridge OUT2.2
#define ENABLE2 3    // H-bridge Ena 2

HMC5883L compass;

int array[100]={60,63,70,50},init_val;  
    //my penis

void setup()   
{
  Serial.begin(9600);
  Wire.begin();
  
  compass = HMC5883L(); //new instance of HMC5883L library
  setupHMC5883L(); //setup the HMC5883L 
  
  pinMode(OUT1PIN1, OUTPUT);// setup Motor 1 pins
  pinMode(OUT1PIN2, OUTPUT);
  pinMode(OUT2PIN1, OUTPUT);// setup Motor 2 pins
  pinMode(OUT2PIN2, OUTPUT);
  pinMode(ENABLE1, OUTPUT);//pwm speed pins
  pinMode(ENABLE2, OUTPUT);
  
  init_val=getAngle();
    
}

int i=1;

void loop() 
{
float angle_mag,angle_dir,val;

angle_dir=array[i-1]-array[i]; //the direction from angle from Array 
          Serial.println(angle_dir);
          
angle_mag = getAngle();   //angle from Magnetometer
          Serial.println(angle_mag);
          
val=angle_mag+angle_dir;//magn. angle + angle to take          
if(val>360)      //val - is value that the robto will take
  val=val-360;
if(val<0)
  val=val+360;


if(abs(val-angle_mag)<=5)
  forward(180);
else 
  {
    if (val>angle_mag)
      turn(230,50); //right
    if(val<angle_mag)
      turn(50,230); //left
  }


i++;
if(i==sizeof(array))
  halt(); //stop robot

}

 
void forward(int accel)
{
 digitalWrite(OUT1PIN1, HIGH);   
 digitalWrite(OUT1PIN2, LOW);
 digitalWrite(OUT2PIN1, LOW);   
 digitalWrite(OUT2PIN2, HIGH);
 analogWrite(ENABLE1, accel );
 analogWrite(ENABLE2, accel );
}

void reverse(int accel)
{
 digitalWrite(OUT1PIN1, LOW);   
 digitalWrite(OUT1PIN2, HIGH);
 digitalWrite(OUT2PIN1, HIGH);   
 digitalWrite(OUT2PIN2, LOW);
 analogWrite(ENABLE1, accel );
 analogWrite(ENABLE2, accel );
}

void turn(int accel_1,int accel_2)
{
  // accel_1 > accel_2 means Right
  // accel_1 < accel_2 means Left
 digitalWrite(OUT1PIN1, HIGH);   
 digitalWrite(OUT1PIN2, LOW);
 digitalWrite(OUT2PIN1, LOW);   
 digitalWrite(OUT2PIN2, HIGH);
 analogWrite(ENABLE1, accel_1 );
 analogWrite(ENABLE2, accel_2 );
}

void halt()
{
 digitalWrite(OUT1PIN1, LOW);   
 digitalWrite(OUT1PIN2, LOW);
 digitalWrite(OUT2PIN1, LOW);   
 digitalWrite(OUT2PIN2, LOW);
 analogWrite(ENABLE1,0 );
 analogWrite(ENABLE2,0 ); 
 }
 
void setupHMC5883L(){
  //Setup the HMC5883L, and check for errors
  int error;  
  error = compass.SetScale(1.3); //Set the scale of the compass.
  if(error != 0) Serial.println(compass.GetErrorText(error)); //check if there is an error, and print if so

  error = compass.SetMeasurementMode(Measurement_Continuous); // Set the measurement mode to Continuous
  if(error != 0) Serial.println(compass.GetErrorText(error)); //check if there is an error, and print if so
}

float getAngle(){
  //Get the reading from the HMC5883L and calculate the heading
  MagnetometerScaled scaled = compass.ReadScaledAxis(); //scaled values from compass.
  float heading = atan2(scaled.YAxis, scaled.XAxis);

  // Correct for when signs are reversed.
  if(heading < 0) heading += 2*PI;
  if(heading > 2*PI) heading -= 2*PI;

  return heading * RAD_TO_DEG; //radians to degrees
}
 
//map(val, 0, 1023, 0, 179);
