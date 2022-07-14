# PrintNode-Java

PrintNode is a cloud printing service which allows you to connect any printer to your application using our PrintNode Client and easy to use JSON API.

This quick start guide covers using the Java API Library. There are examples to show how to use the API Library. It assumes that you have a [PrintNode](https://www.printnode.com) account.

## Quick Start

Here is some sample code:

```Java
import java.io.IOException;

import com.printnode.api.*;

public class Example {
    public static void main(String args[]) throws IOException {
        Auth myAuth = new Auth();
        myAuth.setApiKey("MyApiKey");

        PrintNodeClient myClient = new PrintNodeClient(myAuth);

        Computer[] myComputers = myClient.getComputers("");
    }
}
```

##Compiling from source

Run `mvn install` in the base folder.

