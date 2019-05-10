#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>
#include <string.h>

#define MAX_STRING_LEN  20
#define UDP         5555
#define TCP         5678
#define high        1
#define low         0
#define WifiName "Prism1"
#define WifiPassword  "987654321"



const char *ssid = WifiName ;    // replace with your wifi ssid and wpa2 key
const char *pass = WifiPassword ;
char incomingPacket[256];
char TCPincomingPacket[256];
int portTCP = TCP;
int portUDP = UDP;
int startTcpFlag = low;
int Flag = 0;
int ledpin = 5; // D1
int button = 4; //D2
int i=0;
String reply;

WiFiClient client;
WiFiUDP Udp;

//broadcast ip
IPAddress remoteIP(192,168,0,119);
char replyPacket[] = "publish";



 
void setup() 
{
       Serial.begin(9600);
       delay(10);

       pinMode(ledpin, OUTPUT);
       pinMode(button, INPUT);
        
       Serial.println("Connecting to ");
       Serial.println(ssid);

        
  
       WiFi.begin(ssid, pass); 
       while (WiFi.status() != WL_CONNECTED) 
          {
            delay(500);
            Serial.print(".");
          }
      Serial.println("");
      Serial.println("WiFi connected"); 
      Serial.print("IP address: ");
      Serial.println(WiFi.localIP());
      Udp.begin(portUDP);
      Serial.printf("Now listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), portUDP); 

      Udp.beginPacket(remoteIP, portUDP);
      Udp.write("Hello from 3as3oos client UDP");
      Udp.endPacket();
      delay(5000);
      int packetSize = Udp.parsePacket();
  if (packetSize != -1)
  {
    startTcpFlag = high;
    // receive incoming UDP packets
    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
    int len = Udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);   
  }
}

void loop() 
{      
   if (digitalRead(button) == 1){
  

     //recieve udp packet and start tcp   
    if(startTcpFlag){
      if(!Flag){
      //TCP connection
    if (client.connect(remoteIP, portTCP)) {
      Flag=1;
      client.write("subscribe led button?");
    // wait for data to be available
  unsigned long timeout = millis();
  while (client.available() == 0) {
    if (millis() - timeout > 5000) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      delay(60000);
      return;
    }
  }

  // Read all the lines of the reply from server and print them to Serial
  Serial.println("receiving from remote server");
   while (client.available()) {
    char startMessage = static_cast<char>(client.read());
    Serial.print(startMessage);
  }
  Serial.println();
  
    // This will send a string to the server
  Serial.println("sending data to server");
  client.write("publish button 1?");
  delay(5000);

  Serial.println("receiving from remote server");
   while(client.available()){
 reply= client.readStringUntil('\r');
Serial.print(reply);
}
Serial.println();
char charBuf[100];
reply.toCharArray(charBuf,100);
Serial.println(subStr(charBuf," ",3));
if(strcmp(subStr(charBuf," ",3), "1")==0){
           Serial.println("LED On");
           digitalWrite(ledpin, HIGH); 
           delay(200);
        }
        else{
          Serial.println("LED Off");
           digitalWrite(ledpin, LOW); 
           delay(200);
          }
//while (client.available()){
//     char TCPincomingPacket = client.read();
//     Serial.println(TCPincomingPacket);
//    }
//    Serial.println((char *)subStr(TCPincomingPacket, " ", 1));
//       char* Output = (char *)subStr(TCPincomingPacket, " ", 1);
//       if(strcmp(Output, "1")){
//           Serial.println("LED On");
//           digitalWrite(ledpin, HIGH); 
//           delay(2000);
//        }
//        else{
//          Serial.println("LED Off");
//           digitalWrite(ledpin, LOW); 
//           delay(200);
//          }

  

  Serial.println();
  Serial.println("*");
  //client.stop();
delay(5000);    
       
   }
   else{
    Serial.println("connection failed");
    delay(5000);
    return;
    }    
  }
    }
   }
  }


// Function to return a substring defined by a delimiter at an index
char* subStr (char* str, char *delim, int index) {
  char *act, *sub, *ptr;
  static char copy[MAX_STRING_LEN];
  int i;

  // Since strtok consumes the first arg, make a copy
  strcpy(copy, str);

  for (i = 1, act = copy; i <= index; i++, act = NULL) {
     sub = strtok_r(act, delim, &ptr);
     if (sub == NULL) break;
  }
  return sub;
}
