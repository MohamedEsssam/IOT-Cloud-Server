#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>
#include <string.h>

#define MAX_STRING_LEN  20
#define UDP         5555
#define TCP         5678


const char *ssid =  "MohamedEssam";     // replace with your wifi ssid and wpa2 key
const char *pass =  "moh@123456";
WiFiUDP Udp;
unsigned int localUdpPort = 6666;
char incomingPacket[256];
char replyPacket[] = "Hi there! Got the message :-)";
int portTCP = TCP;
int portUDP = UDP;
int i =0;
WiFiClient client;
IPAddress remoteIP(192,168,1,8);


int ledpin = 5; // D1
int button = 4; //D2

 
void setup() 
{
       Serial.begin(9600);
       delay(10);

//       pinMode(ledpin, OUTPUT);
//       pinMode(button, INPUT);
        
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
}
 
void loop() 
{      
    
//   if (digitalRead(button) == 1)
//  {
//  digitalWrite(ledpin, HIGH); 
//  delay(200);
//  }
//  if (digitalRead(button)==0)
//  {
//  digitalWrite(ledpin, LOW); 
//  delay(200);
//  }


  
    Udp.beginPacket(remoteIP, portUDP);
    Udp.write("Hello from 3as3oos client UDP");
    Udp.endPacket();
    
  int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    // receive incoming UDP packets
    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
    int len = Udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);
    //Serial.println(subStr(incomingPacket, " ", 3));

    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(replyPacket);
    Udp.endPacket();
  
  //TCP connection
    if (!client.connect(remoteIP, portTCP)) {
    Serial.println("connection failed");
    delay(5000);
    return;
  }
  // This will send a string to the server
  Serial.println("sending data to server");
  if (client.available()) {
    Serial.println("start TCP connection");
    client.write("Hello Server");
    Serial.write("Hello Server");
  }

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
  while (client.available()){
    char receivedMessage = static_cast<char>(client.read());
    Serial.print(receivedMessage);
    }

// Close the connection
//  Serial.println();
//  Serial.println("closing connection");
//  client.stop();    
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
     //Serial.print(".");
     sub = strtok_r(act, delim, &ptr);
     if (sub == NULL) break;
  }
  return sub;

}
