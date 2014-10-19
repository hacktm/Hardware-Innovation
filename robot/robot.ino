#include <Wire.h>
#include <HMC5883L.h>

#define trigPin 51
#define echoPin 50

#define OUT1PIN1 30     // H-bridge OUT1.1
#define OUT1PIN2 31   // H-bridge OUT1.2
#define ENABLE1 2    // H-bridge Ena 1
#define OUT2PIN1 32   // H-bridge OUT2.1
#define OUT2PIN2 33   // H-bridge OUT2.2
#define ENABLE2 3    // H-bridge Ena 2

HMC5883L compass;
#define ARRAY_LENGHT 7
int array[100]={60,90,110, 100, 85, 70, 90, 110};
int i=1,led=41;
float last_val;

void setup()   
{
  Serial.begin(9600);
  Wire.begin();
  
  compass = HMC5883L(); //new instance of HMC5883L library
  setupHMC5883L(); //setup the HMC5883L
  
  pinMode(trigPin, OUTPUT); //Distance sensor Trigger
  pinMode(echoPin, INPUT);  //Distance sensor EchoPIN
  pinMode(led, OUTPUT); //LED pin
  digitalWrite(led, LOW);  //LED-off. initialization
  pinMode(OUT1PIN1, OUTPUT);// setup Motor 1 pins
  pinMode(OUT1PIN2, OUTPUT);
  pinMode(OUT2PIN1, OUTPUT);// setup Motor 2 pins
  pinMode(OUT2PIN2, OUTPUT);
  pinMode(ENABLE1, OUTPUT);//pwm speed pins
  pinMode(ENABLE2, OUTPUT);
  
  last_val=getAngle();    
}

void loop() // my pen is huge
{
  if(distance()) 
    {
     move();
     digitalWrite(led, LOW);
    }
    else
    halt();
}

void move()
{
  float angle_mag,angle_dir,val;
  if(i<=ARRAY_LENGHT)
  {
    angle_dir=array[i-1]-array[i]; //the direction from angle from Array 
    angle_mag = getAngle();   //angle from Magnetometer
    val=angle_mag+angle_dir;//magn. angle + angle to take          
    if(val>360)      //val - is value that the robto will take
      val=val-360;
    if(val<0)
      val=val+360;
    if(abs(val-last_val)<=7)
    {
      forward(160);
      i++;
      last_val = val;
    }
    else 
    {
      if (angle_mag+angle_dir > 359)
      {
        turn(40,250); //left
      }
      else if (angle_mag+angle_dir < 0)
      {
        turn(250,40); //right
      }
      else
      {
        if (val<last_val)
          turn(250,40); //right
        if(val>last_val)
          turn(40,250); //left
      }
    }
  }
  else
  {
    halt(); //stop robot
   }
}

int distance()
{
 long duration, distance;
 bool flag;
  
  digitalWrite(trigPin, LOW);
  digitalWrite(trigPin, HIGH);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance = (duration/2) / 29.1;
    if(distance>100)
      distance=100;
    if(distance<1)
      distance=1;  

     if(distance<=8)
        flag=0;
       else
        flag=1;
        
 Serial.println(distance);//debug purposes 
 return flag;
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
 digitalWrite(led, HIGH);
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
