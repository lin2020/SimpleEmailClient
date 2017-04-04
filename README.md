# SimpleEmailClient

## Intro
A simple email client written in Java. Its features include sending and receiving mail and storage of local mail.
#### `mail protocol`
The email client supports three mail protocols to send and receive email.
- __`POP3`__ - A simple mail access protocol, pull emails from server, but local operations are not sync to the server.
- __`IMAP`__ - A powerful mail access protocol, pull emails from server, can sync local operations to the server.
- __`SMTP`__ - Push emails to server, transfer mail from source address to destination address.

#### `database`
The email client supports storage of local mail, using sqlite-jdbc.

## Build
You can download the project and build it on your _windows_ operating system and run it to send and receive email.
#### `frame`
The code sources include folders, _com_ folder and _test_ folder.
- __`com`__ - Contains five folders that hold different classes of functions
- __`test`__ - Contains test units that used to test each functional class in src folders

#### `compile`
For each class, there is a test class for unit test. You can compile a class and then compile its test class and run test program to see how it work. For example:
``` cmd
// compile a com class
javac -d bin -cp bin com/protocol/Pop3.java
// compile a test class
javac -d bin -cp bin test/protocol/Pop3Test.java
// run a test program
java -cp bin test.lin.protocol.Pop3Test
```
At compile time, if we do not use the -encoding parameter to specify the encoding format of our JAVA source code, javac.exe will get our operating system default encoding format. In _windows_, the default encoding format is _-encoding gbk*_. IF the encoding format of our JAVA source code is not _-encoding gbk*_, we should use the -encoding parameter to specify it. For example:
``` cmd
// compile a code file encoded with utf-8
javac -encoding utf-8 -d bin -cp bin com/view/LoginFrame.java
```

## More
To be continued...

## License
MIT
