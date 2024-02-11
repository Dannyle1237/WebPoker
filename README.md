# WebPoker

##Vision
The goal of this project was to develop a multiplayer poker website that ensured the satisfaction of our stakeholder (in our case, the professor). 

Throughout the project, emphasis was not only placed on technical implementation, but also on understanding and meeting the needs of the stakeholder. 
This involved the development of a requirements specification sheet, which was constantly being changed to simulate how real-world software development is handled. 

##Tech Stack
Our web application consisted of utilizing Javascript, HTML, CSS to develop the front-end, while pure Java handled the back-end server.
The front-end and back-end servers communicated through the use of websockets, to keep track of real-time connections during the game and allow constant communication from client to server. 

## How to Run
CSE3310 UTA Spring 2022 Student Group Project


From the command line
```bash
cd WebPoker
mvn clean
mvn compile
mvn package
mvn exec:java -Dexec.mainClass=uta.cse3310.WebPoker
```
point a webbrowser to 127.0.0.1:8080/example.html

The projects will be hosted on

http://webpoker.info/


Where are the jarfiles?
```bash
% find ~/ -name "*.jar"
```


https://www.me.uk/cards/makeadeck.cgi

## How to play
1.)Go to URL http://webpoker.info:8083/
2.)Enter name and then join lobby
3.)Wait until at least another person joins the lobby, then ready up. 
4.)Once both players are ready, the game will start!
5.)Have the time of your life gambling fake money.


